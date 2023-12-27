package com.intern.futsalBookingSystem.payload;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class SignInModel {

    @Email
    private String username;
    private String password;
}
