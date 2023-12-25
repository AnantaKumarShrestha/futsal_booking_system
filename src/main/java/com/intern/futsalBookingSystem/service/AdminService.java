package com.intern.futsalBookingSystem.service;

import com.intern.futsalBookingSystem.dto.*;

import java.util.List;
import java.util.UUID;

public interface AdminService {


   AdminDto signUp(AdminDto adminDto);

   List<FutsalListDto> getFutsalRegistrationRequestList();

   List<FutsalListDto> getFutsalRegistredList();

   FutsalListDto approveFutsalRegistrationRequest(UUID futsalId);

   void rejectFutsalRegistrationRequest(UUID futsalId);

   List<UserListDto> getAllUsers();

   void removeFutsal(UUID futsalId);

   List<FutsalListDto> getFutsalListOfFutsalOwner(UUID futsalOwnerId);

   List<FutsalOwnerDto> getFutsalOwnerList();

}
