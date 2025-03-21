package com.jawbr.dnd5e.exptracker.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record CampaignDTO(
        String name,
        String description,
        String public_campaign_uuid,
        String created_at,
        List<InviteCodeDTO> invite_codes,
        List<PlayerCharacterDTO> players_characters,
        List<PlayerCharacterDTO> inactive_players_characters,
        Boolean is_webhook_configured,
        CampaignCreatorDTO campaign_creator
) {
    @Builder
    public record CampaignCreatorDTO(
            String username,
            String public_uuid
    ) {
    }
}
