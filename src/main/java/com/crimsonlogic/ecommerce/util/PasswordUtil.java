package com.crimsonlogic.ecommerce.util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {

    private PasswordUtil() {
    }

    public static String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verifyPassword(
            String password,
            String hashedPassword) {

        return BCrypt.checkpw(password, hashedPassword);
    }
}