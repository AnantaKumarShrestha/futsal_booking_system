package com.intern.futsalBookingSystem.service;

import com.intern.futsalBookingSystem.dto.FutsalListDto;
import com.intern.futsalBookingSystem.dto.SlotDto;
import com.intern.futsalBookingSystem.dto.SlotsListDto;
import com.intern.futsalBookingSystem.dto.UserDto;
import com.intern.futsalBookingSystem.model.UserModel;

import java.util.List;
import java.util.UUID;

public interface UserService {


    UserDto signUpUser(UserDto userDto);

    List<FutsalListDto> getFutsalList();

    List<SlotsListDto> getAllSlotsOfFutsal(UUID futsalId);

    List<SlotsListDto> getAvailableSLotsOfFutsal(UUID futsalId);

    List<SlotsListDto> getUnavailableSlotsOfFutsal(UUID futsalId);

    SlotDto bookingFutsalSLots(UUID userId,UUID slotId);

    SlotDto cancelBooking(UUID slotId);

    List<SlotsListDto> getOwnBookings(UUID userId);

}
