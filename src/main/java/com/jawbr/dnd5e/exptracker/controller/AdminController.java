package com.jawbr.dnd5e.exptracker.controller;

import com.jawbr.dnd5e.exptracker.dto.request.ClassRequestDTO;
import com.jawbr.dnd5e.exptracker.dto.request.RaceRequestDTO;
import com.jawbr.dnd5e.exptracker.dto.response.ClassDTO;
import com.jawbr.dnd5e.exptracker.dto.response.RaceDTO;
import com.jawbr.dnd5e.exptracker.dto.response.UserDTO;
import com.jawbr.dnd5e.exptracker.service.ClassService;
import com.jawbr.dnd5e.exptracker.service.RaceService;
import com.jawbr.dnd5e.exptracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ClassService classService;
    private final RaceService raceService;
    private final UserService userService;

    public AdminController(ClassService classService, RaceService raceService, UserService userService) {
        this.classService = classService;
        this.raceService = raceService;
        this.userService = userService;
    }

    // CLASSES COMMANDS

    @PostMapping("/classes")
    public ClassDTO saveClass(@RequestBody @Valid ClassRequestDTO classRequestDTO) {
        return classService.saveClass(classRequestDTO);
    }

    @PutMapping("/classes/{className}")
    public ClassDTO updateClass(
            @RequestBody @Valid ClassRequestDTO classRequestDTO,
            @PathVariable String className)
    {
        return classService.updateClass(classRequestDTO, className);
    }

    @DeleteMapping("/classes/{className}")
    public ResponseEntity<Void> deleteClassByName(@PathVariable String className) {
        classService.deleteClassByName(className);
        return ResponseEntity.noContent().build();
    }

    // RACES COMMANDS

    @PostMapping("/races")
    public RaceDTO saveRace(@RequestBody @Valid RaceRequestDTO raceRequestDTO) {
        return raceService.saveRace(raceRequestDTO);
    }

    @PutMapping("/races/{raceName}")
    public RaceDTO updateRace(
            @RequestBody @Valid RaceRequestDTO raceRequestDTO,
            @PathVariable String raceName)
    {
        return raceService.updateRace(raceRequestDTO, raceName);
    }

    @DeleteMapping("/races/{raceName}")
    public ResponseEntity<Void> deleteRaceByName(@PathVariable String raceName) {
        raceService.deleteRaceByName(raceName);
        return ResponseEntity.noContent().build();
    }

    // USERS COMMANDS

    @DeleteMapping("/users/{userUuid}")
    public ResponseEntity<Void> deleteUserByUuid(
            @RequestParam(required = true, defaultValue = "false") boolean isConfirmed,
            @PathVariable UUID userUuid)
    {
        userService.adminDeleteUser(isConfirmed, userUuid);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/deactivate/{userUuid}")
    public ResponseEntity<Void> deactivateUser(
            @RequestParam(required = true, defaultValue = "false") boolean isConfirmed,
            @PathVariable UUID userUuid)
    {
        userService.adminDeactivateUser(isConfirmed, userUuid);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/reactivate/{userUuid}")
    public ResponseEntity<Void> reactivateUser(
            @RequestParam(required = true, defaultValue = "false") boolean isConfirmed,
            @PathVariable UUID userUuid)
    {
        userService.adminReactivateUser(isConfirmed, userUuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{userUuid}")
    public UserDTO checkUserProfile(@PathVariable UUID userUuid) {
        return userService.adminCheckUserProfile(userUuid);
    }
}
