package com.example.mammabackend.global.common;

import com.example.mammabackend.global.exception.ResponseCodes;
import java.security.Principal;
import java.util.Random;
import org.springframework.util.StringUtils;

public class Helper {

    private static final Helper instance = new Helper();

    private Helper() {

    }

    public static Helper getInstance() {
        return instance;
    }

    public Long getMemberSq(Principal principal) {

        if (principal == null) {
            throw new IllegalStateException(ResponseCodes.PROCESS_UNAUTHORIZED);
        }
        String name = principal.getName();
        if (!StringUtils.hasText(name)) {
            throw new IllegalStateException(ResponseCodes.PROCESS_UNAUTHORIZED);
        }
        return Long.valueOf(name);
    }

    public String generateRandomAlphanumericString(int length) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(length)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }
}
