package com.intern.futsalBookingSystem.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intern.futsalBookingSystem.dto.FutsalDto;
import com.intern.futsalBookingSystem.dto.FutsalListDto;
import com.intern.futsalBookingSystem.dto.FutsalOwnerDto;
import com.intern.futsalBookingSystem.dto.SlotDto;
import com.intern.futsalBookingSystem.mapper.FutsalListMapper;
import com.intern.futsalBookingSystem.payload.SlotRequest;
import com.intern.futsalBookingSystem.payload.TurnOverStats;
import com.intern.futsalBookingSystem.utils.MailUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FutsalOwnerService {



    FutsalOwnerDto signUpFutsalOwner(String futsalOwner, MultipartFile photo) throws IOException;

    FutsalListDto futsalRegistrationRequestToAdmin(UUID futsalOwnerId, String futsalDto,MultipartFile photo) throws IOException;

    List<SlotDto> createFutsalSLots(UUID futsalId, SlotRequest slotRequest);

    TurnOverStats getTurnOverStats(UUID futsalOwnerId,UUID futsalId);

    List<SlotDto> getOwnFutsalSlots(UUID futsalId);

    SlotDto cancelBooking(UUID slotID);

    SlotDto completeBooking(UUID slotId) throws IOException;

    void invoiceExcelFile(UUID id);


}
