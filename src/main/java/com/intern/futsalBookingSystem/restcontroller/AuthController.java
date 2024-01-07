package com.intern.futsalBookingSystem.restcontroller;

import com.intern.futsalBookingSystem.payload.JwtRequest;
import com.intern.futsalBookingSystem.payload.JwtResponse;
import com.intern.futsalBookingSystem.security.UserModelUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {


//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//
//    @Autowired
//    private UserModelUserDetailsService userDetailsService;
//    @Autowired
//    private JwtHelper jwtTokenUtil;
//
//
//
//    @PostMapping("/login")
//    public ResponseEntity<?> authenticate(@RequestBody JwtRequest authenticationRequest) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
//        String token = jwtTokenUtil.generateToken(userDetails);
//
//        return ResponseEntity.ok(new JwtResponse(token));
//    }

}
