package com.jawbr.dnd5e.exptracker.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserRequestDTO(
        @NotBlank(message = "Name cannot be empty!")
        @Size(min = 3, message = "Name must have at least 3 characters!")
        String username,
        @NotBlank(message = "Email cannot be empty!")
        @Email(message = "Invalid email format!") String email,
        @NotBlank(message = "Password cannot be empty!")
        @Size(min = 6, message = "Password must have at least 6 characters!")
        String password
) {
}
