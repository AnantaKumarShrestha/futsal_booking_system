package com.intern.futsalBookingSystem.service.serviceImpl;

import com.intern.futsalBookingSystem.db.UserRepo;
import com.intern.futsalBookingSystem.model.UserModel;
import com.intern.futsalBookingSystem.service.UserSignin;
import org.springframework.beans.factory.annotation.Autowired;

public class UserSignInServiceIMpl implements UserSignin {

    @Autowired
    private UserRepo userRepo;

    @Override
    public String addUser(UserModel userModel) {
        userRepo.save(userModel);
        return "saved";
    }
}
