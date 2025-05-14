package com.example.banking.controller;

import com.example.banking.dto.AuthRequest;
import com.example.banking.dto.JwtResponse;
import com.example.banking.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public JwtResponse login(@Valid @RequestBody AuthRequest req) {
        String token = authService.authenticateAndGetToken(req);
        return new JwtResponse(token);
    }
}
