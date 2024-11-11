package com.cil.bf.gestion_courriers_back.Auth.dto;

import lombok.Data;

@Data
public class ResetPasswordDto {
    // @NotNull
    // @Size(min = 6, max = 100)
    private String newPassword;

    // @NotNull
    // @Size(min = 6, max = 100)
    private String oldPassword;

    private Long userId;
}
