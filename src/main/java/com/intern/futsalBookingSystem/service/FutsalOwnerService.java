package com.intern.futsalBookingSystem.service;

import com.intern.futsalBookingSystem.dto.FutsalDto;
import com.intern.futsalBookingSystem.dto.FutsalListDto;
import com.intern.futsalBookingSystem.dto.FutsalOwnerDto;
import com.intern.futsalBookingSystem.dto.SlotDto;
import com.intern.futsalBookingSystem.mapper.FutsalListMapper;
import com.intern.futsalBookingSystem.payload.SlotRequest;
import com.intern.futsalBookingSystem.payload.TurnOverStats;

import java.util.List;
import java.util.UUID;

public interface FutsalOwnerService {



    FutsalOwnerDto signUpFutsalOwner(FutsalOwnerDto futsalOwnerDto);

    FutsalListDto futsalRegistrationRequestToAdmin(UUID futsalOwnerId, FutsalListDto futsalDto);

    List<SlotDto> createFutsalSLots(UUID futsalId, SlotRequest slotRequest);

    TurnOverStats getTurnOverStats(UUID futsalOwnerId,UUID futsalId);

    List<SlotDto> getOwnFutsalSlots(UUID futsalId);

    SlotDto cancelBooking(UUID slotID);


}
