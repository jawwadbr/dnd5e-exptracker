package com.jawbr.dnd5e.exptracker.controller;

import com.jawbr.dnd5e.exptracker.dto.request.UserRequestDTO;
import com.jawbr.dnd5e.exptracker.dto.response.UserCreationDTO;
import com.jawbr.dnd5e.exptracker.dto.response.UserDTO;
import com.jawbr.dnd5e.exptracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "User Controller", description = "User endpoints")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "User check own information",
            security = {@SecurityRequirement(name = "Bearer ")})
    @GetMapping("/me")
    public UserDTO userCheckInfo() {
        return userService.userCheckInfo();
    }

    @Operation(summary = "Register new user", responses = {
            @ApiResponse(responseCode = "200", description = "User created!"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email is already in use.",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"status\": 409,\n" +
                                    "  \"message\": \"string\",\n" +
                                    "  \"timestamp\": \"string\"\n" +
                                    "}")
                    )
            )
    })
    @PostMapping("/register")
    public ResponseEntity<UserCreationDTO> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return new ResponseEntity<>(userService.registerUser(userRequestDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "User checks another user information",
            security = {@SecurityRequirement(name = "Bearer ")})
    @GetMapping("/{userUuid}")
    public UserDTO checkUserProfile(@PathVariable UUID userUuid) {
        return userService.checkUserProfile(userUuid);
    }

    @Operation(summary = "User delete his account.",
            security = {@SecurityRequirement(name = "Bearer ")})
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
            @RequestParam(defaultValue = "false") boolean isConfirmed)
    {
        userService.deleteUser(isConfirmed);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "User updates their information using the PATCH method to partially modify an existing resource.",
            security = {@SecurityRequirement(name = "Bearer ")})
    @PatchMapping
    public UserDTO updateUser(@RequestBody UserRequestDTO userRequestDTO) {
        return userService.updateUser(userRequestDTO);
    }
}
