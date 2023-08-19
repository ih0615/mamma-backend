package com.example.mammabackend.global.common.jwt.application;

import com.example.mammabackend.global.common.jwt.dto.JwtDto.Token;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {

    Token issueToken(String subject, Map<String, Object> claims);

    void revokeToken(String subject);

    boolean validateToken(String token);

    Map<String, Object> parseClaims(String token);

}
