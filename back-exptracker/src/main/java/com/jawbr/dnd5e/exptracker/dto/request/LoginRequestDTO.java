package com.jawbr.dnd5e.exptracker.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @Email(message = "Invalid email format!") String email,
        @NotBlank(message = "Password cannot be empty!") String password
) {
}
