package com.example.banking.service.impl;

import com.example.banking.entity.Account;
import com.example.banking.repository.AccountRepository;
import com.example.banking.service.TransferService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepo;

    public TransferServiceImpl(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Override
    @Transactional
    public void transfer(Long fromUserId, Long toUserId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма перевода должна быть > 0");
        }

        Account from = accountRepo.findByUserId(fromUserId)
                .orElseThrow(() -> new IllegalArgumentException("Счёт отправителя не найден"));

        if (from.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Недостаточно средств");
        }

        Account to = accountRepo.findByUserId(toUserId)
                .orElseThrow(() -> new IllegalArgumentException("Счёт получателя не найден"));

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        accountRepo.save(from);
        accountRepo.save(to);
    }
}

