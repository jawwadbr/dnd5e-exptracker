package com.jawbr.dnd5e.exptracker.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jawbr.dnd5e.exptracker.util.ExperiencePointsTable;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record PlayerCharacterDTO(
        boolean active,
        String name,
        String char_class,
        String char_race,
        int experience_points,
        ExperiencePointsTable level,
        UserDTO player,
        CampaignDTO campaign,
        String player_character_public_uuid
) {
}
