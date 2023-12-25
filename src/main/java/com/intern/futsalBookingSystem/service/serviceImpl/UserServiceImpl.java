package com.intern.futsalBookingSystem.service.serviceImpl;

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
import com.intern.futsalBookingSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public UserDto signUpUser(UserDto userDto) {

        UserModel user= UserMapper.INSTANCE.userDtoIntoUserModel(userDto);
        UserModel savedUser=userRepo.save(user);
        return UserMapper.INSTANCE.userModelIntoUserDto(savedUser);

    }

    @Override
    public List<FutsalListDto> getFutsalList() {

        List<FutsalModel> futsalList=futsalRepo.findAll().stream().filter(FutsalModel::isRegistered).collect(Collectors.toList());
        List<FutsalListDto> futsalListDto= FutsalListMapper.INSTANCE.futsalModelListIntoFutsalListDtoList(futsalList);
        return futsalListDto;

    }

    @Override
    public List<SlotsListDto> getAllSlotsOfFutsal(UUID futsalId) {

        FutsalModel futsal=futsalRepo.getFutsalById(futsalId).orElseThrow(()->new ResourceNotFoundException("Futsal Not found"));
        List<SlotModel> slotList=futsal.getSlots();
        return SlotsListMapper.INSTANCE.slotModelListIntoSlotListDto(slotList);

    }

    @Override
    public List<SlotsListDto> getAvailableSLotsOfFutsal(UUID futsalId) {

        FutsalModel futsal=futsalRepo.getFutsalById(futsalId).orElseThrow(()->new ResourceNotFoundException("Futsal Not Found"));
        List<SlotModel> slotList=futsal.getSlots().stream().filter(slot -> !slot.isBooked()).toList();
        return SlotsListMapper.INSTANCE.slotModelListIntoSlotListDto(slotList);

    }

    @Override
    public List<SlotsListDto> getUnavailableSlotsOfFutsal(UUID futsalId) {

        FutsalModel futsal=futsalRepo.getFutsalById(futsalId).orElseThrow(()->new ResourceNotFoundException("Futsal Not Found"));
        List<SlotModel> slotList=futsal.getSlots().stream().filter(slot -> slot.isBooked()).toList();
        return SlotsListMapper.INSTANCE.slotModelListIntoSlotListDto(slotList);

    }

    @Override
    public SlotDto bookingFutsalSLots(UUID userId, UUID slotId) {

        SlotModel slot = slotRepo.getSlotById(slotId).orElseThrow(()->new ResourceNotFoundException("Slot not found"));
        if (slot.isBooked()) {
            throw new ResourceNotFoundException("Slot is already booked");
        }

        UserModel user = userRepo.getUserById(userId).orElseThrow(()->new ResourceNotFoundException("User NOt Found"));
        slot.setBookedByUser(user);
        slot.setBooked(true);
        return SlotMapper.INSTANCE.slotModelIntoSlotDto(slotRepo.save(slot));

    }

    @Override
    public SlotDto cancelBooking(UUID slotId) {

        SlotModel slot = slotRepo.getSlotById(slotId).orElseThrow(()->new ResourceNotFoundException("Slot not found"));
        if (slot.isBooked()) {
            throw new ResourceNotFoundException("Slot is already booked");
        }

        slot.setBookedByUser(null);
        slot.setBooked(false);
        return SlotMapper.INSTANCE.slotModelIntoSlotDto(slotRepo.save(slot));

    }

    @Override
    public List<SlotsListDto> getOwnBookings(UUID userId) {
        List<SlotModel> slots=slotRepo.findAllByBookedByUserId(userId);
        return SlotsListMapper.INSTANCE.slotModelListIntoSlotListDto(slots);
    }


}
