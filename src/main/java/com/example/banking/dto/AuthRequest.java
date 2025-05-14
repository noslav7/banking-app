package com.example.banking.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank String login,
        @NotBlank String password
) {}

