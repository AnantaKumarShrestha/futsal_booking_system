package com.intern.futsalBookingSystem.restcontroller;

import com.intern.futsalBookingSystem.dto.*;
import com.intern.futsalBookingSystem.enums.Status;
import com.intern.futsalBookingSystem.payload.ApiResponse;
import com.intern.futsalBookingSystem.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(description = "Admin SignUp API")
    @PostMapping("/admin")
    public ResponseEntity<AdminDto> createAdmin(@RequestBody AdminDto adminDto){
        AdminDto admin=adminService.signUp(adminDto);
        return new ResponseEntity<>(admin, HttpStatus.CREATED);
    }
    //===================================================================

    @Operation(description = "Get Registered Futsal List")
    @GetMapping("/admin/futsals")
    public List<FutsalListDto> getRegisteredFutsalList(){
        return adminService.getFutsalRegistredList();
    }

    @Operation(description = "Get Futsal Registration Request List")
    @GetMapping("/admin/registration-request")
    public List<FutsalListDto> getAllFutsalRegistrationRequest(){
        return adminService.getFutsalRegistrationRequestList();
    }

    @Operation(description = "Get Users List")
    @GetMapping("/admin/users")
    public List<UserListDto> getAllUsers(){
        return adminService.getAllUsers();
    }

    @Operation(description = "Approve Futsal Registration Request")
    @GetMapping("/admin/futsals/{futsalId}/registration/approve")
    public FutsalListDto getFutsalApprove(@PathVariable("futsalId") UUID futsalId){
        return adminService.approveFutsalRegistrationRequest(futsalId);
    }


    @Operation(description = "Reject Futsal Registration Request")
    @GetMapping("/admin/futsals/{futsalId}/registration/reject")
    public ResponseEntity<ApiResponse> getFutsalReject(@PathVariable("futsalId")UUID futsalId){
        adminService.rejectFutsalRegistrationRequest(futsalId);
        return new ResponseEntity<>(new ApiResponse("Futsal registration request rejected", Status.SUCCESS),HttpStatus.OK);
    }

    @Operation(description = "Remove Futsal By Id")
    @DeleteMapping("/admin/futsals/{futsalId}/remove")
    public ResponseEntity<ApiResponse> removeFutsalById(@PathVariable("futsalId") UUID futsalId){
        adminService.removeFutsal(futsalId);
        return new ResponseEntity<>(new ApiResponse("Futsal removed successfully",Status.SUCCESS),HttpStatus.OK);
    }

    @Operation(description = "Get All Futsal Owner List")
    @GetMapping("/admin/futsal-owners")
    public List<FutsalOwnerDto> getAllFutsalOwner(){
        return adminService.getFutsalOwnerList();
    }

    @Operation(description = "Get Futsal List Of Owner")
    @GetMapping("/admin/futsal-owners/{futsalOwnerId}/futsals")
    public List<FutsalListDto> getFutsalListOfFutsalOwner(@PathVariable("futsalOwnerId") UUID futsalOwnerId){
        return adminService.getFutsalListOfFutsalOwner(futsalOwnerId);
    }




}
