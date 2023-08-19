package com.example.mammabackend.global.common.jwt.dao;

import com.example.mammabackend.global.common.jwt.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    boolean existsByRefreshToken(String refreshToken);

}
