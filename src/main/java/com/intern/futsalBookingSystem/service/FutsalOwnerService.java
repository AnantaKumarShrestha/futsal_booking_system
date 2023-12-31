package com.intern.futsalBookingSystem.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intern.futsalBookingSystem.dto.*;
import com.intern.futsalBookingSystem.mapper.FutsalListMapper;
import com.intern.futsalBookingSystem.model.FutsalOwnerModel;
import com.intern.futsalBookingSystem.model.InvoiceModel;
import com.intern.futsalBookingSystem.payload.AuthenticationResponse;
import com.intern.futsalBookingSystem.payload.SignInModel;
import com.intern.futsalBookingSystem.payload.SlotRequest;
import com.intern.futsalBookingSystem.payload.TurnOverStats;
import com.intern.futsalBookingSystem.utils.MailUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FutsalOwnerService {

    AuthenticationResponse authenticate(SignInModel request);



    FutsalOwnerDto signUpFutsalOwner(String futsalOwner, MultipartFile photo) throws IOException;

    FutsalListDto futsalRegistrationRequestToAdmin(UUID futsalOwnerId, String futsalDto,MultipartFile photo) throws IOException;

    List<SlotDto> createFutsalSLots(UUID futsalId, SlotRequest slotRequest);

    TurnOverStats getTurnOverStats(UUID futsalOwnerId,UUID futsalId);

    List<SlotDto> getOwnFutsalSlots(UUID futsalId);

    SlotDto cancelBooking(UUID slotID);

    SlotDto completeBooking(UUID slotId) throws IOException;

    List<InvoiceDto> invoiceExcelFile(UUID id);

    FutsalOwnerDto SignIn(SignInModel signInModel);






}
