package com.intern.futsalBookingSystem.restcontroller;

import com.intern.futsalBookingSystem.dto.FutsalListDto;
import com.intern.futsalBookingSystem.dto.FutsalOwnerDto;
import com.intern.futsalBookingSystem.dto.SlotDto;
import com.intern.futsalBookingSystem.payload.SlotRequest;
import com.intern.futsalBookingSystem.payload.TurnOverStats;
import com.intern.futsalBookingSystem.service.FutsalOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class FutsalOwnerController {


    @Autowired
    private FutsalOwnerService futsalOwnerService;

    @PostMapping("/futsal-owner")
    public ResponseEntity<FutsalOwnerDto> signUpFutsalOwner(@RequestBody FutsalOwnerDto futsalOwner){
        FutsalOwnerDto savedFutsalOwner=futsalOwnerService.signUpFutsalOwner(futsalOwner);
        return new ResponseEntity<>(savedFutsalOwner, HttpStatus.OK);
    }

    @PostMapping("/futsal-owner/{futsalOwnerId}/futsals/registration")
    public ResponseEntity<FutsalListDto> futsalRegistrationRequestToAdmin(@PathVariable("futsalOwnerId") UUID futsalOwnerId, @RequestBody FutsalListDto futsal){
        FutsalListDto savedFutsal=futsalOwnerService.futsalRegistrationRequestToAdmin(futsalOwnerId,futsal);
        return new ResponseEntity<>(savedFutsal,HttpStatus.OK);
    }

    @PostMapping("/futsal-owner/futsal/{futsalId}/slot")
    public List<SlotDto> addSFutsalSlots(@PathVariable UUID futsalId, @RequestBody SlotRequest slotRequest){
        return futsalOwnerService.createFutsalSLots(futsalId,slotRequest);
    }

    @GetMapping("/futsal-owner/futsal/slots/{slotId}/complete-booking")
    public SlotDto completeSlotBooking(@PathVariable("slotId") UUID slotId){
        return futsalOwnerService.completeBooking(slotId);
    }


    @GetMapping("/futsal-owner/{futsalOwnerId}/futsal/{futsalId}/turnover")
    public TurnOverStats getTurnOverOfFutsal(@PathVariable("futsalOwnerId") UUID futsalOwnerId, @PathVariable("futsalId") UUID futsalId){
        return futsalOwnerService.getTurnOverStats(futsalOwnerId,futsalId);
    }


    @GetMapping("/futsal-owner/futsal/slots/{slotId}")
    public SlotDto cancelFutsalBooking(@PathVariable("slotId") UUID slotId){
        return futsalOwnerService.cancelBooking(slotId);
    }

    @GetMapping("/futsal-owner/futsal/{futsalId}")
    public List<SlotDto> ownFutsalSlots(@PathVariable("futsalId") UUID futsalId){
        return futsalOwnerService.getOwnFutsalSlots(futsalId);
    }



}
