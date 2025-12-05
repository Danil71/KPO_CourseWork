package com.software.software_development.core.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = IsoDateValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsoDate {
    String message() default "Invalid date format. Expected ISO-8601 (e.g. 2024-05-23T00:00:00Z)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
