package com.jawbr.dnd5e.exptracker.dto.response;

import lombok.Builder;

@Builder
public record ClassDTO(
        String name,
        String class_uuid
) {
}
