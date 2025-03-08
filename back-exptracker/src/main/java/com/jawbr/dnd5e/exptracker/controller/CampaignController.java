package com.jawbr.dnd5e.exptracker.controller;

import com.jawbr.dnd5e.exptracker.dto.request.CampaignRequestDTO;
import com.jawbr.dnd5e.exptracker.dto.response.CampaignDTO;
import com.jawbr.dnd5e.exptracker.dto.response.CampaignPlayersDTO;
import com.jawbr.dnd5e.exptracker.dto.response.GenericMessageResponseDTO;
import com.jawbr.dnd5e.exptracker.dto.response.InviteCodeDTO;
import com.jawbr.dnd5e.exptracker.service.CampaignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Campaign Controller", description = "Campaign endpoints")
@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @Operation(summary = "Find all joined campaigns",
            security = {@SecurityRequirement(name = "Bearer ")})
    @GetMapping("/me/joined")
    public Page<CampaignDTO> findJoinedCampaigns(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String sortBy
    )
    {
        return campaignService.findJoinedCampaigns(page, pageSize, sortBy);
    }

    @Operation(summary = "Find all created campaigns",
            security = {@SecurityRequirement(name = "Bearer ")})
    @GetMapping("/me/created")
    public Page<CampaignDTO> findCreatedCampaigns(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String sortBy
    )
    {
        return campaignService.findCreatedCampaigns(page, pageSize, sortBy);
    }

    @Operation(summary = "Find joined campaign by UUID",
            security = {@SecurityRequirement(name = "Bearer ")})
    @GetMapping("/me/joined/{campaignUuid}")
    public CampaignDTO findJoinedCampaign(@PathVariable UUID campaignUuid) {
        return campaignService.findJoinedCampaignByUuid(campaignUuid);
    }

    @Operation(summary = "Find created campaign by UUID",
            security = {@SecurityRequirement(name = "Bearer ")})
    @GetMapping("/me/created/{campaignUuid}")
    public CampaignDTO findCreatedCampaign(@PathVariable UUID campaignUuid) {
        return campaignService.findCreatedCampaignByUuid(campaignUuid);
    }

    @Operation(summary = "Find all joined players on a campaign by UUID",
            security = {@SecurityRequirement(name = "Bearer ")})
    @GetMapping("/me/joined/players/{campaignUuid}")
    public List<CampaignPlayersDTO> findAllJoinedPlayersOnCampaign(@PathVariable UUID campaignUuid) {
        return campaignService.findAllJoinedPlayersOnCampaign(campaignUuid);
    }

    @Operation(summary = "Create a campaign",
            security = {@SecurityRequirement(name = "Bearer ")})
    @PostMapping("/me")
    public CampaignDTO createCampaign(@Valid @RequestBody CampaignRequestDTO campaignRequestDTO) {
        return campaignService.createCampaign(campaignRequestDTO);
    }

    @Operation(summary = "Owner of campaign generate invite code by Campaign UUID",
            security = {@SecurityRequirement(name = "Bearer ")})
    @PostMapping("/me/generate-invite/{campaignUuid}")
    public InviteCodeDTO generateInviteCodeForCampaign(@PathVariable UUID campaignUuid) {
        return campaignService.generateInviteCodeForCampaign(campaignUuid);
    }

    @Operation(summary = "User join campaign using Invite Code",
            security = {@SecurityRequirement(name = "Bearer ")})
    @PostMapping("/me/join/invite-code/{inviteCode}")
    public CampaignDTO joinCampaign(@PathVariable String inviteCode) {
        return campaignService.joinCampaign(inviteCode);
    }

    @Operation(summary = "User leave campaign by UUID",
            security = {@SecurityRequirement(name = "Bearer ")})
    @PutMapping("/me/leave/{campaignUuid}")
    public GenericMessageResponseDTO leaveCampaign(
            @PathVariable UUID campaignUuid,
            @RequestParam(defaultValue = "false") boolean isConfirmed)
    {
        return campaignService.leaveCampaign(campaignUuid, isConfirmed);
    }

    @Operation(summary = "Owner delete created campaign by UUID",
            security = {@SecurityRequirement(name = "Bearer ")})
    @DeleteMapping("/me/{campaignUuid}")
    public ResponseEntity<Void> deleteCampaign(
            @PathVariable UUID campaignUuid,
            @RequestParam(defaultValue = "false") boolean isConfirmed)
    {
        campaignService.deleteCampaign(campaignUuid, isConfirmed);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Owner remove a player from campaign by Campaign and User UUID",
            security = {@SecurityRequirement(name = "Bearer ")})
    @PutMapping("/me/remove-player")
    public ResponseEntity<Void> removePlayerFromCampaign(
            @RequestParam(defaultValue = "false") boolean isConfirmed,
            @RequestParam UUID campaignUuid,
            @RequestParam UUID userUuid)
    {
        campaignService.removePlayerFromCampaign(campaignUuid, userUuid, isConfirmed);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Owner update campaign using UUID",
            security = {@SecurityRequirement(name = "Bearer ")})
    @PatchMapping("/me/{campaignUuid}")
    public CampaignDTO updateCampaign(
            @RequestBody CampaignRequestDTO campaignRequestDTO,
            @PathVariable UUID campaignUuid)
    {
        return campaignService.updateCampaign(campaignRequestDTO, campaignUuid);
    }
}
