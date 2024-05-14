package com.jawbr.dnd5e.exptracker.dto.response;

import lombok.Builder;

@Builder
public record RaceDTO(
        String name,
        String race_uuid
) {
}
