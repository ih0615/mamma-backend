package com.example.mammabackend.domain.user.dto;

import com.example.mammabackend.domain.user.domain.MemberSuspend;
import com.example.mammabackend.domain.user.enums.MemberState;
import com.example.mammabackend.global.common.jwt.dto.JwtDto.Token;
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
        private MemberState state;
        private LocalDateTime suspendStartDateTime;
        private LocalDateTime suspendEndDateTime;

        public static LoginView normal(Token accessToken, Token refreshToken) {
            return LoginView.builder()
                .accessToken(accessToken.getToken())
                .accessTokenExpiredAt(accessToken.getExpiredAt())
                .refreshToken(refreshToken.getToken())
                .refreshTokenExpiredAt(refreshToken.getExpiredAt())
                .state(MemberState.NORMAL)
                .build();
        }

        public static LoginView suspend(MemberSuspend memberSuspend) {
            return LoginView.builder()
                .state(MemberState.SUSPEND)
                .suspendStartDateTime(memberSuspend.getStartDateTime())
                .suspendEndDateTime(memberSuspend.getEndDateTime())
                .build();
        }

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
