package com.example.banking.controller;

import com.example.banking.dto.TransferRequest;
import com.example.banking.service.TransferService;
import com.example.banking.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    private final TransferService transferService;

    private final JwtUtil jwtUtil;

    public TransferController(TransferService transferService, JwtUtil jwtUtil) {
        this.transferService = transferService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public void transfer(
            @RequestBody TransferRequest req,
            @RequestHeader("Authorization") String authHeader
    ) {
        Long from = jwtUtil.extractUserId(authHeader);
        transferService.transfer(from, req.toUserId(), req.amount());
    }
}
