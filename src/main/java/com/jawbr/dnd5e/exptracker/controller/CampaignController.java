package com.jawbr.dnd5e.exptracker.controller;

import com.jawbr.dnd5e.exptracker.dto.response.CampaignDTO;
import com.jawbr.dnd5e.exptracker.dto.response.CampaignPlayersDTO;
import com.jawbr.dnd5e.exptracker.service.CampaignService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping("/me/joined")
    public Page<CampaignDTO> findJoinedCampaigns(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String sortBy
    )
    {
        return campaignService.findJoinedCampaigns(page, pageSize, sortBy);
    }

    @GetMapping("/me/created")
    public Page<CampaignDTO> findCreatedCampaigns(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String sortBy
    )
    {
        return campaignService.findCreatedCampaigns(page, pageSize, sortBy);
    }

    @GetMapping("/me/joined/{campaignUuid}")
    public CampaignDTO findJoinedCampaign(@PathVariable UUID campaignUuid) {
        return campaignService.findJoinedCampaignByUuid(campaignUuid);
    }

    @GetMapping("/me/created/{campaignUuid}")
    public CampaignDTO findCreatedCampaign(@PathVariable UUID campaignUuid) {
        return campaignService.findCreatedCampaignByUuid(campaignUuid);
    }

    @GetMapping("/me/joined/players/{campaignUuid}")
    public List<CampaignPlayersDTO> findAllJoinedPlayersOnCampaign(@PathVariable UUID campaignUuid) {
        return campaignService.findAllJoinedPlayersOnCampaign(campaignUuid);
    }
}
