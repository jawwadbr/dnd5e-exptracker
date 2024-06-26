package com.jawbr.dnd5e.exptracker.controller;

import com.jawbr.dnd5e.exptracker.dto.request.PlayerCharacterRequestDTO;
import com.jawbr.dnd5e.exptracker.dto.response.CampaignDTO;
import com.jawbr.dnd5e.exptracker.dto.response.PlayerCharacterDTO;
import com.jawbr.dnd5e.exptracker.service.PlayerCharacterService;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/api/player-characters")
public class PlayerCharacterController {

    private final PlayerCharacterService playerCharacterService;

    public PlayerCharacterController(PlayerCharacterService playerCharacterService) {
        this.playerCharacterService = playerCharacterService;
    }

    @GetMapping("/me/campaign/{campaignUuid}")
    public List<PlayerCharacterDTO> findAllUserPlayerCharacterOnSingleCampaign(@PathVariable UUID campaignUuid) {
        return playerCharacterService.findAllUserPlayerCharacterOnSingleCampaign(campaignUuid);
    }

    @GetMapping("/me/{characterUuid}")
    public PlayerCharacterDTO findUserPlayerCharacterByUuid(@PathVariable UUID characterUuid) {
        return playerCharacterService.findUserPlayerCharacterByUuid(characterUuid);
    }

    @PostMapping("/me/create/{campaignUuid}")
    public PlayerCharacterDTO createPlayerCharacter(
            @Valid @RequestBody PlayerCharacterRequestDTO playerCharacterRequestDTO,
            @PathVariable UUID campaignUuid)
    {
        return playerCharacterService.createPlayerCharacter(campaignUuid, playerCharacterRequestDTO);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deletePlayerCharacter(
            @RequestParam(required = false) UUID campaignUuid,
            @RequestParam UUID characterUuid,
            @RequestParam(defaultValue = "false") boolean isConfirmed)
    {
        playerCharacterService.deletePlayerCharacter(campaignUuid, characterUuid, isConfirmed);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/activation")
    public ResponseEntity<Void> characterActivation(
            @RequestParam(required = false) UUID campaignUuid,
            @RequestParam UUID characterUuid)
    {
        playerCharacterService.characterActivation(campaignUuid, characterUuid);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me")
    public PlayerCharacterDTO updatePlayerCharacter(
            @RequestParam(required = false) UUID campaignUuid,
            @RequestParam UUID characterUuid,
            @RequestBody PlayerCharacterRequestDTO playerCharacterRequestDTO)
    {
        return playerCharacterService.updatePlayerCharacter(playerCharacterRequestDTO, characterUuid, campaignUuid);
    }

    @PutMapping("/me/xp")
    public CampaignDTO adjustExperience(
            @RequestParam UUID campaignUuid,
            @RequestParam(required = false) UUID characterUuid,
            @RequestParam int xp_value,
            @RequestParam(defaultValue = "false") boolean include_inactive
    ) {
        return playerCharacterService.adjustExperience(xp_value, characterUuid, campaignUuid, include_inactive);
    }
}
