package com.jawbr.dnd5e.exptracker.dto.mapper;

import com.jawbr.dnd5e.exptracker.dto.response.CampaignDTO;
import com.jawbr.dnd5e.exptracker.dto.response.PlayerCharacterDTO;
import com.jawbr.dnd5e.exptracker.entity.PlayerCharacter;
import com.jawbr.dnd5e.exptracker.util.ExperiencePointsTable;
import com.jawbr.dnd5e.exptracker.util.Mapper;

import java.util.function.Function;

@Mapper
public class PlayerCharacterDTOMapper implements Function<PlayerCharacter, PlayerCharacterDTO> {

    @Override
    public PlayerCharacterDTO apply(PlayerCharacter playerCharacter) {
        return PlayerCharacterDTO.builder()
                .name(playerCharacter.getCharacterName())
                .char_class((playerCharacter.getPlayerCharClass() != null) ? playerCharacter.getPlayerCharClass().getName() : "null")
                .char_race((playerCharacter.getPlayerRace() != null) ? playerCharacter.getPlayerRace().getName() : "null")
                .experience_points(playerCharacter.getExperiencePoints())
                .level(ExperiencePointsTable.getLevelFromXP(playerCharacter.getExperiencePoints()))
                .campaign(CampaignDTO.builder()
                        .name(playerCharacter.getCampaign().getName())
                        .public_campaign_uuid(playerCharacter.getCampaign().getUuid().toString())
                        .campaign_creator(CampaignDTO.CampaignCreatorDTO.builder()
                                .username(playerCharacter.getCampaign().getCreator().getUsername())
                                .public_uuid(playerCharacter.getCampaign().getCreator().getUuid().toString())
                                .build())
                        .build())
                .player_character_public_uuid(playerCharacter.getUuid().toString())
                .build();
    }
}
