package com.example.mammabackend.global.common.jwt.properties;

import com.example.mammabackend.global.common.jwt.enums.TokenType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("props.token.access-token")
public class AccessTokenProperties {

    private final String ROLE_KEY = "role";
    private final String ROLE_VALUE = "ROLE_USER";
    private final String TOKEN_TYPE_KEY = "type";
    private final String TOKEN_TYPE_VALUE = TokenType.ACCESS_TOKEN.name().toUpperCase();
    @NotBlank
    private String key;
    @NotBlank
    private Long expireSecond;
}
