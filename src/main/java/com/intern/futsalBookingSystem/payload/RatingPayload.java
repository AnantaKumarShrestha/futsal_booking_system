package com.intern.futsalBookingSystem.payload;


import lombok.Data;

import java.util.UUID;

@Data
public class RatingPayload {


    private UUID userId;
    private UUID futsalId;
    private int rating;

}
