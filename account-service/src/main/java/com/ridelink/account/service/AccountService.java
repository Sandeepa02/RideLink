package com.ridelink.account.service;

import com.ridelink.account.dto.RegisterRequest;
import com.ridelink.account.model.Account;
import com.ridelink.account.model.AccountStatus;
import com.ridelink.account.repository.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository,
                          PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Account register(RegisterRequest request) {

        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        Account account = new Account(
                request.getFullName(),
                request.getEmail(),
                hashedPassword,
                request.getPhone(),
                request.getRole(),
                AccountStatus.ACTIVE
        );

        return accountRepository.save(account);
    }
}