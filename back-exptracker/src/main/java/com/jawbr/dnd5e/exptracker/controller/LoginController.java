package com.jawbr.dnd5e.exptracker.controller;

import com.jawbr.dnd5e.exptracker.config.security.jwt.JwtUserService;
import com.jawbr.dnd5e.exptracker.dto.request.LoginRequestDTO;
import com.jawbr.dnd5e.exptracker.dto.response.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication Controller", description = "User authentication endpoints")
@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final JwtUserService jwtUserService;

    public LoginController(JwtUserService jwtUserService) {
        this.jwtUserService = jwtUserService;
    }

    @Operation(summary = "Login endpoint")
    @PostMapping("/login")
    public TokenDTO login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return jwtUserService.generateToken(loginRequestDTO);
    }
}
