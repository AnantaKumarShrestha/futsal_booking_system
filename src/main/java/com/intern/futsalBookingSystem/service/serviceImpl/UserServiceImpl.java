package com.intern.futsalBookingSystem.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intern.futsalBookingSystem.db.FutsalRepo;
import com.intern.futsalBookingSystem.db.SlotRepo;
import com.intern.futsalBookingSystem.db.UserRepo;
import com.intern.futsalBookingSystem.dto.*;
import com.intern.futsalBookingSystem.exception.ResourceNotFoundException;
import com.intern.futsalBookingSystem.mapper.FutsalListMapper;
import com.intern.futsalBookingSystem.mapper.SlotMapper;
import com.intern.futsalBookingSystem.mapper.SlotsListMapper;
import com.intern.futsalBookingSystem.mapper.UserMapper;
import com.intern.futsalBookingSystem.model.FutsalModel;
import com.intern.futsalBookingSystem.model.SlotModel;
import com.intern.futsalBookingSystem.model.UserModel;
import com.intern.futsalBookingSystem.payload.AuthenticationResponse;
import com.intern.futsalBookingSystem.payload.SignInModel;
import com.intern.futsalBookingSystem.security.JwtService;
import com.intern.futsalBookingSystem.service.AwsService;
import com.intern.futsalBookingSystem.service.UserService;
import com.intern.futsalBookingSystem.token.UserToken;
import com.intern.futsalBookingSystem.token.UserTokenRepository;
import com.intern.futsalBookingSystem.token.TokenType;
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
public class UserServiceImpl implements UserService {


    @Autowired
    private SlotRepo slotRepo;


    @Autowired
    private FutsalRepo futsalRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AwsService awsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Autowired
    private JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Transactional
    @Override
    public UserDto signUpUser(String user, MultipartFile file) throws IOException {

        UserModel userModel= objectMapper.readValue(user,UserModel.class);

        userModel.setPhoto(awsService.uploadPhotoIntoAws(file));
        UserModel savedUser=userRepo.save(userModel);
        var jwtToken = jwtService.generateToken(userModel);
        saveUserToken(savedUser, jwtToken);
        logger.info("User signed up successfully.");
        savedUser.setPhoto(awsService.getPhotoFromAws(savedUser.getPhoto()));
        logger.info("Extracted user photo from aws server successfully");

        System.out.println(jwtToken);
        System.out.println("ko");
        return UserMapper.INSTANCE.userModelIntoUserDto(userModel);

    }

    @Override
    public List<FutsalListDto> getFutsalList() {

        List<FutsalModel> futsalList=futsalRepo.findAll().stream().filter(FutsalModel::isRegistered).collect(Collectors.toList());
        futsalList=awsService.setFutsalPhotoIntoPresignUrl(futsalList);
        List<FutsalListDto> futsalListDto= FutsalListMapper.INSTANCE.futsalModelListIntoFutsalListDtoList(futsalList);
        logger.info("Futsal list extracted from database successfully.");
        return futsalListDto;

    }

    @Override
    public List<SlotsListDto> getAllSlotsOfFutsal(UUID futsalId) {

        FutsalModel futsal=futsalRepo.getFutsalById(futsalId).orElseThrow(()->new ResourceNotFoundException("Futsal Not found"));
        List<SlotModel> slotList=futsal.getSlots();
        logger.info("Futsal-game-Slots extracted successfully.");
        return SlotsListMapper.INSTANCE.slotModelListIntoSlotListDto(slotList);

    }

    @Override
    public List<SlotsListDto> getAvailableSLotsOfFutsal(UUID futsalId) {

        FutsalModel futsal=futsalRepo.getFutsalById(futsalId).orElseThrow(()->new ResourceNotFoundException("Futsal Not Found"));
        List<SlotModel> slotList=futsal.getSlots().stream().filter(slot -> !slot.isBooked()).toList();
        logger.info("Available futsal game slots extracted from database successfully");
        return SlotsListMapper.INSTANCE.slotModelListIntoSlotListDto(slotList);

    }

    @Override
    public List<SlotsListDto> getUnavailableSlotsOfFutsal(UUID futsalId) {

        FutsalModel futsal=futsalRepo.getFutsalById(futsalId).orElseThrow(()->new ResourceNotFoundException("Futsal Not Found"));
        List<SlotModel> slotList=futsal.getSlots().stream().filter(slot -> slot.isBooked()).toList();
        logger.info("Booked futsal slot extracted from database successfully");
        return SlotsListMapper.INSTANCE.slotModelListIntoSlotListDto(slotList);

    }

    @Override
    public SlotDto bookingFutsalSLots(UUID userId, UUID slotId) {

        SlotModel slot = slotRepo.getSlotById(slotId).orElseThrow(()->new ResourceNotFoundException("Slot not found"));
        if (slot.isBooked()) {
            logger.error("Slot is already booked Status : Fail");
            throw new ResourceNotFoundException("Slot is already booked");
        }

        UserModel user = userRepo.getUserById(userId).orElseThrow(()->new ResourceNotFoundException("User NOt Found"));
        slot.setBookedByUser(user);
        slot.setBooked(true);
        logger.info("Slot is booked by userId {}",userId);
        return SlotMapper.INSTANCE.slotModelIntoSlotDto(slotRepo.save(slot));

    }

    @Override
    public SlotDto cancelBooking(UUID slotId) {

        SlotModel slot = slotRepo.getSlotById(slotId).orElseThrow(()->new ResourceNotFoundException("Slot not found"));

        slot.setBookedByUser(null);
        slot.setBooked(false);
        logger.info("Futsal game slot canceled successfully.");
        return SlotMapper.INSTANCE.slotModelIntoSlotDto(slotRepo.save(slot));

    }

    @Override
    public List<SlotsListDto> getOwnBookings(UUID userId) {
        List<SlotModel> slots=slotRepo.findAllByBookedByUserId(userId);
        logger.info("Own futsal booking data are extracted from database successfully.");
        return SlotsListMapper.INSTANCE.slotModelListIntoSlotListDto(slots);
    }

    @Override
    public UserDto userSignIn(SignInModel signModel) {


        UserModel user=userRepo.findByEmailAndPassword(signModel.getUsername(), signModel.getPassword()).orElseThrow(()->new ResourceNotFoundException("User not found"));
        user.setPhoto(awsService.getPhotoFromAws(user.getPhoto()));
        return UserMapper.INSTANCE.userModelIntoUserDto(user);
    }

@Override
    public AuthenticationResponse authenticate(SignInModel request) {
        UserModel user = userRepo.findByEmail(request.getUsername())
                .orElseThrow(()->new ResourceNotFoundException("User not Found"));
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    private void saveUserToken(UserModel user, String jwtToken) {
        var token = UserToken.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        userTokenRepository.save(token);
    }

    private void revokeAllUserTokens(UserModel user) {
        var validUserTokens = userTokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        userTokenRepository.saveAll(validUserTokens);
    }




}
