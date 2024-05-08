package com.jawbr.dnd5e.exptracker.controller;

import com.jawbr.dnd5e.exptracker.dto.request.UserRequestDTO;
import com.jawbr.dnd5e.exptracker.dto.response.UserCreationDTO;
import com.jawbr.dnd5e.exptracker.dto.response.UserDTO;
import com.jawbr.dnd5e.exptracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserDTO userCheckInfo() {
        return userService.userCheckInfo();
    }

    @PostMapping("/register")
    public ResponseEntity<UserCreationDTO> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return new ResponseEntity<>(userService.registerUser(userRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{userUuid}")
    public UserDTO checkUserProfile(@PathVariable UUID userUuid) {
        return userService.checkUserProfile(userUuid);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
            @RequestParam(required = true, defaultValue = "false") boolean isConfirmed)
    {
        userService.deleteUser(isConfirmed);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    public UserDTO updateUser(@RequestBody UserRequestDTO userRequestDTO) {
        return userService.updateUser(userRequestDTO);
    }
}
