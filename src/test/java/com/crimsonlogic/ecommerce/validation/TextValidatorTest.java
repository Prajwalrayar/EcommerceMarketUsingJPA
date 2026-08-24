package com.crimsonlogic.ecommerce.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class TextValidatorTest {

    private TextValidator textValidator;

    private ConstraintValidatorContext context;

    private ConstraintValidatorContext.ConstraintViolationBuilder builder;


    // ==========================================================
    // SETUP
    // ==========================================================

    @BeforeEach
    void setUp() {

        textValidator = new TextValidator();

        // Mock ConstraintValidatorContext
        context = mock(ConstraintValidatorContext.class);

        // Mock the builder returned by
        // buildConstraintViolationWithTemplate()
        builder =
                mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

        /*
         * IMPORTANT:
         *
         * TextValidator internally does:
         *
         * context.buildConstraintViolationWithTemplate(message)
         *        .addConstraintViolation();
         *
         * Mockito returns null by default for the builder.
         *
         * Therefore we explicitly return our mocked builder.
         */
        when(
                context.buildConstraintViolationWithTemplate(anyString())
        ).thenReturn(builder);
    }


    // ==========================================================
    // NULL / BLANK VALUES
    // ==========================================================

    @Test
    void shouldAcceptNullValue() {

        boolean result =
                textValidator.isValid(
                        null,
                        context
                );

        assertTrue(result);

        // Context should not be used for null values
        verifyNoInteractions(context);
    }


    @Test
    void shouldAcceptEmptyValue() {

        boolean result =
                textValidator.isValid(
                        "",
                        context
                );

        assertTrue(result);

        verifyNoInteractions(context);
    }


    @Test
    void shouldAcceptBlankValue() {

        boolean result =
                textValidator.isValid(
                        "   ",
                        context
                );

        assertTrue(result);

        verifyNoInteractions(context);
    }


    // ==========================================================
    // VALID TEXT
    // ==========================================================

    @Test
    void shouldAcceptValidText() {

        boolean result =
                textValidator.isValid(
                        "Prajwal",
                        context
                );

        assertTrue(result);

        verifyNoInteractions(context);
    }


    @Test
    void shouldAcceptNormalMixedText() {

        boolean result =
                textValidator.isValid(
                        "Prajwal123@",
                        context
                );

        assertTrue(result);

        verifyNoInteractions(context);
    }


    // ==========================================================
    // THREE CONSECUTIVE IDENTICAL LETTERS
    // ==========================================================

    @Test
    void shouldRejectThreeConsecutiveIdenticalLetters() {

        boolean result =
                textValidator.isValid(
                        "Suuu",
                        context
                );

        assertFalse(result);

        verify(context)
                .disableDefaultConstraintViolation();

        verify(context)
                .buildConstraintViolationWithTemplate(
                        "Value cannot contain 3 consecutive identical letters."
                );

        verify(builder)
                .addConstraintViolation();
    }


    @Test
    void shouldRejectAaaPattern() {

        boolean result =
                textValidator.isValid(
                        "aaa",
                        context
                );

        assertFalse(result);

        verify(builder)
                .addConstraintViolation();
    }


    @Test
    void shouldRejectBbbPattern() {

        boolean result =
                textValidator.isValid(
                        "bbb",
                        context
                );

        assertFalse(result);

        verify(builder)
                .addConstraintViolation();
    }


    @Test
    void shouldRejectUppercaseRepeatedLetters() {

        boolean result =
                textValidator.isValid(
                        "AAA",
                        context
                );

        assertFalse(result);

        verify(builder)
                .addConstraintViolation();
    }


    // ==========================================================
    // ALPHABETICAL SEQUENCES
    // ==========================================================

    @Test
    void shouldRejectAlphabeticalSequenceAbc() {

        boolean result =
                textValidator.isValid(
                        "abc",
                        context
                );

        assertFalse(result);

        verify(context)
                .disableDefaultConstraintViolation();

        verify(context)
                .buildConstraintViolationWithTemplate(
                        "Value cannot contain alphabetical sequences."
                );

        verify(builder)
                .addConstraintViolation();
    }


    @Test
    void shouldRejectAlphabeticalSequenceBcd() {

        boolean result =
                textValidator.isValid(
                        "bcd",
                        context
                );

        assertFalse(result);

        verify(builder)
                .addConstraintViolation();
    }


    @Test
    void shouldRejectAlphabeticalSequenceXyz() {

        boolean result =
                textValidator.isValid(
                        "xyz",
                        context
                );

        assertFalse(result);

        verify(builder)
                .addConstraintViolation();
    }


    // ==========================================================
    // CASE INSENSITIVE VALIDATION
    // ==========================================================

    @Test
    void shouldRejectUppercaseAlphabeticalSequence() {

        boolean result =
                textValidator.isValid(
                        "ABC",
                        context
                );

        assertFalse(result);

        verify(builder)
                .addConstraintViolation();
    }


    @Test
    void shouldRejectMixedCaseAlphabeticalSequence() {

        boolean result =
                textValidator.isValid(
                        "AbC",
                        context
                );

        assertFalse(result);

        verify(builder)
                .addConstraintViolation();
    }


    // ==========================================================
    // NON-LETTER CHARACTERS
    // ==========================================================

    @Test
    void shouldRejectAlphabeticalSequenceWithNumbers() {

        /*
         * TextValidator removes non-letters:
         *
         * a1b2c3
         *    ↓
         * abc
         *
         * abc is an alphabetical sequence.
         */

        boolean result =
                textValidator.isValid(
                        "a1b2c3",
                        context
                );

        assertFalse(result);

        verify(builder)
                .addConstraintViolation();
    }


    @Test
    void shouldRejectAlphabeticalSequenceWithSpecialCharacters() {

        /*
         * TextValidator removes non-letters:
         *
         * a-b-c
         *   ↓
         * abc
         *
         * Therefore it must be rejected.
         */

        boolean result =
                textValidator.isValid(
                        "a-b-c",
                        context
                );

        assertFalse(result);

        verify(builder)
                .addConstraintViolation();
    }


    // ==========================================================
    // VALID TEXT WITH NUMBERS / SPECIAL CHARACTERS
    // ==========================================================

    @Test
    void shouldAcceptTextWithNumbersWhenNoInvalidPatternExists() {

        boolean result =
                textValidator.isValid(
                        "Prajwal123",
                        context
                );

        assertTrue(result);

        verifyNoInteractions(context);
    }


    @Test
    void shouldAcceptTextWithSpecialCharactersWhenNoInvalidPatternExists() {

        boolean result =
                textValidator.isValid(
                        "Prajwal@123",
                        context
                );

        assertTrue(result);

        verifyNoInteractions(context);
    }
}