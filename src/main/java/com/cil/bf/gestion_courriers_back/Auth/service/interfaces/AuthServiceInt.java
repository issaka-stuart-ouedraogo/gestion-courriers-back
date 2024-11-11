package com.cil.bf.gestion_courriers_back.Auth.service.interfaces;

import org.springframework.http.ResponseEntity;

import com.cil.bf.gestion_courriers_back.Auth.dto.AuthDto;
import com.cil.bf.gestion_courriers_back.Auth.dto.RefreshTokenDto;
import com.cil.bf.gestion_courriers_back.Auth.dto.ResetPasswordDto;

public interface AuthServiceInt {
    public ResponseEntity<String> enableAccount(String token);

    public ResponseEntity<?> signIn(AuthDto authDto);

    public ResponseEntity<?> refreshToken(RefreshTokenDto refreshTokenDto);

    public ResponseEntity<String> resetPassword(ResetPasswordDto resetPasswordDto);
}
