package com.jawbr.dnd5e.exptracker.service;

import com.jawbr.dnd5e.exptracker.dto.mapper.RaceDTOMapper;
import com.jawbr.dnd5e.exptracker.dto.response.RaceDTO;
import com.jawbr.dnd5e.exptracker.entity.Race;
import com.jawbr.dnd5e.exptracker.exception.IllegalParameterException;
import com.jawbr.dnd5e.exptracker.exception.RaceNotFoundException;
import com.jawbr.dnd5e.exptracker.repository.RaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RaceService {

    private final RaceRepository raceRepository;
    private final RaceDTOMapper raceDTOMapper;

    public RaceService(RaceRepository raceRepository, RaceDTOMapper raceDTOMapper) {
        this.raceRepository = raceRepository;
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
}
