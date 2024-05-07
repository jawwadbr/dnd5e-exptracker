package com.jawbr.dnd5e.exptracker.service;

import com.jawbr.dnd5e.exptracker.dto.mapper.RaceDTOMapper;
import com.jawbr.dnd5e.exptracker.dto.request.RaceRequestDTO;
import com.jawbr.dnd5e.exptracker.dto.response.RaceDTO;
import com.jawbr.dnd5e.exptracker.entity.PlayerCharacter;
import com.jawbr.dnd5e.exptracker.entity.Race;
import com.jawbr.dnd5e.exptracker.exception.IllegalParameterException;
import com.jawbr.dnd5e.exptracker.exception.RaceNotFoundException;
import com.jawbr.dnd5e.exptracker.repository.PlayerCharacterRepository;
import com.jawbr.dnd5e.exptracker.repository.RaceRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RaceService {

    private final RaceRepository raceRepository;
    private final PlayerCharacterRepository playerCharacterRepository;
    private final RaceDTOMapper raceDTOMapper;

    public RaceService(RaceRepository raceRepository, PlayerCharacterRepository playerCharacterRepository, RaceDTOMapper raceDTOMapper) {
        this.raceRepository = raceRepository;
        this.playerCharacterRepository = playerCharacterRepository;
        this.raceDTOMapper = raceDTOMapper;
    }

    public Page<RaceDTO> findAllRaces(Integer page, Integer pageSize, String sortBy) {
        page = Optional.ofNullable(page).orElse(0);
        pageSize = Math.min(Optional.ofNullable(pageSize).orElse(6), 15);
        String sortByField = Optional.ofNullable(sortBy)
                .filter(s -> !s.isEmpty())
                .map(s -> {
                    if(s.equals("name"))
                        return "name";
                    else
                        throw new IllegalParameterException(String.format("Parameter '%s' is illegal.", sortBy));
                })
                .orElse("id");

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortByField));

        Page<Race> races = Optional.of(raceRepository.findAll(pageable))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new RaceNotFoundException("No races found."));

        return races.map(raceDTOMapper);
    }

    public RaceDTO findByUuid(UUID raceUuid) {
        return Optional.ofNullable(raceRepository.findByUuid(raceUuid))
                .map(raceDTOMapper)
                .orElseThrow(() -> new RaceNotFoundException("No races found."));
    }

    /*
     * ADMIN ENDPOINTS
     */

    public RaceDTO saveRace(RaceRequestDTO raceRequestDTO) {
        if(Optional.ofNullable(raceRepository.findByName(raceRequestDTO.name())).isPresent()) {
            throw new DataIntegrityViolationException(
                    String.format("Race '%s' already exists.", raceRequestDTO.name()));
        }

        Race newRace = new Race();
        newRace.setName(raceRequestDTO.name());
        newRace = raceRepository.save(newRace);

        return RaceDTO.builder()
                .name(newRace.getName())
                .race_uuid(newRace.getUuid().toString())
                .build();
    }

    public void deleteRaceByName(String raceName) {
        Optional.of(raceRepository.findByName(raceName)).ifPresentOrElse(
                race -> {
                    List<PlayerCharacter> characters = playerCharacterRepository.findByPlayerRace(race);
                    characters.forEach(c -> c.setPlayerRace(null));
                    raceRepository.delete(race);
                },
                () -> {
                    throw new RaceNotFoundException(String.format("Race '%s' not found.", raceName));
                }
        );
    }

    public RaceDTO updateRace(RaceRequestDTO raceRequestDTO, String raceName) {
        return Optional.of(raceRepository.findByName(raceName)).map((r) -> {
                    Race updatedRace = Race.builder()
                            .uuid(r.getUuid())
                            .name(raceRequestDTO.name())
                            .id(r.getId())
                            .build();

                    raceRepository.save(updatedRace);
                    return RaceDTO.builder()
                            .race_uuid(updatedRace.getUuid().toString())
                            .name(updatedRace.getName())
                            .build();
                })
                .orElseThrow(() ->
                        new RaceNotFoundException(String.format("Race '%s' not found.", raceName)));
    }
}
