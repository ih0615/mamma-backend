package com.example.mammabackend.domain.member.dto;

import com.example.mammabackend.domain.member.domain.Member;
import com.example.mammabackend.domain.member.enums.MemberState;
import com.example.mammabackend.global.common.AddValid;
import com.example.mammabackend.global.common.Regexp;
import com.example.mammabackend.global.exception.ResponseCodes;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

public class MemberDto {

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    public static class IsDuplicatedEmailParam {

        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        @Email(message = ResponseCodes.VALID_INVALID)
        private String email;

    }

    @ToString
    @Getter
    @AllArgsConstructor
    public static class IsDuplicatedEmailView {

        private Boolean isDuplicated;
    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    public static class VerifyRequestEmailParam {

        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        @Email(message = ResponseCodes.VALID_INVALID)
        private String email;

    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerifyConfirmEmailParam {

        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        @Email(message = ResponseCodes.VALID_INVALID)
        private String email;
        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        private String emailValidationCode;


    }

    @ToString
    @Getter
    @AllArgsConstructor
    public static class VerifyConfirmEmailView {

        private boolean isValid;
    }


    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    public static class RegisterMemberParam implements AddValid {

        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        @Email(message = ResponseCodes.VALID_INVALID)
        private String email;
        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        private String emailValidationCode;
        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        @Pattern(regexp = Regexp.REGEXP_PASSWORD, message = ResponseCodes.VALID_INVALID)
        private String password;
        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        private String passwordCheck;
        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        @Pattern(regexp = Regexp.REGEXP_NICKNAME, message = ResponseCodes.VALID_INVALID)
        private String nickname;
        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        @Pattern(regexp = Regexp.REGEXP_NAME, message = ResponseCodes.VALID_INVALID)
        private String name;
        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        @Pattern(regexp = Regexp.REGEXP_PHONE, message = ResponseCodes.VALID_INVALID)
        private String phone;

        @Override
        public BindingResult verifyAdditional(BindingResult bindingResult) {
            if (bindingResult.hasErrors()) {
                return bindingResult;
            }
            if (!password.equals(passwordCheck)) {
                bindingResult.rejectValue("passwordCheck", ResponseCodes.VALID_INVALID,
                    ResponseCodes.VALID_INVALID);
            }

            return bindingResult;
        }

        public Member toEntity(PasswordEncoder passwordEncoder) {
            return Member.builder()
                .email(this.email)
                .password(passwordEncoder.encode(this.password))
                .nickname(this.nickname)
                .name(this.name)
                .phone(this.phone)
                .state(MemberState.NORMAL)
                .build();
        }
    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    public static class UpdateMemberParam implements AddValid {

        @Pattern(regexp = Regexp.REGEXP_PASSWORD, message = ResponseCodes.VALID_INVALID)
        private String password;
        private String passwordCheck;
        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        @Pattern(regexp = Regexp.REGEXP_NICKNAME, message = ResponseCodes.VALID_INVALID)
        private String nickname;
        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        @Pattern(regexp = Regexp.REGEXP_PHONE, message = ResponseCodes.VALID_INVALID)
        private String phone;

        public Member toEntity(Member member) {
            return member.toBuilder()
                .nickname(nickname)
                .phone(phone)
                .build();
        }

        @Override
        public BindingResult verifyAdditional(BindingResult bindingResult) {

            if (bindingResult.hasErrors()) {
                return bindingResult;
            }

            if (StringUtils.hasText(password) && !StringUtils.hasText(passwordCheck)) {
                bindingResult.rejectValue("password", ResponseCodes.VALID_REQUIRE,
                    ResponseCodes.VALID_REQUIRE);
            }

            if (!StringUtils.hasText(password) && StringUtils.hasText(passwordCheck)) {
                bindingResult.rejectValue("passwordCheck", ResponseCodes.VALID_REQUIRE,
                    ResponseCodes.VALID_REQUIRE);
            }

            if (StringUtils.hasText(password) && StringUtils.hasText(passwordCheck)
                && !password.equals(passwordCheck)) {
                bindingResult.rejectValue("passwordCheck", ResponseCodes.VALID_INVALID,
                    ResponseCodes.VALID_INVALID);
            }

            return bindingResult;
        }
    }

    @ToString
    @Getter
    @AllArgsConstructor
    @Builder
    public static class MemberInfoView {

        private String email;
        private String nickname;
        private String name;
        private String phone;

        public static MemberInfoView fromEntity(Member member) {

            return MemberInfoView.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .name(member.getName())
                .phone(member.getPhone())
                .build();
        }
    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    public static class FindMemberEmailParam {

        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        @Pattern(regexp = Regexp.REGEXP_NAME, message = ResponseCodes.VALID_INVALID)
        private String name;
        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        @Pattern(regexp = Regexp.REGEXP_PHONE, message = ResponseCodes.VALID_INVALID)
        private String phone;

    }

    @ToString
    @Getter
    @AllArgsConstructor
    public static class FindMemberEmailView {

        private String email;
    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    public static class FindMemberPasswordParam {

        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        @Email(message = ResponseCodes.VALID_INVALID)
        private String email;
        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        @Pattern(regexp = Regexp.REGEXP_NAME, message = ResponseCodes.VALID_INVALID)
        private String name;
        @NotBlank(message = ResponseCodes.VALID_REQUIRE)
        @Pattern(regexp = Regexp.REGEXP_PHONE, message = ResponseCodes.VALID_INVALID)
        private String phone;

    }
}
