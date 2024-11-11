package com.cil.bf.gestion_courriers_back.Auth.controllers.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cil.bf.gestion_courriers_back.Auth.dto.AuthDto;
import com.cil.bf.gestion_courriers_back.Auth.dto.RefreshTokenDto;
import com.cil.bf.gestion_courriers_back.Auth.dto.ResetPasswordDto;

@RequestMapping(path = "/auth/users")
public interface AuthControllerInt {

    @PostMapping(path = "/enable-account")
    public ResponseEntity<String> enableAccount(final @RequestParam("token") String token);

    @PostMapping(path = "/sign-in")
    public ResponseEntity<String> signIn(@RequestBody(required = true) AuthDto authDto);

    @PostMapping(path = "/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestBody(required = true) RefreshTokenDto refreshTokenDto);

    @PostMapping(path = "/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody(required = true) ResetPasswordDto resetPasswordDto);
}
