package com.example.mammabackend.global.common.jwt.properties;

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

    @NotBlank
    private String key;
    @NotBlank
    private Long expireSecond;
}
