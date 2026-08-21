package com.crimsonlogic.ecommerce.validation;

import com.crimsonlogic.ecommerce.validation.interfaces.ValidText;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class TextValidator
        implements ConstraintValidator<ValidText, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank())
            return true;

        String text = value.toLowerCase()
                .replaceAll("[^a-z]", "");

        // aaa, bbb, ccc...
        for (int i = 0; i < text.length() - 2; i++) {

            if (text.charAt(i) == text.charAt(i + 1)
                    && text.charAt(i) == text.charAt(i + 2)) {

                addMessage(
                        context,
                        "Value cannot contain 3 consecutive identical letters."
                );

                return false;
            }
        }

        // abc, bcd, cde, mno, xyz...
        for (int i = 0; i < text.length() - 2; i++) {

            char a = text.charAt(i);
            char b = text.charAt(i + 1);
            char c = text.charAt(i + 2);

            if (b == a + 1 && c == b + 1) {

                addMessage(
                        context,
                        "Value cannot contain alphabetical sequences."
                );

                return false;
            }
        }

        return true;
    }

    private void addMessage(
            ConstraintValidatorContext context,
            String message) {

        context.disableDefaultConstraintViolation();

        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}