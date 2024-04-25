package com.jawbr.dnd5e.exptracker.service;

import com.jawbr.dnd5e.exptracker.dto.mapper.CampaignDTOMapper;
import com.jawbr.dnd5e.exptracker.dto.response.CampaignDTO;
import com.jawbr.dnd5e.exptracker.entity.Campaign;
import com.jawbr.dnd5e.exptracker.exception.CampaignNotFoundException;
import com.jawbr.dnd5e.exptracker.exception.IllegalParameterException;
import com.jawbr.dnd5e.exptracker.repository.CampaignRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final CampaignDTOMapper campaignDTOMapper;
    private final CurrentAuthUserService currentAuthUser;

    public CampaignService(CampaignRepository campaignRepository,
                           CampaignDTOMapper campaignDTOMapper,
                           CurrentAuthUserService currentAuthUser)
    {
        this.campaignRepository = campaignRepository;
        this.campaignDTOMapper = campaignDTOMapper;
        this.currentAuthUser = currentAuthUser;
    }

    public Page<CampaignDTO> findJoinedCampaigns(Integer page, Integer pageSize, String sortBy) {
        page = Optional.ofNullable(page).orElse(0);
        pageSize = Math.min(Optional.ofNullable(pageSize).orElse(6), 15);
        String sortByField = Optional.ofNullable(sortBy)
                .filter(s -> !s.isEmpty())
                .map(s -> switch(s) {
                    case "creator", "creator_username" -> "creator";
                    case "name" -> "name";
                    default -> throw new IllegalParameterException(String.format("Parameter '%s' is illegal.", sortBy));
                })
                .orElse("id");

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortByField));

        Page<Campaign> campaigns = campaignRepository.findJoinedCampaignsByUserId(currentAuthUser.getCurrentAuthUser().getId(), pageable);

        return campaigns.map(campaignDTOMapper::mapJoinedCampaignsToDTO);
    }

    public Page<CampaignDTO> findCreatedCampaigns(Integer page, Integer pageSize, String sortBy) {
        page = Optional.ofNullable(page).orElse(0);
        pageSize = Math.min(Optional.ofNullable(pageSize).orElse(6), 15);
        String sortByField = Optional.ofNullable(sortBy)
                .filter(s -> !s.isEmpty())
                .map(s -> {
                    if(s.equals("name"))
                        return "name";
                    else
                        throw new IllegalParameterException(String.format("Parameter '%s' is illegal.", sortBy));
                })
                .orElse("id");

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortByField));

        Page<Campaign> campaigns = campaignRepository.findCreatedCampaignsByUserId(currentAuthUser.getCurrentAuthUser().getId(), pageable);

        return campaigns.map(campaignDTOMapper::mapCreatedCampaignsToDTO);
    }

    public CampaignDTO findJoinedCampaignByUuid(UUID campaignUuid) {
        Campaign campaign = Optional.ofNullable(
                campaignRepository.findJoinedCampaignByUuidAndUserId(campaignUuid, currentAuthUser.getCurrentAuthUser().getId()))
                .orElseThrow(() -> new CampaignNotFoundException("Campaign not found."));

        return campaignDTOMapper.apply(campaign);
    }

    public CampaignDTO findCreatedCampaignByUuid(UUID campaignUuid) {
        Campaign campaign = Optional.ofNullable(
                        campaignRepository.findCreatedCampaignByUuidAndUserId(campaignUuid, currentAuthUser.getCurrentAuthUser().getId()))
                .orElseThrow(() -> new CampaignNotFoundException("Campaign not found."));

        return campaignDTOMapper.apply(campaign);
    }

    // TODO - Check all joined players on specific campaign

}
