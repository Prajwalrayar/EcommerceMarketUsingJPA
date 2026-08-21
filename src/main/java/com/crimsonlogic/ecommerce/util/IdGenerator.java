package com.crimsonlogic.ecommerce.util;

import java.security.SecureRandom;

public final class IdGenerator {

    private static final SecureRandom RANDOM =
            new SecureRandom();


    private IdGenerator() {
        // Utility class.
    }


    /**
     * Generates an ID using the supplied prefix
     * followed by a six-digit random number.
     *
     * Example:
     * CUS583921
     * SEL294817
     * ADM731492
     * ADR482913
     */
    public static String generateId(String prefix) {

        int randomNumber =
                10000 + RANDOM.nextInt(90000);

        return prefix.toUpperCase() + randomNumber;
    }
}