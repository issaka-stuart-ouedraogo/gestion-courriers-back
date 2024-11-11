package com.cil.bf.gestion_courriers_back.Auth.dto;

import lombok.Data;

@Data
public class JwtAuthResponseDto {
    private String accessToken;

    private String refreshToken;
}
