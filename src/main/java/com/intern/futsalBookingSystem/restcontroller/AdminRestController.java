package com.intern.futsalBookingSystem.restcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intern.futsalBookingSystem.dto.*;
import com.intern.futsalBookingSystem.enums.Status;
import com.intern.futsalBookingSystem.payload.ApiResponse;
import com.intern.futsalBookingSystem.payload.SignInModel;
import com.intern.futsalBookingSystem.service.AdminService;
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
public class AdminRestController {

    @Autowired
    private AdminService adminService;


    //========================================================================

    @Operation(description = "Admin SignUp API")
    @PostMapping("/admin")
    public ResponseEntity<AdminDto> createAdmin(@RequestParam("admin") String admin,@RequestParam("photo") MultipartFile file) throws IOException {
        AdminDto savedAdmin=adminService.signUp(admin,file);
        return new ResponseEntity<>(savedAdmin, HttpStatus.CREATED);
    }

    @Operation(description = "Admin SignIn")
    @PostMapping("/admin/signin")
    public ResponseEntity<AdminDto> adminSignIn(@RequestBody SignInModel signInModel) {
        AdminDto admin=adminService.signIn(signInModel);
        return new ResponseEntity<>(admin, HttpStatus.OK);
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
