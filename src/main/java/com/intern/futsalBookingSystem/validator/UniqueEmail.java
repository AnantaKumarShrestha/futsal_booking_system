package com.intern.futsalBookingSystem.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
@ReportAsSingleViolation
@Documented
public @interface UniqueEmail {

    public String message() default "Email Already exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
