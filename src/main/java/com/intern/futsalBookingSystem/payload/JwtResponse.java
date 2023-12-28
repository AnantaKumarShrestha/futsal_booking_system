package com.intern.futsalBookingSystem.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JwtResponse {

    private String jwtToken;
    private String username;

    public JwtResponse(String token) {
    }
}
