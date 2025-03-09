package com.jawbr.dnd5e.exptracker.controller;

import com.jawbr.dnd5e.exptracker.dto.response.RaceDTO;
import com.jawbr.dnd5e.exptracker.service.RaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Race Controller", description = "Race endpoints")
@RestController
@RequestMapping("/api/v1/races")
public class RaceController {

    private final RaceService raceService;

    public RaceController(RaceService raceService) {
        this.raceService = raceService;
    }

    @Operation(summary = "Find all races",
            security = {@SecurityRequirement(name = "Bearer ")})
    @GetMapping
    public Page<RaceDTO> findAllRaces(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String sortBy
    )
    {
        return raceService.findAllRaces(page, pageSize, sortBy);
    }

    @Operation(summary = "Find race using UUID",
            security = {@SecurityRequirement(name = "Bearer ")})
    @GetMapping("/{raceUuid}")
    public RaceDTO findByUuid(@PathVariable UUID raceUuid) {
        return raceService.findByUuid(raceUuid);
    }
}
