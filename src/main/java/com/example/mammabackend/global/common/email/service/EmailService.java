package com.example.mammabackend.global.common.email.service;

import com.example.mammabackend.global.common.email.dto.EmailDto.EmailMessage;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {

    void sendMail(EmailMessage emailMessage);
}
