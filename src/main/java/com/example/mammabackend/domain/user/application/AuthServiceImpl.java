package com.example.mammabackend.domain.user.application;

import com.example.mammabackend.domain.user.application.interfaces.AuthService;
import com.example.mammabackend.domain.user.dto.AuthDto.LoginParam;
import com.example.mammabackend.domain.user.dto.AuthDto.LoginView;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public LoginView login(LoginParam request) {
        return null;
    }

    @Override
    public LoginView reissue(String refreshToken) {
        return null;
    }

    @Override
    public void logout(Long memberSq) {

    }
}
