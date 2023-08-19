package com.example.mammabackend.global.common.jwt.application;

import com.example.mammabackend.global.common.jwt.enums.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtServiceFactory {

    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    public JwtService getTokenService(TokenType tokenType) {
        return switch (tokenType) {
            case ACCESS_TOKEN -> accessTokenService;
            case REFRESH_TOKEN -> refreshTokenService;
        };
    }

}
