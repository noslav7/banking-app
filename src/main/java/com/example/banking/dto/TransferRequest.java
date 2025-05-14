package com.example.banking.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TransferRequest(
        @NotNull Long toUserId,
        @NotNull @DecimalMin("0.01") BigDecimal amount
) {}

