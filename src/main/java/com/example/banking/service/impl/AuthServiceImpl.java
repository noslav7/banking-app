package com.example.banking.service.impl;

import com.example.banking.dto.AuthRequest;
import com.example.banking.entity.User;
import com.example.banking.repository.UserRepository;
import com.example.banking.service.AuthService;
import com.example.banking.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;

    private final JwtUtil jwtUtil;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthServiceImpl(UserRepository userRepo, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String authenticateAndGetToken(AuthRequest req) {
        User user = userRepo.findByEmail(req.login())
                .or(() -> userRepo.findByPhone(req.login()))
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!encoder.matches(req.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return jwtUtil.generateToken(user.getId());
    }
}
