package com.ridelink.fare.service;

import com.ridelink.fare.dto.LoginRequest;
import com.ridelink.fare.dto.LoginResponse;
import com.ridelink.fare.security.JwtService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtService jwtService;

    public AuthService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        String username = request.getUsername();
        String password = request.getPassword();

        String role;

        if (username.equals("rider") && password.equals("rider123")) {
            role = "RIDER";

        } else if (username.equals("driver") && password.equals("driver123")) {
            role = "DRIVER";

        } else if (username.equals("admin") && password.equals("admin123")) {
            role = "ADMIN";

        } else {
            throw new RuntimeException("Invalid username or password");
        }

        String token = jwtService.generateToken(username, role);

        return new LoginResponse(
                token,
                username,
                role
        );
    }
}