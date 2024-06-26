package com.jawbr.dnd5e.exptracker.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record UserDTO(
        String username,
        String email,
        String created_at,
        String role,
        Boolean is_active,
        String deletion_date,
        String public_uuid
) {
}
