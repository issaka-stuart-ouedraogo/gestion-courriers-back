package com.cil.bf.gestion_courriers_back.Auth.jwt;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTServiceInt {
    public String extractUsername(String token);

    public String generateAccessToken(UserDetails userDetail);

    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);

    public Boolean isTokenValid(String token, UserDetails userDetails);
}
