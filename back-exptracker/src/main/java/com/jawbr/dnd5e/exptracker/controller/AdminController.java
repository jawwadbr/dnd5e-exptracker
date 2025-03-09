package com.jawbr.dnd5e.exptracker.controller;

import com.jawbr.dnd5e.exptracker.dto.request.ClassRequestDTO;
import com.jawbr.dnd5e.exptracker.dto.request.RaceRequestDTO;
import com.jawbr.dnd5e.exptracker.dto.response.ClassDTO;
import com.jawbr.dnd5e.exptracker.dto.response.RaceDTO;
import com.jawbr.dnd5e.exptracker.dto.response.UserDTO;
import com.jawbr.dnd5e.exptracker.service.ClassService;
import com.jawbr.dnd5e.exptracker.service.RaceService;
import com.jawbr.dnd5e.exptracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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

@Tag(name = "Admin Controller", description = "Admin endpoints")
@RestController
@RequestMapping("/api/v1/admin")
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

    @Operation(summary = "Admin create new class",
            security = {@SecurityRequirement(name = "Bearer ")})
    @PostMapping("/classes")
    public ClassDTO saveClass(@RequestBody @Valid ClassRequestDTO classRequestDTO) {
        return classService.saveClass(classRequestDTO);
    }

    @Operation(summary = "Admin update a class",
            security = {@SecurityRequirement(name = "Bearer ")})
    @PutMapping("/classes/{className}")
    public ClassDTO updateClass(
            @RequestBody @Valid ClassRequestDTO classRequestDTO,
            @PathVariable String className)
    {
        return classService.updateClass(classRequestDTO, className);
    }

    @Operation(summary = "Admin delete class",
            security = {@SecurityRequirement(name = "Bearer ")})
    @DeleteMapping("/classes/{className}")
    public ResponseEntity<Void> deleteClassByName(@PathVariable String className) {
        classService.deleteClassByName(className);
        return ResponseEntity.noContent().build();
    }

    // RACES COMMANDS

    @Operation(summary = "Admin create new race",
            security = {@SecurityRequirement(name = "Bearer ")})
    @PostMapping("/races")
    public RaceDTO saveRace(@RequestBody @Valid RaceRequestDTO raceRequestDTO) {
        return raceService.saveRace(raceRequestDTO);
    }

    @Operation(summary = "Admin update race",
            security = {@SecurityRequirement(name = "Bearer ")})
    @PutMapping("/races/{raceName}")
    public RaceDTO updateRace(
            @RequestBody @Valid RaceRequestDTO raceRequestDTO,
            @PathVariable String raceName)
    {
        return raceService.updateRace(raceRequestDTO, raceName);
    }

    @Operation(summary = "Admin delete race",
            security = {@SecurityRequirement(name = "Bearer ")})
    @DeleteMapping("/races/{raceName}")
    public ResponseEntity<Void> deleteRaceByName(@PathVariable String raceName) {
        raceService.deleteRaceByName(raceName);
        return ResponseEntity.noContent().build();
    }

    // USERS COMMANDS

    @Operation(summary = "Admin delete user by UUID",
            security = {@SecurityRequirement(name = "Bearer ")})
    @DeleteMapping("/users/{userUuid}")
    public ResponseEntity<Void> deleteUserByUuid(
            @RequestParam(defaultValue = "false") boolean isConfirmed,
            @PathVariable UUID userUuid)
    {
        userService.adminDeleteUser(isConfirmed, userUuid);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Admin toggle User Activation state by UUID",
            security = {@SecurityRequirement(name = "Bearer ")},
            description = "Admin can toggle a user activation as True/False, this means a user with activation at false cannot login.")
    @PutMapping("/users/activation/{userUuid}")
    public ResponseEntity<Void> toggleUserActivation(
            @RequestParam(defaultValue = "false") boolean isConfirmed,
            @PathVariable UUID userUuid)
    {
        userService.adminToggleUserActivation(isConfirmed, userUuid);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Admin check user by UUID",
            security = {@SecurityRequirement(name = "Bearer ")})
    @GetMapping("/users/{userUuid}")
    public UserDTO checkUserProfile(@PathVariable UUID userUuid) {
        return userService.adminCheckUserProfile(userUuid);
    }

    @Operation(summary = "Admin find all users",
            security = {@SecurityRequirement(name = "Bearer ")})
    @GetMapping("/users")
    public Page<UserDTO> findAllUsers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String sortBy
    )
    {
        return userService.adminFindAllUsers(page, pageSize, sortBy);
    }

    @Operation(summary = "Admin find user using username",
            security = {@SecurityRequirement(name = "Bearer ")})
    @GetMapping("/users/u/{username}")
    public Page<UserDTO> findUsersByUsername(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String sortBy,
            @PathVariable String username
    )
    {
        return userService.adminFindUsersByUsername(page, pageSize, sortBy, username);
    }

    @Operation(summary = "Admin find all other admins",
            security = {@SecurityRequirement(name = "Bearer ")})
    @GetMapping("/users/admins")
    public Page<UserDTO> findAllAdmins(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String sortBy
    )
    {
        return userService.adminFindAllAdmins(page, pageSize, sortBy);
    }

    @Operation(summary = "Admin find admin by username",
            security = {@SecurityRequirement(name = "Bearer ")})
    @GetMapping("/users/admins/u/{username}")
    public Page<UserDTO> findAdminsByUsername(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String sortBy,
            @PathVariable String username
    )
    {
        return userService.adminFindAdminsByUsername(page, pageSize, sortBy, username);
    }

}
