package com.crimsonlogic.ecommerce.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class PasswordUtil {

    private static final BCryptPasswordEncoder ENCODER =
            new BCryptPasswordEncoder();

    private PasswordUtil() {
    }

    public static String encryptPassword(String password) {
        return ENCODER.encode(password);
    }

    public static boolean verifyPassword(
            String password,
            String hashedPassword) {

        return ENCODER.matches(
                password,
                hashedPassword
        );
    }
}