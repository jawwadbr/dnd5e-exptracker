package com.jawbr.dnd5e.exptracker.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ClassRequestDTO(
        @NotBlank(message = "Name cannot be empty!") String name
) {
}
