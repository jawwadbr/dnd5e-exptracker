package com.jawbr.dnd5e.exptracker.service;

import com.jawbr.dnd5e.exptracker.dto.mapper.PlayerCharacterDTOMapper;
import com.jawbr.dnd5e.exptracker.dto.request.PlayerCharacterRequestDTO;
import com.jawbr.dnd5e.exptracker.dto.response.PlayerCharacterDTO;
import com.jawbr.dnd5e.exptracker.entity.Campaign;
import com.jawbr.dnd5e.exptracker.entity.Class;
import com.jawbr.dnd5e.exptracker.entity.PlayerCharacter;
import com.jawbr.dnd5e.exptracker.entity.Race;
import com.jawbr.dnd5e.exptracker.entity.User;
import com.jawbr.dnd5e.exptracker.exception.CampaignNotFoundException;
import com.jawbr.dnd5e.exptracker.exception.ClassNotFoundException;
import com.jawbr.dnd5e.exptracker.exception.PlayerCharacterNotFoundException;
import com.jawbr.dnd5e.exptracker.exception.RaceNotFoundException;
import com.jawbr.dnd5e.exptracker.repository.ClassRepository;
import com.jawbr.dnd5e.exptracker.repository.PlayerCharacterRepository;
import com.jawbr.dnd5e.exptracker.repository.RaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.jawbr.dnd5e.exptracker.util.CheckRequestConfirmation.checkConfirmation;

@Service
public class PlayerCharacterService {

    private final PlayerCharacterRepository playerCharacterRepository;
    private final ClassRepository classRepository;
    private final RaceRepository raceRepository;
    private final CurrentAuthUserService currentAuthUserService;
    private final PlayerCharacterDTOMapper playerCharacterDTOMapper;
    private final CurrentAuthUserService currentAuthUser;

    public PlayerCharacterService(PlayerCharacterRepository characterRepository,
                                  ClassRepository classRepository,
                                  RaceRepository raceRepository,
                                  CurrentAuthUserService currentAuthUserService,
                                  PlayerCharacterDTOMapper playerCharacterDTOMapper,
                                  CurrentAuthUserService currentAuthUser)
    {
        this.playerCharacterRepository = characterRepository;
        this.classRepository = classRepository;
        this.raceRepository = raceRepository;
        this.currentAuthUserService = currentAuthUserService;
        this.playerCharacterDTOMapper = playerCharacterDTOMapper;
        this.currentAuthUser = currentAuthUser;
    }

    // User check all his player characters on a campaign that he is part
    public List<PlayerCharacterDTO> findAllUserPlayerCharacterOnSingleCampaign(UUID campaignUuid) {
        List<PlayerCharacter> playerCharacters = Optional.ofNullable(
                        playerCharacterRepository.findAllUserPlayerCharacterOnSingleCampaign(
                                campaignUuid,
                                currentAuthUser.getCurrentAuthUser().getId()))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new PlayerCharacterNotFoundException("No player character found."));

        return playerCharacters.stream().map(playerCharacterDTOMapper).toList();
    }

    // User check specific player character using UUID
    public PlayerCharacterDTO findUserPlayerCharacterByUuid(UUID characterUuid) {
        PlayerCharacter playerCharacter = Optional.ofNullable(
                        playerCharacterRepository.findUserPlayerCharacterByUuid(characterUuid,
                                currentAuthUser.getCurrentAuthUser().getId()))
                .orElseThrow(() -> new PlayerCharacterNotFoundException("No player character found."));

        return playerCharacterDTOMapper.apply(playerCharacter);
    }

    /*
     *  User create a character
     *  User can turn his character inactive (Owner can turn a character inactive too)
     *  User delete his character (Owner can delete characters too)
     *  User edit his character (Owner can edit character too)
     *  TODO - User owner of campaign give all players XP (Can include inactive players if wanted)
     *  TODO - User owner of campaign give XP to a single player using UUID (including inactive players)
     *  TODO - User owner of campaign remove XP from all players (Can include inactive players if wanted)
     *  TODO - User owner of campaign remove XP from a single player using UUID (Can include inactive players if wanted)
     */

    public PlayerCharacterDTO updatePlayerCharacter(PlayerCharacterRequestDTO playerCharacterRequestDTO, UUID characterUuid, UUID campaignUuid) {
        User user = currentAuthUserService.getCurrentAuthUser();

        boolean isOwner = false;
        if(campaignUuid != null) {
            isOwner = user.getJoinedCampaigns().stream()
                    .anyMatch(c -> c.getCreator().equals(user) && c.getUuid().equals(campaignUuid));
        }

        PlayerCharacter playerCharacterToBeUpdated;
        if(isOwner) {
            playerCharacterToBeUpdated = Optional.ofNullable(playerCharacterRepository.findPlayerCharacterByUuidInCampaign(
                            characterUuid,
                            campaignUuid,
                            user.getId()))
                    .orElseThrow(() -> new PlayerCharacterNotFoundException("No player character found."));
        }
        else {
            playerCharacterToBeUpdated = Optional.ofNullable(
                            playerCharacterRepository.findUserPlayerCharacterByUuid(characterUuid,
                                    user.getId()))
                    .orElseThrow(() -> new PlayerCharacterNotFoundException("No player character found."));
        }

        final String name = StringUtils.hasText(playerCharacterRequestDTO.name())
                ? playerCharacterRequestDTO.name() : playerCharacterToBeUpdated.getCharacterName();
        final String charClass = StringUtils.hasText(playerCharacterRequestDTO.char_class())
                ? playerCharacterRequestDTO.char_class().toLowerCase() : playerCharacterToBeUpdated.getPlayerCharClass().getName().toLowerCase();
        final String charRace = StringUtils.hasText(playerCharacterRequestDTO.char_race())
                ? playerCharacterRequestDTO.char_race().toLowerCase() : playerCharacterToBeUpdated.getPlayerRace().getName().toLowerCase();
        final int experiencePoints = Integer.parseInt((playerCharacterRequestDTO.experience_points() != 0)
                ? String.valueOf(playerCharacterRequestDTO.experience_points()) : String.valueOf(playerCharacterToBeUpdated.getExperiencePoints()));

        // Might change it to use UUID instead of name in the future
        Race char_race = Optional.ofNullable(raceRepository.findByName(charRace))
                .orElseThrow(() -> new RaceNotFoundException("Race not found."));
        Class char_class = Optional.ofNullable(classRepository.findByName(charClass))
                .orElseThrow(() -> new ClassNotFoundException("Class not found."));

        PlayerCharacter playerCharacterUpdated = PlayerCharacter.builder()
                .id(playerCharacterToBeUpdated.getId())
                .uuid(playerCharacterToBeUpdated.getUuid())
                .characterName(name)
                .playerRace(char_race)
                .playerCharClass(char_class)
                .player(playerCharacterToBeUpdated.getPlayer())
                .campaign(playerCharacterToBeUpdated.getCampaign())
                .experiencePoints(experiencePoints)
                .active(playerCharacterToBeUpdated.isActive())
                .build();

        playerCharacterRepository.save(playerCharacterUpdated);

        return playerCharacterDTOMapper.apply(playerCharacterUpdated);
    }

    public void characterActivation(UUID campaignUuid, UUID characterUuid) {
        User user = currentAuthUserService.getCurrentAuthUser();

        boolean isOwner = false;
        if(campaignUuid != null) {
            isOwner = user.getJoinedCampaigns().stream()
                    .anyMatch(c -> c.getCreator().equals(user) && c.getUuid().equals(campaignUuid));
        }

        PlayerCharacter playerCharacterToChangeActivation;
        if(isOwner) {
            playerCharacterToChangeActivation = Optional.ofNullable(playerCharacterRepository.findPlayerCharacterByUuidInCampaign(
                            characterUuid,
                            campaignUuid,
                            user.getId()))
                    .orElseThrow(() -> new PlayerCharacterNotFoundException("No player character found."));
        }
        else {
            playerCharacterToChangeActivation = Optional.ofNullable(
                            playerCharacterRepository.findUserPlayerCharacterByUuid(characterUuid,
                                    user.getId()))
                    .orElseThrow(() -> new PlayerCharacterNotFoundException("No player character found."));
        }

        playerCharacterToChangeActivation.setActive(!playerCharacterToChangeActivation.isActive());

        playerCharacterRepository.save(playerCharacterToChangeActivation);
    }

    public void deletePlayerCharacter(UUID campaignUuid, UUID characterUuid, boolean isConfirmed) {
        checkConfirmation(isConfirmed);

        User user = currentAuthUserService.getCurrentAuthUser();

        boolean isOwner = false;
        if(campaignUuid != null) {
            isOwner = user.getJoinedCampaigns().stream()
                    .anyMatch(c -> c.getCreator().equals(user) && c.getUuid().equals(campaignUuid));
        }

        PlayerCharacter playerCharacterToDelete;
        if(isOwner) {
            playerCharacterToDelete = Optional.ofNullable(playerCharacterRepository.findPlayerCharacterByUuidInCampaign(
                            characterUuid,
                            campaignUuid,
                            user.getId()))
                    .orElseThrow(() -> new PlayerCharacterNotFoundException("No player character found."));
        }
        else {
            playerCharacterToDelete = Optional.ofNullable(
                            playerCharacterRepository.findUserPlayerCharacterByUuid(characterUuid,
                                    user.getId()))
                    .orElseThrow(() -> new PlayerCharacterNotFoundException("No player character found."));
        }

        playerCharacterRepository.delete(playerCharacterToDelete);
    }

    public PlayerCharacterDTO createPlayerCharacter(UUID campaignUuid, PlayerCharacterRequestDTO playerCharacterRequestDTO) {
        User user = currentAuthUserService.getCurrentAuthUser();

        Campaign campaign = user.getJoinedCampaigns().stream()
                .filter(c -> c.getUuid().equals(campaignUuid))
                .findFirst()
                .orElseThrow(() -> new CampaignNotFoundException("Campaign not found."));

        // Might change it to use UUID instead of name in the future
        Race char_race = Optional.ofNullable(raceRepository.findByName(
                        playerCharacterRequestDTO.char_race()))
                .orElseThrow(() -> new RaceNotFoundException("Race not found."));
        Class char_class = Optional.ofNullable(classRepository.findByName(
                        playerCharacterRequestDTO.char_class()))
                .orElseThrow(() -> new ClassNotFoundException("Class not found."));

        PlayerCharacter playerCharacter = PlayerCharacter.builder()
                .characterName(playerCharacterRequestDTO.name())
                .playerRace(char_race)
                .playerCharClass(char_class)
                .experiencePoints(Math.min(355000, Math.max(0, playerCharacterRequestDTO.experience_points())))
                .active(true)
                .player(user)
                .campaign(campaign)
                .build();

        playerCharacterRepository.save(playerCharacter);

        return playerCharacterDTOMapper.apply(playerCharacter);
    }
}
