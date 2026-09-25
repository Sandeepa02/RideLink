package com.ridelink.account.service;

import com.ridelink.account.dto.LoginRequest;
import com.ridelink.account.dto.RegisterRequest;
import com.ridelink.account.dto.AccountResponse;
import com.ridelink.account.dto.UpdateProfileRequest;
import com.ridelink.account.dto.UpdateRoleRequest;
import com.ridelink.account.dto.UpdateStatusRequest;
import com.ridelink.account.model.Account;
import com.ridelink.account.model.AccountStatus;
import com.ridelink.account.model.Role;
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

        if (request.getRole() == Role.ADMIN &&
                accountRepository.existsByRole(Role.ADMIN)) {
            throw new IllegalArgumentException("An ADMIN account already exists");
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

    public Account updateProfile(String email, UpdateProfileRequest request) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Account not found"));

        account.setFullName(request.getFullName());
        account.setPhone(request.getPhone());

        return accountRepository.save(account);
    }

    public Account updateRole(String email, UpdateRoleRequest request) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Account not found"));

        account.setRole(request.getRole());

        return accountRepository.save(account);
    }

    public Account updateStatus(String email, UpdateStatusRequest request) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Account not found"));

        account.setStatus(request.getStatus());

        return accountRepository.save(account);
    }
}
