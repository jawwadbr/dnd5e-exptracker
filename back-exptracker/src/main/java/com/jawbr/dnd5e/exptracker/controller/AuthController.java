package com.jawbr.dnd5e.exptracker.controller;

import com.jawbr.dnd5e.exptracker.config.security.jwt.JwtUserService;
import com.jawbr.dnd5e.exptracker.dto.request.LoginRequestDTO;
import com.jawbr.dnd5e.exptracker.dto.response.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication Controller", description = "User authentication endpoints")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtUserService jwtUserService;

    public AuthController(JwtUserService jwtUserService) {
        this.jwtUserService = jwtUserService;
    }

    @Operation(summary = "Login endpoint")
    @PostMapping("/login")
    public TokenDTO login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return jwtUserService.generateToken(loginRequestDTO);
    }

    @Operation(summary = "Logout endpoint",
            security = {@SecurityRequirement(name = "Bearer ")},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Logout"),
                    @ApiResponse(responseCode = "401", description = "Token expired, invalid or incorrect")
            })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        jwtUserService.logout(authHeader);
        return ResponseEntity.noContent().build();
    }
}
