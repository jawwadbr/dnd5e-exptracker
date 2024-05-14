package com.jawbr.dnd5e.exptracker.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserRequestDTO(
        @NotBlank(message = "Name cannot be empty!") String username,
        @NotBlank(message = "Email cannot be empty!")
        @Email(message = "Invalid email format!") String email,
        @NotBlank(message = "Password cannot be empty!") String password
) {
}
