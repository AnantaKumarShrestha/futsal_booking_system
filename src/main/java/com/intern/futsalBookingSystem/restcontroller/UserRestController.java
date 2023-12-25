package com.intern.futsalBookingSystem.restcontroller;

import com.intern.futsalBookingSystem.dto.FutsalListDto;
import com.intern.futsalBookingSystem.dto.SlotDto;
import com.intern.futsalBookingSystem.dto.SlotsListDto;
import com.intern.futsalBookingSystem.dto.UserDto;
import com.intern.futsalBookingSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserRestController {



    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public UserDto signUpUser(@RequestBody UserDto user){
        return userService.signUpUser(user);
    }

    @GetMapping("/users/futsals")
    public List<FutsalListDto> getFutsalList(){
        return userService.getFutsalList();
    }

    @GetMapping("/users/futsals/{futsalId}/slots")
    public List<SlotsListDto> getAllSlots(@PathVariable("futsalId") UUID futsalId){
        return userService.getAllSlotsOfFutsal(futsalId);
    }

    @GetMapping("/users/futsals/{futsalId}/slots/available")
    public List<SlotsListDto> getAllAvailableSlot(@PathVariable("futsalId") UUID futsalId){
        return userService.getAvailableSLotsOfFutsal(futsalId);
    }

    @GetMapping("/users/futsals/{futsalId}/slots/unavailable")
    public List<SlotsListDto> getUnavailableSlot(@PathVariable("futsalId") UUID futsalId){
        return userService.getUnavailableSlotsOfFutsal(futsalId);
    }

    @GetMapping("/users/{userId}/futsal/slots/{slotId}/book")
    public ResponseEntity<SlotDto> bookSlots(@PathVariable("userId") UUID userId, @PathVariable("slotId") UUID slotId){
        return new ResponseEntity<>(userService.bookingFutsalSLots(userId,slotId), HttpStatus.OK);
    }


    @GetMapping("/user/futsal/slots/{slotId}/cancel")
    public ResponseEntity<SlotDto> cancelBooking(@PathVariable("slotId") UUID slotId){
        return new ResponseEntity<>(userService.cancelBooking(slotId),HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/slots/booked")
    public List<SlotsListDto> getOwnBookings(@PathVariable("userId") UUID userId){
        return userService.getOwnBookings(userId);
    }

}
