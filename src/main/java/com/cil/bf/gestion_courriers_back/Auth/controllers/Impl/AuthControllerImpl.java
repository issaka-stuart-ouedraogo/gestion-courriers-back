package com.cil.bf.gestion_courriers_back.Auth.controllers.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.cil.bf.gestion_courriers_back.Auth.controllers.interfaces.AuthControllerInt;
import com.cil.bf.gestion_courriers_back.Auth.dto.AuthDto;
import com.cil.bf.gestion_courriers_back.Auth.dto.RefreshTokenDto;
import com.cil.bf.gestion_courriers_back.Auth.dto.ResetPasswordDto;
import com.cil.bf.gestion_courriers_back.Auth.service.interfaces.AuthServiceInt;

@RestController
@SuppressWarnings("unchecked")
public class AuthControllerImpl implements AuthControllerInt {

    @Autowired
    AuthServiceInt authService;

    @Override
    public ResponseEntity<String> enableAccount(String token) {
        return authService.enableAccount(token);
    }

    @Override
    public ResponseEntity<String> signIn(AuthDto authDto) {
        return (ResponseEntity<String>) authService.signIn(authDto);
    }

    @Override
    public ResponseEntity<String> refreshToken(RefreshTokenDto refreshTokenDto) {
        return (ResponseEntity<String>) authService.refreshToken(refreshTokenDto);
    }

    @Override
    public ResponseEntity<String> resetPassword(ResetPasswordDto resetPasswordDto) {
        return (ResponseEntity<String>) authService.resetPassword(resetPasswordDto);
    }

}
