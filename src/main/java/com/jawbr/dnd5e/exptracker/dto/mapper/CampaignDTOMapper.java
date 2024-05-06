package com.jawbr.dnd5e.exptracker.dto.mapper;

import com.jawbr.dnd5e.exptracker.dto.response.CampaignDTO;
import com.jawbr.dnd5e.exptracker.dto.response.CampaignPlayersDTO;
import com.jawbr.dnd5e.exptracker.dto.response.PlayerCharacterDTO;
import com.jawbr.dnd5e.exptracker.dto.response.UserDTO;
import com.jawbr.dnd5e.exptracker.entity.Campaign;
import com.jawbr.dnd5e.exptracker.entity.PlayerCharacter;
import com.jawbr.dnd5e.exptracker.entity.User;
import com.jawbr.dnd5e.exptracker.util.ExperiencePointsTable;
import com.jawbr.dnd5e.exptracker.util.Mapper;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Mapper
public class CampaignDTOMapper implements Function<Campaign, CampaignDTO> {

    @Override
    public CampaignDTO apply(Campaign campaign) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss a");

        List<PlayerCharacterDTO> activePlayerCharacterReferenceDTOS = new ArrayList<>();
        List<PlayerCharacterDTO> inactivePlayerCharacterReferenceDTOS = new ArrayList<>();
        if(campaign.getPlayers() != null) {
            for(PlayerCharacter pc : campaign.getPlayerCharacters()) {
                if(pc.isActive()) {
                    activePlayerCharacterReferenceDTOS.add(PlayerCharacterDTO.builder()
                            .name(pc.getCharacterName())
                            .char_class(pc.getPlayerCharClass().getName())
                            .char_race(pc.getPlayerRace().getName())
                            .experience_points(pc.getExperiencePoints())
                            .level(ExperiencePointsTable.getLevelFromXP(pc.getExperiencePoints()))
                            .player(UserDTO.builder()
                                    .username(pc.getPlayer().getUsername())
                                    .public_uuid(pc.getPlayer().getUuid().toString())
                                    .build())
                            .player_character_public_uuid(pc.getUuid().toString())
                            .active(pc.isActive())
                            .build());
                }
                else {
                    inactivePlayerCharacterReferenceDTOS.add(PlayerCharacterDTO.builder()
                            .name(pc.getCharacterName())
                            .char_class(pc.getPlayerCharClass().getName())
                            .char_race(pc.getPlayerRace().getName())
                            .experience_points(pc.getExperiencePoints())
                            .level(ExperiencePointsTable.getLevelFromXP(pc.getExperiencePoints()))
                            .player(UserDTO.builder()
                                    .username(pc.getPlayer().getUsername())
                                    .public_uuid(pc.getPlayer().getUuid().toString())
                                    .build())
                            .player_character_public_uuid(pc.getUuid().toString())
                            .active(pc.isActive())
                            .build());
                }
            }
        }

        return CampaignDTO.builder()
                .name(campaign.getName())
                .description(campaign.getDescription())
                .public_campaign_uuid(campaign.getUuid().toString())
                .created_at(campaign.getCreatedAt() != null ? campaign.getCreatedAt().format(formatter) + " UTC" : null)
                .campaign_creator(CampaignDTO.CampaignCreatorDTO.builder()
                        .username(campaign.getCreator().getUsername())
                        .public_uuid(campaign.getCreator().getUuid().toString())
                        .build())
                .players_characters(activePlayerCharacterReferenceDTOS)
                .inactive_players_characters(inactivePlayerCharacterReferenceDTOS)
                .build();
    }

    public CampaignDTO mapJoinedCampaignsToDTO(Campaign campaign) {
        return CampaignDTO.builder()
                .name(campaign.getName())
                .description(campaign.getDescription())
                .public_campaign_uuid(campaign.getUuid().toString())
                .campaign_creator(CampaignDTO.CampaignCreatorDTO.builder()
                        .username(campaign.getCreator().getUsername())
                        .public_uuid(campaign.getCreator().getUuid().toString())
                        .build())
                .build();
    }

    public CampaignDTO mapCreatedCampaignsToDTO(Campaign campaign) {
        return CampaignDTO.builder()
                .name(campaign.getName())
                .description(campaign.getDescription())
                .public_campaign_uuid(campaign.getUuid().toString())
                .build();
    }

    public CampaignPlayersDTO mapAllJoinedPlayersOnCampaignToDTO(User user) {
        return CampaignPlayersDTO.builder()
                .username(user.getUsername())
                .player_public_uuid(user.getUuid().toString())
                .build();
    }
}
