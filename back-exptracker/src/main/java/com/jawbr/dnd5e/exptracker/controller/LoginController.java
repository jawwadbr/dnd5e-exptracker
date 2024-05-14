package com.jawbr.dnd5e.exptracker.controller;

import com.jawbr.dnd5e.exptracker.dto.request.LoginRequestDTO;
import com.jawbr.dnd5e.exptracker.dto.response.TokenDTO;
import com.jawbr.dnd5e.exptracker.security.jwt.JwtUserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final JwtUserService jwtUserService;

    public LoginController(JwtUserService jwtUserService) {
        this.jwtUserService = jwtUserService;
    }

    @PostMapping("/login")
    public TokenDTO login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return jwtUserService.generateToken(loginRequestDTO);
    }
}
