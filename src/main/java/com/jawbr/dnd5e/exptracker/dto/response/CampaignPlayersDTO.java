package com.jawbr.dnd5e.exptracker.dto.response;

import lombok.Builder;

@Builder
public record CampaignPlayersDTO(
        String username,
        String player_public_uuid
) {
}
