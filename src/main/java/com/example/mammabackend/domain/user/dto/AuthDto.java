package com.example.mammabackend.domain.user.dto;

import com.example.mammabackend.global.exception.ResponseCodes;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class AuthDto {

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    public static class LoginParam {

        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        private String email;
        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        private String password;

    }

    @ToString
    @Getter
    @AllArgsConstructor
    @Builder
    public static class LoginView {

        private String accessToken;
        private LocalDateTime accessTokenExpiredAt;
        private String refreshToken;
        private LocalDateTime refreshTokenExpiredAt;

    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ReIssueParam {

        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        private String refreshToken;

    }

}
