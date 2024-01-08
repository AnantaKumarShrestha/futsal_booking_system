package com.intern.futsalBookingSystem.service;

import com.intern.futsalBookingSystem.payload.ApiResponse;
import com.intern.futsalBookingSystem.payload.RatingPayload;

public interface RatingService {

    ApiResponse submitRating(RatingPayload rating);
}
