package com.example.banking.service.impl;

import com.example.banking.entity.Account;
import com.example.banking.repository.AccountRepository;
import com.example.banking.service.BalanceScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Component
public class BalanceSchedulerImpl implements BalanceScheduler {

    private final AccountRepository accountRepo;
    private static final BigDecimal RATE = new BigDecimal("0.1");
    private static final BigDecimal MAX_FACTOR = new BigDecimal("2.07");

    public BalanceSchedulerImpl(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Override
    @Scheduled(fixedRate = 30_000)
    @Transactional
    public void incrementAllAccounts() {
        List<Account> accounts = accountRepo.findAll();
        for (Account acc : accounts) {
            BigDecimal maxAllowed = acc.getInitialBalance().multiply(MAX_FACTOR);
            BigDecimal increased = acc.getBalance().multiply(BigDecimal.ONE.add(RATE));

            if (increased.compareTo(maxAllowed) > 0) {
                acc.setBalance(maxAllowed);
            } else {
                acc.setBalance(increased);
            }
        }
    }
}

