package com.jawbr.dnd5e.exptracker.dto.mapper;

import com.jawbr.dnd5e.exptracker.dto.response.RaceDTO;
import com.jawbr.dnd5e.exptracker.entity.Race;
import com.jawbr.dnd5e.exptracker.util.Mapper;

import java.util.function.Function;

@Mapper
public class RaceDTOMapper implements Function<Race, RaceDTO> {

    @Override
    public RaceDTO apply(Race race) {
        return RaceDTO.builder()
                .name(race.getName())
                .race_uuid(race.getUuid().toString())
                .build();
    }
}
