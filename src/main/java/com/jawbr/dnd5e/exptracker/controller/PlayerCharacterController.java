package com.jawbr.dnd5e.exptracker.controller;

import com.jawbr.dnd5e.exptracker.dto.response.PlayerCharacterDTO;
import com.jawbr.dnd5e.exptracker.service.PlayerCharacterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
