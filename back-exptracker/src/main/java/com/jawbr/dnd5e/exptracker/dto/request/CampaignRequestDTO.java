package com.jawbr.dnd5e.exptracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CampaignRequestDTO(
        @NotBlank(message = "Name cannot be empty!") String name,
        String description,
        @Pattern(
                regexp = "^https://discord.com/api/webhooks/\\d+/[A-Za-z0-9-_]+$",
                message = "Invalid Discord Webhook URL!")
        String discord_webhook
) {
}
