package com.intern.futsalBookingSystem.service.serviceImpl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.intern.futsalBookingSystem.payload.AuthenticationResponse;
import com.intern.futsalBookingSystem.payload.SignInModel;
import com.intern.futsalBookingSystem.security.JwtService;
import com.intern.futsalBookingSystem.service.AdminService;
import com.intern.futsalBookingSystem.service.AwsService;
import com.intern.futsalBookingSystem.token.AdminToken;
import com.intern.futsalBookingSystem.token.AdminTokenRepo;
import com.intern.futsalBookingSystem.token.TokenType;
import com.intern.futsalBookingSystem.token.UserToken;
import com.intern.futsalBookingSystem.utils.MailUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Autowired
    private MailUtils mailUtils;

    @Autowired
    private AwsService awsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AdminTokenRepo adminTokenRepo;

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Override
    public AdminDto signUp(String admin, MultipartFile photo) throws IOException {

        AdminModel adminModel= objectMapper.readValue(admin,AdminModel.class);
        adminModel.setPhoto(awsService.uploadPhotoIntoAws(photo));
        AdminModel savedAdmin=adminRepo.save(adminModel);
        logger.info("Admin is signed up successfully");
        savedAdmin.setPhoto(awsService.getPhotoFromAws(savedAdmin.getPhoto()));
        logger.info("Extracted admin photo from aws server successfully");
        String jwtToken = jwtService.generateToken(adminModel);
        System.out.println(jwtToken);
        var refreshToken = jwtService.generateRefreshToken(adminModel);
        return AdminMapper.INSTANCE.adminModelIntoAdminDto(savedAdmin);

    }

    @Override
    public List<FutsalListDto> getFutsalRegistrationRequestList() {

        List<FutsalModel> registeredFutsal=futsalRepo.findAll().stream().filter(futsal->!futsal.isRegistered()).collect(Collectors.toList());
        logger.info("Futsal registration request list extracted from database successfully");
        registeredFutsal=awsService.setFutsalPhotoIntoPresignUrl(registeredFutsal);
        logger.info("Extracted respective photos of futsals from aws server successfully");
        return FutsalListMapper.INSTANCE.futsalModelListIntoFutsalListDtoList(registeredFutsal);

    }

    @Override
    public List<FutsalListDto> getFutsalRegistredList() {

        List<FutsalModel> registeredFutsal=futsalRepo.findAll().stream().filter(FutsalModel::isRegistered).collect(Collectors.toList());
        logger.info("Registered futsal list extracted from database successfully.");
        registeredFutsal=awsService.setFutsalPhotoIntoPresignUrl(registeredFutsal);
        logger.info("Extracted respective photos of futsals from aws server successfully");
        return FutsalListMapper.INSTANCE.futsalModelListIntoFutsalListDtoList(registeredFutsal);

    }

    @Override
    public FutsalListDto approveFutsalRegistrationRequest(UUID futsalId) {
        FutsalModel futsalRequest = futsalRepo.findById(futsalId).orElseThrow(() -> new ResourceNotFoundException("Futsal Registration Request not found"));
        futsalRequest.setRegistered(true);
        FutsalOwnerModel futsalOwner=futsalOwnerRepo.findByFutsals_Id(futsalId).orElseThrow(()->new ResourceNotFoundException("Futsal owner not found"));
        mailUtils.sendEmail(futsalOwner.getGmail(),"Futsal Booking System",futsalOwner.getFirstName()+" "+futsalOwner.getLastName()+" your futsal registration request has been approved by our company");
        logger.info("Email send successfully");
        FutsalModel savedFutsal=futsalRepo.save(futsalRequest);
        savedFutsal.setPhoto(awsService.getPhotoFromAws(savedFutsal.getPhoto()));
        logger.info("Extracted futsal photo from aws successfully");
        return FutsalListMapper.INSTANCE.futsalModelIntoFutsalListDto(savedFutsal);
    }

    @Override
    public void rejectFutsalRegistrationRequest(UUID futsalId) {

        FutsalModel futsalRequest = futsalRepo.findById(futsalId).orElseThrow(() -> new ResourceNotFoundException("Futsal Registration Request not found"));
        awsService.deletePhotoInAwsServer(futsalRequest.getPhoto());
        logger.info("Futsal photo has been deleted from aws server successfully");
        futsalRepo.delete(futsalRequest);
        logger.info("Futsal registration request is rejected");
        logger.info("Futsal registration request is deleted from database successfully.");
        FutsalOwnerModel futsalOwner=futsalOwnerRepo.findByFutsals_Id(futsalId).orElseThrow(()->new ResourceNotFoundException("Futsal owner not found"));
        mailUtils.sendEmail(futsalOwner.getGmail(),"Futsal Booking System",futsalOwner.getFirstName()+" "+futsalOwner.getLastName()+" your futsal registration request has been rejected by our company");
        logger.info("Email send successfully");


    }

    @Override
    public List<UserListDto> getAllUsers() {

        List<UserModel> userList=userRepo.findAll();
        logger.info("User list extracted from database successfully.");
        userList=awsService.setUserPhotoIntoUrl(userList);
        logger.info("Extracted respective photos of users from aws server successfully");
        return UserMapper.INSTANCE.userModelListIntoUserListDtoList(userList);

    }

    @Override
    public void removeFutsal(UUID futsalId) {

       FutsalModel futsal=futsalRepo.findById(futsalId).orElseThrow(()->new ResourceNotFoundException("Futsal not found"));
      // awsService.deletePhotoInAwsServer(futsal.getPhoto());
       logger.info("Futsal photo has been deleted from aws server successfully");
       futsalRepo.delete(futsal);
       logger.info("Futsal is removed from database successfully");

    }

    @Override
    public List<FutsalListDto> getFutsalListOfFutsalOwner(UUID futsalOwnerId) {

        FutsalOwnerModel futsalOwner=futsalOwnerRepo.findById(futsalOwnerId).orElseThrow(()->new ResourceNotFoundException("Futsal Owner Not Found"));
        logger.info("FutsalOwner found by id successfully");
        List<FutsalModel> futsalListOfOwner= futsalOwner.getFutsals();
        logger.info("FutsalOwner's futsal list extracted from database successfully");
        futsalListOfOwner=awsService.setFutsalPhotoIntoPresignUrl(futsalListOfOwner);
        logger.info("Extracted respective photo of futsal from aws server successfully");
        return FutsalListMapper.INSTANCE.futsalModelListIntoFutsalListDtoList(futsalListOfOwner);

    }

    @Override
    public List<FutsalOwnerDto> getFutsalOwnerList() {

        List<FutsalOwnerModel> futsalOwnerList = futsalOwnerRepo.findAll();
        logger.info("FutsalOwner list extracted from database successfully");
        futsalOwnerList=awsService.setFutsalOwnerPhotoIntoUrl(futsalOwnerList);
        return FutsalOwnerMapper.INSTANCE.futsalOwnerListIntoFutsalOwnerDtoList(futsalOwnerList);

    }

    @Override
    public AdminDto signIn(SignInModel signInModel) {
        AdminModel admin=adminRepo.findByUsernameAndPassword(signInModel.getUsername(),signInModel.getPassword()).orElseThrow(()->new ResourceNotFoundException("Admin not found"));
        admin.setPhoto(awsService.getPhotoFromAws(admin.getPhoto()));
        return AdminMapper.INSTANCE.adminModelIntoAdminDto(admin);
    }

    @Override
    public AuthenticationResponse authenticate(SignInModel request) {
        AdminModel admin = adminRepo.findByUsername(request.getUsername()).orElseThrow(()->new ResourceNotFoundException("Admin not Found"));
        String jwtToken = jwtService.generateToken(admin);
        String refreshToken = jwtService.generateRefreshToken(admin);
        saveUserToken(admin,jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
    private void saveUserToken(AdminModel user, String jwtToken) {
        var token = AdminToken.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        adminTokenRepo.save(token);
    }



}
