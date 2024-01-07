package com.intern.futsalBookingSystem.security;

import com.intern.futsalBookingSystem.db.AdminRepo;
import com.intern.futsalBookingSystem.db.FutsalOwnerRepo;
import com.intern.futsalBookingSystem.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class FutsalOwnerDetailsSerive implements UserDetailsService {

    @Autowired
    private FutsalOwnerRepo futsalOwnerRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return futsalOwnerRepo.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("Futsal Owner not found"));
    }
}
