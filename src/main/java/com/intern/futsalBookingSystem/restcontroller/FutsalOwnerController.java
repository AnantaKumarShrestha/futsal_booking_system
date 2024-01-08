package com.intern.futsalBookingSystem.restcontroller;

import com.intern.futsalBookingSystem.dto.FutsalListDto;
import com.intern.futsalBookingSystem.dto.FutsalOwnerDto;
import com.intern.futsalBookingSystem.dto.InvoiceDto;
import com.intern.futsalBookingSystem.dto.SlotDto;
import com.intern.futsalBookingSystem.payload.AuthenticationResponse;
import com.intern.futsalBookingSystem.payload.SignInModel;
import com.intern.futsalBookingSystem.payload.SlotRequest;
import com.intern.futsalBookingSystem.payload.TurnOverStats;
import com.intern.futsalBookingSystem.service.FutsalOwnerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
public class FutsalOwnerController {


    @Autowired
    private FutsalOwnerService futsalOwnerService;

    @Operation(description = "SignUp Futsal Owner")
    @PostMapping("/futsal-owner")
    public ResponseEntity<FutsalOwnerDto> signUpFutsalOwner(@RequestParam("futsalOwner") String futsalOwner, @RequestParam("photo")MultipartFile photo) throws IOException {
        FutsalOwnerDto savedFutsalOwner=futsalOwnerService.signUpFutsalOwner(futsalOwner,photo);
        return new ResponseEntity<>(savedFutsalOwner, HttpStatus.CREATED);
    }


    @Operation(description = "Futsal owner sign inr")
    @PostMapping("/futsal-owner/signin")
    public ResponseEntity<AuthenticationResponse> futsalOwnerSignIn(@RequestBody SignInModel signInModel) throws IOException {
        AuthenticationResponse authenticationResponse=futsalOwnerService.authenticate(signInModel);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

    @Operation(description = "Futsal Registration Request To Admin")
    @PostMapping("/futsal-owner/{futsalOwnerId}/futsals/registration")
    public ResponseEntity<FutsalListDto> futsalRegistrationRequestToAdmin(@PathVariable("futsalOwnerId") UUID futsalOwnerId, @RequestParam("futsal") String futsal,@RequestParam("photo") MultipartFile photo) throws IOException {
        FutsalListDto savedFutsal=futsalOwnerService.futsalRegistrationRequestToAdmin(futsalOwnerId,futsal,photo);
        return new ResponseEntity<>(savedFutsal,HttpStatus.OK);
    }

    @Operation(description = "Add Futsal Game Slots")
    @PostMapping("/futsal-owner/futsal/{futsalId}/slot")
    public List<SlotDto> addSFutsalSlots(@PathVariable UUID futsalId, @RequestBody SlotRequest slotRequest){
        return futsalOwnerService.createFutsalSLots(futsalId,slotRequest);
    }

    @Operation(description = "Complete Booking After User pay the Bill")
    @GetMapping("/futsal-owner/futsal/slots/{slotId}/complete-booking")
    public SlotDto completeSlotBooking(@PathVariable("slotId") UUID slotId) throws IOException {
        return futsalOwnerService.completeBooking(slotId);
    }


    @Operation(description = "Get TurnOver Of Futsal")
    @GetMapping("/futsal-owner/{futsalOwnerId}/futsal/{futsalId}/turnover")
    public TurnOverStats getTurnOverOfFutsal(@PathVariable("futsalOwnerId") UUID futsalOwnerId, @PathVariable("futsalId") UUID futsalId){
        return futsalOwnerService.getTurnOverStats(futsalOwnerId,futsalId);
    }

    @Operation(description = "Cancel Futsal Booking By FutsalOwner")
    @GetMapping("/futsal-owner/futsal/slots/{slotId}")
    public SlotDto cancelFutsalBooking(@PathVariable("slotId") UUID slotId){
        return futsalOwnerService.cancelBooking(slotId);
    }

    @Operation(description = "Own Futsal Game Slots")
    @GetMapping("/futsal-owner/futsal/{futsalId}/slot")
    public List<SlotDto> ownFutsalSlots(@PathVariable("futsalId") UUID futsalId){
        return futsalOwnerService.getOwnFutsalSlots(futsalId);
    }

    @Operation(description = "Own Futsal statement")
    @GetMapping("/futsal-owner/futsal/{futsalId}/statement")
    public List<InvoiceDto> getFutsalStatement(@PathVariable("futsalId") UUID futsalId){
        return futsalOwnerService.invoiceExcelFile(futsalId);
    }


}
