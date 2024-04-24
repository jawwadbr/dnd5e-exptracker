package com.jawbr.dnd5e.exptracker.dto.response;

import lombok.Builder;

@Builder
public record UserCampaignsDTO(
        String name,
        String description,
        String creator_username,
        String creator_public_uuid
) {
}
