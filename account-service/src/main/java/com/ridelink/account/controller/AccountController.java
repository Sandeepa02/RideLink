package com.ridelink.account.controller;

import com.ridelink.account.dto.AccountResponse;
import com.ridelink.account.dto.RegisterRequest;
import com.ridelink.account.dto.LoginRequest;
import com.ridelink.account.dto.LoginResponse;
import com.ridelink.account.model.Account;
import jakarta.validation.Valid;
import com.ridelink.account.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

   @PostMapping("/register")
    public ResponseEntity<AccountResponse> register(
        @Valid @RequestBody RegisterRequest request) {

    Account account = accountService.register(request);
    return ResponseEntity.ok(AccountResponse.from(account));
}

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        String token = accountService.login(request);

        return ResponseEntity.ok(
                new LoginResponse(token, "Bearer")
        );
    }

    @GetMapping("/me")
    public ResponseEntity<AccountResponse> getMyProfile(
            Authentication authentication) {

        String email = authentication.getName();

        Account account = accountService.getByEmail(email);

        return ResponseEntity.ok(AccountResponse.from(account));
    }

    @GetMapping("/passenger-test")
    public ResponseEntity<String> passengerTest() {
        return ResponseEntity.ok("Passenger access granted");
    }
}
