package com.jawbr.dnd5e.exptracker.dto.response;

import lombok.Builder;

@Builder
public record UserDTO(
        String username,
        String email,
        String created_at,
        String role
) {
}
