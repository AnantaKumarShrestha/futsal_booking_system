package com.intern.futsalBookingSystem.payload;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInModel {

    @Email
    private String username;
    private String password;
}
