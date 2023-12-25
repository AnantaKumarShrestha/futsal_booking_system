package com.intern.futsalBookingSystem.service.serviceImpl;


import com.intern.futsalBookingSystem.db.AdminRepo;
import com.intern.futsalBookingSystem.db.FutsalOwnerRepo;
import com.intern.futsalBookingSystem.db.FutsalRepo;
import com.intern.futsalBookingSystem.db.UserRepo;
import com.intern.futsalBookingSystem.dto.AdminDto;
import com.intern.futsalBookingSystem.dto.FutsalListDto;
import com.intern.futsalBookingSystem.dto.FutsalOwnerDto;
import com.intern.futsalBookingSystem.dto.UserDto;
import com.intern.futsalBookingSystem.exception.ResourceNotFoundException;
import com.intern.futsalBookingSystem.mapper.*;
import com.intern.futsalBookingSystem.model.AdminModel;
import com.intern.futsalBookingSystem.model.FutsalModel;
import com.intern.futsalBookingSystem.model.FutsalOwnerModel;
import com.intern.futsalBookingSystem.model.UserModel;
import com.intern.futsalBookingSystem.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {


    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private FutsalRepo futsalRepo;

    @Autowired
    private FutsalOwnerRepo futsalOwnerRepo;

    @Autowired
    private UserRepo userRepo;


    @Override
    public AdminDto signUp(AdminDto adminDto) {
        AdminModel admin= AdminMapper.INSTANCE.adminDtoIntoAdminModel(adminDto);
        AdminModel savedAdmin=adminRepo.save(admin);
        return AdminMapper.INSTANCE.adminModelIntoAdminDto(savedAdmin);
    }

    @Override
    public List<FutsalListDto> getFutsalRegistrationRequestList() {
        List<FutsalModel> registeredFutsal=futsalRepo.findAll().stream().filter(futsal->!futsal.isRegistered()).collect(Collectors.toList());
        return FutsalListMapper.INSTANCE.futsalModelListIntoFutsalListDtoList(registeredFutsal);
    }

    @Override
    public List<FutsalListDto> getFutsalRegistredList() {
        List<FutsalModel> registeredFutsal=futsalRepo.findAll().stream().filter(FutsalModel::isRegistered).collect(Collectors.toList());
        return FutsalListMapper.INSTANCE.futsalModelListIntoFutsalListDtoList(registeredFutsal);
    }

    @Override
    public FutsalListDto approveFutsalRegistrationRequest(UUID futsalId) {
        FutsalModel futsalRequest = futsalRepo.findById(futsalId).orElseThrow(() -> new ResourceNotFoundException("Futsal Registration Request not found"));
        futsalRequest.setRegistered(true);
        return FutsalListMapper.INSTANCE.futsalModelIntoFutsalListDto(futsalRequest);
    }

    @Override
    public void rejectFutsalRegistrationRequest(UUID futsalId) {
        FutsalModel futsalRequest = futsalRepo.findById(futsalId).orElseThrow(() -> new ResourceNotFoundException("Futsal Registration Request not found"));
        futsalRepo.delete(futsalRequest);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserModel> userList=userRepo.findAll();
        return UserMapper.INSTANCE.userModelListIntoUserDtoList(userList);
    }

    @Override
    public void removeFutsal(UUID futsalId) {
       FutsalModel futsal=futsalRepo.findById(futsalId).orElseThrow(()->new ResourceNotFoundException("Futsal not found"));
       futsalRepo.delete(futsal);
    }

    @Override
    public List<FutsalListDto> getFutsalListOfFutsalOwner(UUID futsalOwnerId) {
        FutsalOwnerModel futsalOwner=futsalOwnerRepo.findById(futsalOwnerId).orElseThrow(()->new ResourceNotFoundException("Futsal Owner Not Found"));
        List<FutsalModel> futsalListOfOwner= futsalOwner.getFutsals();
        return FutsalListMapper.INSTANCE.futsalModelListIntoFutsalListDtoList(futsalListOfOwner);
    }

    @Override
    public List<FutsalOwnerDto> getFutsalOwnerList() {
        List<FutsalOwnerModel> futsalOwnerList=futsalOwnerRepo.findAll();
        return FutsalOwnerMapper.INSTANCE.futsalOwnerListIntoFutsalOwnerDtoList(futsalOwnerList);
    }
}
