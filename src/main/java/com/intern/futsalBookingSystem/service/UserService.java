package com.intern.futsalBookingSystem.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intern.futsalBookingSystem.dto.FutsalListDto;
import com.intern.futsalBookingSystem.dto.SlotDto;
import com.intern.futsalBookingSystem.dto.SlotsListDto;
import com.intern.futsalBookingSystem.dto.UserDto;
import com.intern.futsalBookingSystem.model.UserModel;
import com.intern.futsalBookingSystem.payload.AuthenticationResponse;
import com.intern.futsalBookingSystem.payload.SignInModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService {

    AuthenticationResponse authenticate(SignInModel request);


    UserDto signUpUser(String user, MultipartFile file) throws IOException;

    List<FutsalListDto> getFutsalList();

    List<SlotsListDto> getAllSlotsOfFutsal(UUID futsalId);

    List<SlotsListDto> getAvailableSLotsOfFutsal(UUID futsalId);

    List<SlotsListDto> getUnavailableSlotsOfFutsal(UUID futsalId);

    SlotDto bookingFutsalSLots(UUID userId,UUID slotId);

    SlotDto cancelBooking(UUID slotId);

    List<SlotsListDto> getOwnBookings(UUID userId);

    UserDto userSignIn(SignInModel signInModel);


}
