package com.jawbr.dnd5e.exptracker.dto.mapper;

import com.jawbr.dnd5e.exptracker.dto.response.CampaignDTO;
import com.jawbr.dnd5e.exptracker.dto.response.CampaignPlayersDTO;
import com.jawbr.dnd5e.exptracker.dto.response.InviteCodeDTO;
import com.jawbr.dnd5e.exptracker.dto.response.PlayerCharacterDTO;
import com.jawbr.dnd5e.exptracker.dto.response.UserDTO;
import com.jawbr.dnd5e.exptracker.entity.Campaign;
import com.jawbr.dnd5e.exptracker.entity.InviteCode;
import com.jawbr.dnd5e.exptracker.entity.PlayerCharacter;
import com.jawbr.dnd5e.exptracker.entity.User;
import com.jawbr.dnd5e.exptracker.util.ExperiencePointsTable;
import com.jawbr.dnd5e.exptracker.util.Mapper;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Mapper
public class CampaignDTOMapper implements Function<Campaign, CampaignDTO> {

    @Override
    public CampaignDTO apply(Campaign campaign) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss a");

        List<InviteCodeDTO> inviteCodes = new ArrayList<>();
        if(campaign.getInviteCodes() != null) {
            for(InviteCode code : campaign.getInviteCodes()) {
                inviteCodes.add(InviteCodeDTO.builder()
                        .code(code.getCode())
                        .expiry_date(code.getExpiryDate().format(formatter))
                        .build());
            }
        }

        List<PlayerCharacterDTO> activePlayerCharacterReferenceDTOS = new ArrayList<>();
        List<PlayerCharacterDTO> inactivePlayerCharacterReferenceDTOS = new ArrayList<>();
        if(campaign.getPlayers() != null) {
            List<PlayerCharacter> playerCharacterList = campaign.getPlayerCharacters();
            if(playerCharacterList != null) {
                for(PlayerCharacter pc : playerCharacterList) {
                    if(pc.isActive()) {
                        activePlayerCharacterReferenceDTOS.add(PlayerCharacterDTO.builder()
                                .name(pc.getCharacterName())
                                .char_class((pc.getPlayerCharClass() != null) ? pc.getPlayerCharClass().getName() : "null")
                                .char_race((pc.getPlayerRace() != null) ? pc.getPlayerRace().getName() : "null")
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
                                .char_class((pc.getPlayerCharClass() != null) ? pc.getPlayerCharClass().getName() : "null")
                                .char_race((pc.getPlayerRace() != null) ? pc.getPlayerRace().getName() : "null")
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
        }

        return CampaignDTO.builder()
                .name(campaign.getName())
                .description(campaign.getDescription())
                .public_campaign_uuid(campaign.getUuid().toString())
                .created_at(campaign.getCreatedAt() != null ? campaign.getCreatedAt().format(formatter) + " UTC" : null)
                .invite_codes(inviteCodes)
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss a");

        List<InviteCodeDTO> inviteCodes = new ArrayList<>();
        if(campaign.getInviteCodes() != null) {
            List<InviteCode> inviteCodeList = campaign.getInviteCodes();
            for(InviteCode code : inviteCodeList) {
                inviteCodes.add(InviteCodeDTO.builder()
                        .code(code.getCode())
                        .expiry_date(code.getExpiryDate().format(formatter) + " UTC")
                        .build());
            }
        }

        return CampaignDTO.builder()
                .name(campaign.getName())
                .description(campaign.getDescription())
                .public_campaign_uuid(campaign.getUuid().toString())
                .invite_codes(inviteCodes)
                .build();
    }

    public CampaignPlayersDTO mapAllJoinedPlayersOnCampaignToDTO(User user, UUID campaignUuid) {

        boolean isCreator = false;
        List<Campaign> createdCampaigns = user.getCreatedCampaigns();
        if(createdCampaigns != null) {
            for(Campaign campaign : createdCampaigns) {
                if(campaign.getCreator().equals(user) && campaign.getUuid().equals(campaignUuid)) {
                    isCreator = true;
                    break;
                }
            }
        }

        return CampaignPlayersDTO.builder()
                .username(user.getUsername())
                .is_creator(isCreator)
                .player_public_uuid(user.getUuid().toString())
                .build();
    }
}
