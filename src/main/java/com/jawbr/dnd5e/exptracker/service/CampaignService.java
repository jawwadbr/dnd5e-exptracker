package com.jawbr.dnd5e.exptracker.service;

import com.jawbr.dnd5e.exptracker.dto.mapper.CampaignDTOMapper;
import com.jawbr.dnd5e.exptracker.dto.mapper.InviteCodeDTOMapper;
import com.jawbr.dnd5e.exptracker.dto.request.CampaignRequestDTO;
import com.jawbr.dnd5e.exptracker.dto.response.CampaignDTO;
import com.jawbr.dnd5e.exptracker.dto.response.CampaignPlayersDTO;
import com.jawbr.dnd5e.exptracker.dto.response.GenericMessageResponseDTO;
import com.jawbr.dnd5e.exptracker.dto.response.InviteCodeDTO;
import com.jawbr.dnd5e.exptracker.entity.Campaign;
import com.jawbr.dnd5e.exptracker.entity.InviteCode;
import com.jawbr.dnd5e.exptracker.entity.User;
import com.jawbr.dnd5e.exptracker.exception.CampaignNotFoundException;
import com.jawbr.dnd5e.exptracker.exception.IllegalParameterException;
import com.jawbr.dnd5e.exptracker.exception.InviteCodeNotFoundException;
import com.jawbr.dnd5e.exptracker.repository.CampaignRepository;
import com.jawbr.dnd5e.exptracker.repository.InviteCodeRepository;
import com.jawbr.dnd5e.exptracker.util.InviteCodeGenerator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.jawbr.dnd5e.exptracker.util.CheckRequestConfirmation.checkConfirmation;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final InviteCodeRepository inviteCodeRepository;
    private final CampaignDTOMapper campaignDTOMapper;
    private final InviteCodeDTOMapper inviteCodeDTOMapper;
    private final CurrentAuthUserService currentAuthUser;

    public CampaignService(CampaignRepository campaignRepository,
                           InviteCodeRepository inviteCodeRepository, CampaignDTOMapper campaignDTOMapper,
                           InviteCodeDTOMapper inviteCodeDTOMapper, CurrentAuthUserService currentAuthUser)
    {
        this.campaignRepository = campaignRepository;
        this.inviteCodeRepository = inviteCodeRepository;
        this.campaignDTOMapper = campaignDTOMapper;
        this.inviteCodeDTOMapper = inviteCodeDTOMapper;
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

    public List<CampaignPlayersDTO> findAllJoinedPlayersOnCampaign(UUID campaignUuid) {
        List<User> users = Optional.ofNullable(campaignRepository.findAllJoinedPlayersOnCampaign(campaignUuid, currentAuthUser.getCurrentAuthUser().getId()))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new CampaignNotFoundException("Campaign or players not found, or user not a participant."));

        return users.stream().map(user -> campaignDTOMapper.mapAllJoinedPlayersOnCampaignToDTO(user, campaignUuid)).toList();
    }

    /*
     * User create campaign
     * TODO - User update campaign
     * TODO - User delete campaign
     * User owner of campaign create invite code
     * User join campaign using invite code
     * User leave campaign
     * TODO - User owner of campaign give all players XP (Can include inactive players if wanted)
     * TODO - User owner of campaign give XP to a single player using UUID (including inactive players)
     */

    public GenericMessageResponseDTO leaveCampaign(UUID campaignUuid, boolean isConfirmed) {
        checkConfirmation(isConfirmed);

        User user = currentAuthUser.getCurrentAuthUser();

        Campaign campaign = Optional.ofNullable(
                        campaignRepository.findJoinedCampaignByUuidAndUserId(campaignUuid, user.getId()))
                .orElseThrow(() -> new CampaignNotFoundException("Campaign not found."));

        if(campaign.getCreator().equals(user)) {
            campaignRepository.delete(campaign);
            return GenericMessageResponseDTO.builder()
                    .message("You have successfully left and deleted the campaign.")
                    .build();
        }

        campaign.getPlayers().remove(user);

        campaignRepository.save(campaign);

        return GenericMessageResponseDTO.builder()
                .message("You have successfully left the campaign.")
                .build();
    }

    public CampaignDTO joinCampaign(String inviteCode) {

        InviteCode invite = Optional.ofNullable(inviteCodeRepository.findByCode(inviteCode))
                .orElseThrow(() -> new InviteCodeNotFoundException("Invalid invite code."));

        User user = currentAuthUser.getCurrentAuthUser();

        Campaign campaign = invite.getCampaign();

        campaign.getPlayers().add(user);

        campaignRepository.save(campaign);

        return campaignDTOMapper.apply(campaign);
    }

    public CampaignDTO createCampaign(CampaignRequestDTO campaignRequestDTO) {
        User user = currentAuthUser.getCurrentAuthUser();
        Campaign campaign = Campaign.builder()
                .creator(user)
                .name(campaignRequestDTO.name())
                .description(StringUtils.hasText(campaignRequestDTO.description()) ? campaignRequestDTO.description() : "")
                .build();
        campaign.setPlayers(List.of(user));

        // Generate one invite code when creating a campaign
        InviteCode code = new InviteCode();
        code.setCampaign(campaign);

        // In case of duplicated invite code in the database
        regenerateCodeWhenDuplicated(code);

        campaign.setInviteCodes(List.of(code));

        // In case of duplicated UUID - Chances are extremely low but still checking
        boolean saved = false;
        while(!saved) {
            try {
                campaign = campaignRepository.save(campaign);
                saved = true;
            } catch(DataIntegrityViolationException exc) {
                campaign.setUuid(UUID.randomUUID());
            }
        }

        return campaignDTOMapper.apply(campaign);
    }

    public InviteCodeDTO generateInviteCodeForCampaign(UUID campaignUuid) {
        Campaign campaign = Optional.ofNullable(
                        campaignRepository.findCreatedCampaignByUuidAndUserId(campaignUuid, currentAuthUser.getCurrentAuthUser().getId()))
                .orElseThrow(() -> new CampaignNotFoundException("Campaign not found."));

        InviteCode code = new InviteCode();
        code.setCampaign(campaign);

        regenerateCodeWhenDuplicated(code);

        inviteCodeRepository.save(code);

        campaign.getInviteCodes().add(code);
        campaignRepository.save(campaign);

        return inviteCodeDTOMapper.apply(code);
    }

    // Util private methods

    private void regenerateCodeWhenDuplicated(InviteCode code) {
        boolean inviteCodeExists = inviteCodeRepository.existsByCode(code.getCode());
        while(inviteCodeExists) {
            code.setCode(InviteCodeGenerator.generateRandomCode(10));
            inviteCodeExists = inviteCodeRepository.existsByCode(code.getCode());
        }
    }

}
