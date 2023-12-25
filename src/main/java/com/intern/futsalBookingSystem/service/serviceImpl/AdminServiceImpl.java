package com.intern.futsalBookingSystem.service.serviceImpl;


import com.intern.futsalBookingSystem.db.AdminRepo;
import com.intern.futsalBookingSystem.db.FutsalOwnerRepo;
import com.intern.futsalBookingSystem.db.FutsalRepo;
import com.intern.futsalBookingSystem.db.UserRepo;
import com.intern.futsalBookingSystem.dto.*;
import com.intern.futsalBookingSystem.exception.ResourceNotFoundException;
import com.intern.futsalBookingSystem.mapper.*;
import com.intern.futsalBookingSystem.model.AdminModel;
import com.intern.futsalBookingSystem.model.FutsalModel;
import com.intern.futsalBookingSystem.model.FutsalOwnerModel;
import com.intern.futsalBookingSystem.model.UserModel;
import com.intern.futsalBookingSystem.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Override
    public AdminDto signUp(AdminDto adminDto) {

        AdminModel admin= AdminMapper.INSTANCE.adminDtoIntoAdminModel(adminDto);
        AdminModel savedAdmin=adminRepo.save(admin);
        logger.info("Admin is signed up successfully");
        return AdminMapper.INSTANCE.adminModelIntoAdminDto(savedAdmin);

    }

    @Override
    public List<FutsalListDto> getFutsalRegistrationRequestList() {

        List<FutsalModel> registeredFutsal=futsalRepo.findAll().stream().filter(futsal->!futsal.isRegistered()).collect(Collectors.toList());
        logger.info("Futsal registration request list extracted from database successfully");
        return FutsalListMapper.INSTANCE.futsalModelListIntoFutsalListDtoList(registeredFutsal);

    }

    @Override
    public List<FutsalListDto> getFutsalRegistredList() {

        List<FutsalModel> registeredFutsal=futsalRepo.findAll().stream().filter(FutsalModel::isRegistered).collect(Collectors.toList());
        logger.info("Registered futsal list extracted from database successfully.");
        return FutsalListMapper.INSTANCE.futsalModelListIntoFutsalListDtoList(registeredFutsal);

    }

    @Override
    public FutsalListDto approveFutsalRegistrationRequest(UUID futsalId) {
        FutsalModel futsalRequest = futsalRepo.findById(futsalId).orElseThrow(() -> new ResourceNotFoundException("Futsal Registration Request not found"));
        futsalRequest.setRegistered(true);
        return FutsalListMapper.INSTANCE.futsalModelIntoFutsalListDto(futsalRepo.save(futsalRequest));
    }

    @Override
    public void rejectFutsalRegistrationRequest(UUID futsalId) {

        FutsalModel futsalRequest = futsalRepo.findById(futsalId).orElseThrow(() -> new ResourceNotFoundException("Futsal Registration Request not found"));
        futsalRepo.delete(futsalRequest);
        logger.info("Futsal registration request is rejected");
        logger.info("Futsal registration request is deleted from database successfully.");

    }

    @Override
    public List<UserListDto> getAllUsers() {

        List<UserModel> userList=userRepo.findAll();
        logger.info("User list extracted from database successfully.");
        return UserMapper.INSTANCE.userModelListIntoUserListDtoList(userList);

    }

    @Override
    public void removeFutsal(UUID futsalId) {

       FutsalModel futsal=futsalRepo.findById(futsalId).orElseThrow(()->new ResourceNotFoundException("Futsal not found"));
       futsalRepo.delete(futsal);
       logger.info("Futsal is removed from database successfully");

    }

    @Override
    public List<FutsalListDto> getFutsalListOfFutsalOwner(UUID futsalOwnerId) {

        FutsalOwnerModel futsalOwner=futsalOwnerRepo.findById(futsalOwnerId).orElseThrow(()->new ResourceNotFoundException("Futsal Owner Not Found"));
        logger.info("FutsalOwner found by id successfully");
        List<FutsalModel> futsalListOfOwner= futsalOwner.getFutsals();
        logger.info("FutsalOwner's futsal list extracted from database successfully");
        return FutsalListMapper.INSTANCE.futsalModelListIntoFutsalListDtoList(futsalListOfOwner);

    }

    @Override
    public List<FutsalOwnerDto> getFutsalOwnerList() {

        List<FutsalOwnerModel> futsalOwnerList=futsalOwnerRepo.findAll();
        logger.info("FutsalOwner list extracted from database successfully");
        return FutsalOwnerMapper.INSTANCE.futsalOwnerListIntoFutsalOwnerDtoList(futsalOwnerList);

    }
}
