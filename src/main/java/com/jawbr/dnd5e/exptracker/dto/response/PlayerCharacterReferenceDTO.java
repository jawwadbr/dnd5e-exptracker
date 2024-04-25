package com.jawbr.dnd5e.exptracker.dto.response;

import com.jawbr.dnd5e.exptracker.util.ExperiencePointsTable;
import lombok.Builder;

@Builder
public record PlayerCharacterReferenceDTO(
        String name,
        String char_class,
        String char_race,
        int experience_points,
        ExperiencePointsTable level,
        UserDTO player,
        String player_character_public_uuid
) {
}
