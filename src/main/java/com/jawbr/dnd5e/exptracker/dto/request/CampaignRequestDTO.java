package com.jawbr.dnd5e.exptracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CampaignRequestDTO(
        @NotBlank(message = "Name cannot be empty!") String name,
        String description
) {
}
