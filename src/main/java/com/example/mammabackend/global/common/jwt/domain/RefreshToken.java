package com.example.mammabackend.global.common.jwt.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "refresh_user")
public class RefreshToken {

    @Id
    private Long id;
    @Indexed
    private String refreshToken;
    @TimeToLive
    private Long expirationInSeconds;

    public void update(String refreshToken, Long expirationInSeconds) {
        this.refreshToken = refreshToken;
        this.expirationInSeconds = expirationInSeconds;
    }
}
