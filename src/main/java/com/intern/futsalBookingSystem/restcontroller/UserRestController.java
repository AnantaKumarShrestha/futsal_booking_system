package com.intern.futsalBookingSystem.restcontroller;

import com.intern.futsalBookingSystem.dto.FutsalListDto;
import com.intern.futsalBookingSystem.dto.SlotDto;
import com.intern.futsalBookingSystem.dto.SlotsListDto;
import com.intern.futsalBookingSystem.dto.UserDto;
import com.intern.futsalBookingSystem.payload.SignInModel;
import com.intern.futsalBookingSystem.service.UserService;
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
public class UserRestController {



    @Autowired
    private UserService userService;

    @Operation(description = "User SignUp API")
    @PostMapping("/user")
    public UserDto signUpUser(@RequestParam("user") String user, @RequestParam("photo")MultipartFile file) throws IOException {
        return userService.signUpUser(user,file);
    }

    @Operation(description = "User SignIn API")
    @PostMapping("/user/signin")
    public UserDto UserSignIn(@RequestBody SignInModel signInModel)  {
        return userService.userSignIn(signInModel);
    }

    @Operation(description = "Get Available Futsal List")
    @GetMapping("/users/futsals")
    public List<FutsalListDto> getFutsalList(){
        return userService.getFutsalList();
    }

    @Operation(description = "Get All Game Slots Of Specific Futsal")
    @GetMapping("/users/futsals/{futsalId}/slots")
    public List<SlotsListDto> getAllSlots(@PathVariable("futsalId") UUID futsalId){
        return userService.getAllSlotsOfFutsal(futsalId);
    }

    @Operation(description = "Get All Available Game Slots Of Specific Futsal")
    @GetMapping("/users/futsals/{futsalId}/slots/available")
    public List<SlotsListDto> getAllAvailableSlot(@PathVariable("futsalId") UUID futsalId){
        return userService.getAvailableSLotsOfFutsal(futsalId);
    }

    @Operation(description = "Get Unavailable Futsal Slots Of Specific Futsal")
    @GetMapping("/users/futsals/{futsalId}/slots/unavailable")
    public List<SlotsListDto> getUnavailableSlot(@PathVariable("futsalId") UUID futsalId){
        return userService.getUnavailableSlotsOfFutsal(futsalId);
    }
    @Operation(description = "Book Futsal Game SLots")
    @GetMapping("/users/{userId}/futsal/slots/{slotId}/book")
    public ResponseEntity<SlotDto> bookSlots(@PathVariable("userId") UUID userId, @PathVariable("slotId") UUID slotId){
        return new ResponseEntity<>(userService.bookingFutsalSLots(userId,slotId), HttpStatus.OK);
    }

    @Operation(description = "Cancel Booking")
    @GetMapping("/user/futsal/slots/{slotId}/cancel")
    public ResponseEntity<SlotDto> cancelBooking(@PathVariable("slotId") UUID slotId){
        return new ResponseEntity<>(userService.cancelBooking(slotId),HttpStatus.OK);
    }

    @Operation(description = "Get All Own Futsal Game Booking List")
    @GetMapping("/users/{userId}/slots/booked")
    public List<SlotsListDto> getOwnBookings(@PathVariable("userId") UUID userId){
        return userService.getOwnBookings(userId);
    }

}
