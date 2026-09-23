package com.ridelink.account.service;

import com.ridelink.account.dto.LoginRequest;
import com.ridelink.account.dto.RegisterRequest;
import com.ridelink.account.dto.AccountResponse;
import com.ridelink.account.model.Account;
import com.ridelink.account.model.AccountStatus;
import com.ridelink.account.repository.AccountRepository;
import com.ridelink.account.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AccountService(AccountRepository accountRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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

    public String login(LoginRequest request) {

        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                account.getPassword())) {

            throw new IllegalArgumentException("Invalid email or password");
        }

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException("Account is not active");
        }

        return jwtService.generateToken(account);
    }

    public Account getByEmail(String email) {

    return accountRepository.findByEmail(email)
            .orElseThrow(() ->
                    new IllegalArgumentException("Account not found"));
    }
}