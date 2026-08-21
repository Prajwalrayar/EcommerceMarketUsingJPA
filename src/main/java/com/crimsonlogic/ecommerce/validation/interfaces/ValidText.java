package com.crimsonlogic.ecommerce.validation.interfaces;

import com.crimsonlogic.ecommerce.validation.TextValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TextValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidText {

    String message() default "Invalid text value.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}