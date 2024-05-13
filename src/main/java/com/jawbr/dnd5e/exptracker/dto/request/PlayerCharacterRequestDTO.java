package com.jawbr.dnd5e.exptracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PlayerCharacterRequestDTO(
        @NotBlank(message = "Name cannot be empty!") String name,
        @NotBlank(message = "Class cannot be empty!") String char_class,
        @NotBlank(message = "Race cannot be empty!") String char_race
) {
}
