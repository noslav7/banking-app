package com.example.banking.service;

import com.example.banking.dto.AuthRequest;

public interface AuthService {

    String authenticateAndGetToken(AuthRequest request);
}
