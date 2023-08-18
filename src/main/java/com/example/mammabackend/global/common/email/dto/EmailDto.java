package com.example.mammabackend.global.common.email.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class EmailDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class EmailMessage {

        private String to;
        private String subject;
        private String message;
    }

}
