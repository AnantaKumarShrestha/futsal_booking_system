package com.intern.futsalBookingSystem.service.serviceImpl;

import com.intern.futsalBookingSystem.db.FutsalRepo;
import com.intern.futsalBookingSystem.db.RatingRepo;
import com.intern.futsalBookingSystem.db.UserRepo;
import com.intern.futsalBookingSystem.enums.Status;
import com.intern.futsalBookingSystem.exception.ResourceNotFoundException;
import com.intern.futsalBookingSystem.model.FutsalModel;
import com.intern.futsalBookingSystem.model.RatingModel;
import com.intern.futsalBookingSystem.model.UserModel;
import com.intern.futsalBookingSystem.payload.ApiResponse;
import com.intern.futsalBookingSystem.payload.RatingPayload;
import com.intern.futsalBookingSystem.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class RatingServiceImpl implements RatingService {


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FutsalRepo futsalRepo;

    @Autowired
    private RatingRepo ratingRepo;

    @Override
    public ApiResponse submitRating(RatingPayload ratingPayload) {
        UUID userId=ratingPayload.getUserId();
        UUID futsalId=ratingPayload.getFutsalId();
        int ratingValue=ratingPayload.getRating();

        UserModel user=userRepo.getUserById(userId).orElseThrow(()->new ResourceNotFoundException("User not found Exception"));
        FutsalModel futsal=futsalRepo.getFutsalById(futsalId).orElseThrow(()->new ResourceNotFoundException("Futsal Not found Exception"));

        RatingModel existingRating=ratingRepo.getRatingByUserAndFutsal(user,futsal).orElse(null);

        if (existingRating != null) {
            existingRating.setRating(ratingValue);
            ratingRepo.save(existingRating);
        } else {
            RatingModel newRating = new RatingModel();
            newRating.setUser(user);
            newRating.setFutsal(futsal);
            newRating.setRating(ratingValue);
            ratingRepo.save(newRating);
        }

        return new ApiResponse("Rating submitted successfully", Status.SUCCESS);
    }


}
