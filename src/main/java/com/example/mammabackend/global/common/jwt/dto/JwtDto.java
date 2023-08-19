package com.example.mammabackend.global.common.jwt.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class JwtDto {

    @Getter
    @AllArgsConstructor
    public static class Token {

        private String token;
        private LocalDateTime expiredAt;
    }

}
