package com.jawbr.dnd5e.exptracker.dto.response;

import lombok.Builder;

@Builder
public record TokenDTO(
        String token,
        long expireIn,
        UserDetailDTO user
) {
    @Builder
    public record UserDetailDTO(
            String email
    ) {
    }
}
