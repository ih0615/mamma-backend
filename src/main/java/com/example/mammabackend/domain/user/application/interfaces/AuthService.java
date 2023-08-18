package com.example.mammabackend.domain.user.application.interfaces;

import com.example.mammabackend.domain.user.dto.AuthDto.LoginParam;
import com.example.mammabackend.domain.user.dto.AuthDto.LoginView;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    LoginView login(LoginParam request);

    LoginView reissue(String refreshToken);

    void logout(Long memberSq);
}
