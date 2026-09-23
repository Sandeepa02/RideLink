package com.ridelink.account.security;

import com.ridelink.account.model.Account;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "RideLinkAccountServiceSecretKey2026ForJWT123456789";

    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    private final SecretKey key;

    public JwtService() {
        this.key = Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateToken(Account account) {

        return Jwts.builder()
                .subject(account.getEmail())
                .claim("role", account.getRole().name())
                .claim("accountId", account.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }
}