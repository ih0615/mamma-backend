package com.example.mammabackend.domain.user.application;

import com.example.mammabackend.domain.user.application.interfaces.AuthService;
import com.example.mammabackend.domain.user.dao.MemberRepository;
import com.example.mammabackend.domain.user.dao.MemberSuspendRepository;
import com.example.mammabackend.domain.user.domain.Member;
import com.example.mammabackend.domain.user.domain.MemberSuspend;
import com.example.mammabackend.domain.user.dto.AuthDto.LoginParam;
import com.example.mammabackend.domain.user.dto.AuthDto.LoginView;
import com.example.mammabackend.domain.user.enums.MemberState;
import com.example.mammabackend.global.common.jwt.application.JwtService;
import com.example.mammabackend.global.common.jwt.application.JwtServiceFactory;
import com.example.mammabackend.global.common.jwt.dao.RefreshTokenRepository;
import com.example.mammabackend.global.common.jwt.domain.RefreshToken;
import com.example.mammabackend.global.common.jwt.dto.JwtDto.Token;
import com.example.mammabackend.global.common.jwt.enums.TokenType;
import com.example.mammabackend.global.exception.ResponseCodes;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final MemberSuspendRepository memberSuspendRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService accessTokenService;
    private final JwtService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthServiceImpl(MemberRepository memberRepository,
        MemberSuspendRepository memberSuspendRepository, PasswordEncoder passwordEncoder,
        JwtServiceFactory jwtServiceFactory, RefreshTokenRepository refreshTokenRepository) {
        this.memberRepository = memberRepository;
        this.memberSuspendRepository = memberSuspendRepository;
        this.passwordEncoder = passwordEncoder;
        this.accessTokenService = jwtServiceFactory.getTokenService(TokenType.ACCESS_TOKEN);
        this.refreshTokenService = jwtServiceFactory.getTokenService(TokenType.REFRESH_TOKEN);
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public LoginView login(LoginParam request) {

        Member member = memberRepository.findByEmailAndStateNot(request.getEmail(),
                MemberState.WITHDRAW)
            .orElseThrow(() -> new IllegalStateException(ResponseCodes.PROCESS_NOT_EXIST));

        if (member.getState().equals(MemberState.SUSPEND)) {
            MemberSuspend memberSuspend = memberSuspendRepository.findByMemberAndIsAppliedIsTrue(
                    member)
                .orElseThrow(() -> new IllegalStateException(ResponseCodes.PROCESS_NOT_EXIST));
            return LoginView.suspend(memberSuspend);
        }

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException(ResponseCodes.VALID_INVALID);
        }

        String subject = String.valueOf(member.getMemberSq());

        Token accessToken = accessTokenService.issueToken(subject, Collections.emptyMap());
        Token refreshToken = refreshTokenService.issueToken(subject, Collections.emptyMap());

        return LoginView.normal(accessToken, refreshToken);
    }

    @Override
    public LoginView reissue(String refreshToken) {
        RefreshToken existRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new IllegalStateException(ResponseCodes.PROCESS_NOT_EXIST));

        Long memberSq = existRefreshToken.getId();

        Member member = memberRepository.findByMemberSqAndStateNot(memberSq, MemberState.WITHDRAW)
            .orElseThrow(() -> new IllegalStateException(ResponseCodes.PROCESS_NOT_EXIST));

        if (member.getState().equals(MemberState.SUSPEND)) {
            MemberSuspend memberSuspend = memberSuspendRepository.findByMemberAndIsAppliedIsTrue(
                    member)
                .orElseThrow(() -> new IllegalStateException(ResponseCodes.PROCESS_NOT_EXIST));
            return LoginView.suspend(memberSuspend);
        }

        String subject = String.valueOf(member.getMemberSq());

        Token renewAccessToken = accessTokenService.issueToken(subject, Collections.emptyMap());
        Token renewRefreshToken = refreshTokenService.issueToken(subject, Collections.emptyMap());

        return LoginView.normal(renewAccessToken, renewRefreshToken);
    }

    @Override
    public void logout(Long memberSq) {
        refreshTokenService.revokeToken(String.valueOf(memberSq));
    }
}
