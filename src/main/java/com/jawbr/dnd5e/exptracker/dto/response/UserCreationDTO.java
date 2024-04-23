package com.jawbr.dnd5e.exptracker.dto.response;

import lombok.Builder;

@Builder
public record UserCreationDTO(
        String username,
        String email,
        String creation_time,
        String message
) {
}
