package com.example.mammabackend.global.common.jwt.filter;

import com.example.mammabackend.global.common.jwt.application.JwtService;
import com.example.mammabackend.global.common.jwt.application.JwtServiceFactory;
import com.example.mammabackend.global.common.jwt.enums.TokenType;
import com.example.mammabackend.global.common.jwt.properties.AccessTokenProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService accessTokenService;
    private final AccessTokenProperties accessTokenProperties;
    private final String AUTHORIZATION_HEADER = "Authorization";

    public JwtAuthenticationFilter(JwtServiceFactory jwtServiceFactory,
        AccessTokenProperties accessTokenProperties) {
        this.accessTokenService = jwtServiceFactory.getTokenService(TokenType.ACCESS_TOKEN);
        this.accessTokenProperties = accessTokenProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);

        if (StringUtils.hasText(token) && accessTokenService.validateToken(token)) {
            SecurityContextHolder.getContext().setAuthentication(getAuthentication(token));
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        String realToken = null;
        if (token != null) {
            String bearer = "Bearer";
            realToken = Optional.of(token)
                .map(String::trim) // 값의 처음과 끝 부분의 공백을 잘라낸다
                .filter(s -> s.startsWith(bearer)) // 값의 시작이 Bearer 로 시작하는지 검사
                .filter(s -> Character
                    .isSpaceChar(s.charAt(bearer.length()))) // Bearer 뒤에 공백이 하나 존재하는지 검사
                .map(s -> s.substring(bearer.length() + 1)) // 'Bearer ' 를 잘라낸다(공백 한개 포함)
                .orElse(null);
        }

        return realToken;
    }

    public Authentication getAuthentication(String token) {

        Map<String, Object> accessTokenClaims = accessTokenService.parseClaims(token);

        //클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(
                accessTokenClaims.get(accessTokenProperties.getROLE_KEY()).toString()));

        return new UsernamePasswordAuthenticationToken(accessTokenClaims.get("sub"), null,
            authorities);
    }
}
