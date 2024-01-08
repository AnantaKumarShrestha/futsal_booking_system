package com.intern.futsalBookingSystem.service;

import com.intern.futsalBookingSystem.dto.*;
import com.intern.futsalBookingSystem.payload.AuthenticationResponse;
import com.intern.futsalBookingSystem.payload.SignInModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface AdminService {

  AuthenticationResponse authenticate(SignInModel request);


 AdminDto signUp(String adminDto, MultipartFile file) throws IOException;

   List<FutsalListDto> getFutsalRegistrationRequestList();

   List<FutsalListDto> getFutsalRegistredList();

   FutsalListDto approveFutsalRegistrationRequest(UUID futsalId);

   void rejectFutsalRegistrationRequest(UUID futsalId);

   List<UserListDto> getAllUsers();

   void removeFutsal(UUID futsalId);

   List<FutsalListDto> getFutsalListOfFutsalOwner(UUID futsalOwnerId);

   List<FutsalOwnerDto> getFutsalOwnerList();

   AdminDto signIn(SignInModel signInModel);

}
