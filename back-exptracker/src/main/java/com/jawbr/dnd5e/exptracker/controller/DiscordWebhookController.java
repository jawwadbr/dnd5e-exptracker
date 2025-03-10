package com.jawbr.dnd5e.exptracker.controller;

import com.jawbr.dnd5e.exptracker.dto.response.GenericMessageResponseDTO;
import com.jawbr.dnd5e.exptracker.service.DiscordWebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Discord Integration", description = "Endpoint for Discord Integration")
@RestController
@RequestMapping("/api/v1/integrations/discord/webhook")
public class DiscordWebhookController {

    private final DiscordWebhookService discordWebhookService;

    public DiscordWebhookController(DiscordWebhookService discordWebhookService) {
        this.discordWebhookService = discordWebhookService;
    }

    @Operation(summary = "Send characters information to discord via webhook",
            security = {@SecurityRequirement(name = "Bearer ")})
    @PostMapping("/send/{campaignUuid}")
    public GenericMessageResponseDTO sendToDiscord(@PathVariable UUID campaignUuid) {
        return discordWebhookService.sendCharacterUpdate(campaignUuid);
    }
}
