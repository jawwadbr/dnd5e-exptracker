package com.jawbr.dnd5e.exptracker.service;

import com.jawbr.dnd5e.exptracker.dto.request.discord.DiscordWebhookPayload;
import com.jawbr.dnd5e.exptracker.dto.request.discord.DiscordWebhookPayload.Embed;
import com.jawbr.dnd5e.exptracker.dto.request.discord.DiscordWebhookPayload.Embed.Field;
import com.jawbr.dnd5e.exptracker.dto.response.GenericMessageResponseDTO;
import com.jawbr.dnd5e.exptracker.entity.Campaign;
import com.jawbr.dnd5e.exptracker.entity.PlayerCharacter;
import com.jawbr.dnd5e.exptracker.entity.User;
import com.jawbr.dnd5e.exptracker.exception.CampaignNotFoundException;
import com.jawbr.dnd5e.exptracker.exception.DiscordWebhookBadRequestException;
import com.jawbr.dnd5e.exptracker.exception.PlayerCharacterNotFoundException;
import com.jawbr.dnd5e.exptracker.repository.CampaignRepository;
import com.jawbr.dnd5e.exptracker.util.ExperiencePointsTable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DiscordWebhookService {

    private static final String GITHUB_REP_LINK = "[:link: Click here to check the source code for D&D 5e XP Tracker API](https://github.com/jawwadbr/dnd5e-exptracker)";
    private static final String DISCORD_API_URL = "https://discord.com/api/webhooks/";

    private final WebhookEncryptionService webhookEncryptionService;
    private final CampaignRepository campaignRepository;
    private final CurrentAuthUserService currentAuthUserService;
    private final RestTemplate restTemplate;

    public DiscordWebhookService(WebhookEncryptionService webhookEncryptionService,
                                 CampaignRepository campaignRepository,
                                 CurrentAuthUserService currentAuthUserService,
                                 RestTemplate restTemplate) {
        this.webhookEncryptionService = webhookEncryptionService;
        this.campaignRepository = campaignRepository;
        this.currentAuthUserService = currentAuthUserService;
        this.restTemplate = restTemplate;
    }

    public GenericMessageResponseDTO sendCharacterUpdate(UUID campaignUuid) {
        Campaign campaign = fetchCampaign(campaignUuid);
        List<PlayerCharacter> characters = fetchCharacters(campaign);
        String webhookUrl = buildWebhookUrl(campaign);

        Embed embed = buildEmbed(campaign, characters);
        DiscordWebhookPayload payload = buildPayload(embed);

        sendToDiscord(webhookUrl, payload);

        return GenericMessageResponseDTO.builder()
                .message("Successfully sent data to Discord via webhook.")
                .build();
    }

    private Campaign fetchCampaign(UUID campaignUuid) {
        User currentUser = currentAuthUserService.getCurrentAuthUser();
        return Optional.ofNullable(
                        campaignRepository.findCreatedCampaignByUuidAndUserId(campaignUuid, currentUser.getId()))
                .orElseThrow(() -> new CampaignNotFoundException(String.format("Campaign with UUID '%s' not found.", campaignUuid)));
    }

    private List<PlayerCharacter> fetchCharacters(Campaign campaign) {
        List<PlayerCharacter> characters = campaign.getPlayerCharacters();
        if (characters.isEmpty()) {
            throw new PlayerCharacterNotFoundException("No characters found in campaign. Can't send the details to Discord.");
        }
        return characters;
    }

    private String buildWebhookUrl(Campaign campaign) {
        if (campaign.getWebhookUrl() == null || campaign.getWebhookUrl().isEmpty()) {
            throw new DiscordWebhookBadRequestException("Webhook is null or invalid.");
        }
        String decryptedWebhook = webhookEncryptionService.decrypt(campaign.getWebhookUrl());
        return DISCORD_API_URL + decryptedWebhook;
    }

    private Embed buildEmbed(Campaign campaign, List<PlayerCharacter> characters) {
        // Build embed to add title and create fields for characters
        // Obs: max embeds in a payload is 10
        Embed embed = Embed.builder().build();
        embed.setTitle("\uD83D\uDCDC " + campaign.getName() + " \uD83D\uDCDC");
        embed.setDescription("Characters details:");
        embed.setColor(16729856);

        // Obs: max fields in an embed is 25
        List<Field> fields = characters.stream()
                .filter(PlayerCharacter::isActive)
                .sorted(Comparator.comparingInt(PlayerCharacter::getExperiencePoints).reversed())
                .map(this::buildField)
                .collect(Collectors.toList());

        fields.add(Field.builder()
                .name(":globe_with_meridians: Github Repository")
                .value(GITHUB_REP_LINK)
                .inline(false)
                .build());

        embed.setFields(fields);
        embed.setAuthor(Embed.Author.builder()
                .name("Dungeon Master: " + campaign.getCreator().getUsername())
                .icon_url("https://cdn-icons-png.flaticon.com/512/6545/6545894.png")
                .build());

        return embed;
    }

    private Field buildField(PlayerCharacter character) {
        return Field.builder()
                .name(character.getCharacterName())
                .value(formatCharacterDetail(character))
                .inline(false)
                .build();
    }

    private DiscordWebhookPayload buildPayload(Embed embed) {
        DiscordWebhookPayload payload = DiscordWebhookPayload.builder().build();
        payload.setUsername("D&D 5E: XP Tracker");
        payload.setAvatar_url("https://img.icons8.com/?size=512&id=104704&format=png");
        payload.setEmbeds(List.of(embed));
        return payload;
    }

    private void sendToDiscord(String webhookUrl, DiscordWebhookPayload payload) {
        try {
            restTemplate.postForObject(webhookUrl, payload, String.class);
        } catch (Exception e) {
            throw new DiscordWebhookBadRequestException("Something went wrong. Check Webhook URL and try again later.");
        }
    }

    private String formatCharacterDetail(PlayerCharacter character) {
        String classLabel = "Class";
        String raceLabel = "Race";
        String xpLabel = "XP";
        String levelLabel = "Level";
        String playerLabel = "Player";

        String level = String.valueOf((ExperiencePointsTable.getLevelFromXP(character.getExperiencePoints())));

        return String.format("%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s",
                classLabel, character.getPlayerCharClass().getName(),
                raceLabel, character.getPlayerRace().getName(),
                xpLabel, character.getExperiencePoints(),
                levelLabel, level.replaceFirst("^LEVEL_", ""),
                playerLabel, character.getPlayer().getUsername());
    }
}