package com.example.mammabackend.domain.member.application.interfaces;

import com.example.mammabackend.domain.member.dto.AuthDto.LoginParam;
import com.example.mammabackend.domain.member.dto.AuthDto.LoginView;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    LoginView login(LoginParam request);

    LoginView reissue(String refreshToken);

    void logout(Long memberSq);
}
