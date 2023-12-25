package com.intern.futsalBookingSystem.restcontroller;

import com.intern.futsalBookingSystem.dto.*;
import com.intern.futsalBookingSystem.enums.Status;
import com.intern.futsalBookingSystem.payload.ApiResponse;
import com.intern.futsalBookingSystem.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class AdminRestController {

    @Autowired
    private AdminService adminService;


    //========================================================================

    @PostMapping("/admin")
    public ResponseEntity<AdminDto> createAdmin(@RequestBody AdminDto adminDto){
        AdminDto admin=adminService.signUp(adminDto);
        return new ResponseEntity<>(admin, HttpStatus.CREATED);
    }
    //===================================================================


    @GetMapping("/admin/futsals")
    public List<FutsalListDto> getRegisteredFutsalList(){
        return adminService.getFutsalRegistredList();
    }

    @GetMapping("/admin/registration-request")
    public List<FutsalListDto> getAllFutsalRegistrationRequest(){
        return adminService.getFutsalRegistrationRequestList();
    }

    @GetMapping("/admin/users")
    public List<UserListDto> getAllUsers(){
        return adminService.getAllUsers();
    }

    @GetMapping("/admin/futsals/{futsalId}/registration/approve")
    public FutsalListDto getFutsalApprove(@PathVariable("futsalId") UUID futsalId){
        return adminService.approveFutsalRegistrationRequest(futsalId);
    }


    @GetMapping("/admin/futsals/{futsalId}/registration/reject")
    public ResponseEntity<ApiResponse> getFutsalReject(@PathVariable("futsalId")UUID futsalId){
        adminService.rejectFutsalRegistrationRequest(futsalId);
        return new ResponseEntity<>(new ApiResponse("Futsal registration request rejected", Status.SUCCESS),HttpStatus.OK);
    }

    @DeleteMapping("/admin/futsals/{futsalId}/remove")
    public ResponseEntity<ApiResponse> removeFutsalById(@PathVariable("futsalId") UUID futsalId){
        adminService.removeFutsal(futsalId);
        return new ResponseEntity<>(new ApiResponse("Futsal removed successfully",Status.SUCCESS),HttpStatus.OK);
    }

    @GetMapping("/admin/futsal-owners")
    public List<FutsalOwnerDto> getAllFutsalOwner(){
        return adminService.getFutsalOwnerList();
    }

    @GetMapping("/admin/futsal-owners/{futsalOwnerId}/futsals")
    public List<FutsalListDto> getFutsalListOfFutsalOwner(@PathVariable("futsalOwnerId") UUID futsalOwnerId){
        return adminService.getFutsalListOfFutsalOwner(futsalOwnerId);
    }




}
