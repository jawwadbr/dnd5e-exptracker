package com.jawbr.dnd5e.exptracker.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RaceRequestDTO(
        @NotBlank(message = "Name cannot be empty!") String name
) {
}
