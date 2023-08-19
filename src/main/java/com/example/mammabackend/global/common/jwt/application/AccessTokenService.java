package com.example.mammabackend.global.common.jwt.application;

import com.example.mammabackend.global.common.jwt.dto.JwtDto.Token;
import com.example.mammabackend.global.common.jwt.enums.TokenType;
import com.example.mammabackend.global.common.jwt.properties.AccessTokenProperties;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccessTokenService implements JwtService {

    private final AccessTokenProperties properties;
    private final String ROLE_KEY = "role";
    private final String ROLE_VALUE = "ROLE_USER";
    private final String TOKEN_TYPE_KEY = "type";
    private final String TOKEN_TYPE_VALUE = TokenType.ACCESS_TOKEN.name().toUpperCase();
    private final SecretKey key;

    public AccessTokenService(AccessTokenProperties properties) {
        this.properties = properties;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(properties.getKey()));
    }

    @Override
    public Token issueToken(String subject, Map<String, Object> claims) {

        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + (properties.getExpireSecond() * 1000));

        String token = Jwts.builder().setSubject(subject)
            .claim(ROLE_KEY, ROLE_VALUE)
            .claim(TOKEN_TYPE_KEY, TOKEN_TYPE_VALUE)
            .addClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiredAt)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        return new Token(token,
            expiredAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
    }

    @Override
    public void revokeToken(String subject) {

    }

    @Override
    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    @Override
    public Map<String, Object> parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
