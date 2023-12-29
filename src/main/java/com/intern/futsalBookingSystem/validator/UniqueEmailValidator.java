package com.intern.futsalBookingSystem.validator;

import com.intern.futsalBookingSystem.db.UserRepo;
import com.intern.futsalBookingSystem.validator.UniqueEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail,String> {

    @Autowired
    private UserRepo userRepo;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        System.out.println("as");
        if (s.isBlank()) {
            return true;
        }
        System.out.println("as");

        if(userRepo.existsByEmail(s)){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Email address must be unique").addConstraintViolation();
            return false;
        }

        return true;

    }
}
