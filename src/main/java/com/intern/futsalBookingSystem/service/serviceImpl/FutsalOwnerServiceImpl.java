package com.intern.futsalBookingSystem.service.serviceImpl;

import com.intern.futsalBookingSystem.db.FutsalOwnerRepo;
import com.intern.futsalBookingSystem.db.FutsalRepo;
import com.intern.futsalBookingSystem.db.SlotRepo;
import com.intern.futsalBookingSystem.dto.FutsalDto;
import com.intern.futsalBookingSystem.dto.FutsalListDto;
import com.intern.futsalBookingSystem.dto.FutsalOwnerDto;
import com.intern.futsalBookingSystem.dto.SlotDto;
import com.intern.futsalBookingSystem.exception.ResourceNotFoundException;
import com.intern.futsalBookingSystem.mapper.FutsalListMapper;
import com.intern.futsalBookingSystem.mapper.FutsalMapper;
import com.intern.futsalBookingSystem.mapper.FutsalOwnerMapper;
import com.intern.futsalBookingSystem.mapper.SlotMapper;
import com.intern.futsalBookingSystem.model.FutsalModel;
import com.intern.futsalBookingSystem.model.FutsalOwnerModel;
import com.intern.futsalBookingSystem.model.SlotModel;
import com.intern.futsalBookingSystem.model.UserModel;
import com.intern.futsalBookingSystem.payload.SlotRequest;
import com.intern.futsalBookingSystem.payload.TurnOverStats;
import com.intern.futsalBookingSystem.service.FutsalOwnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FutsalOwnerServiceImpl implements FutsalOwnerService {

    @Autowired
    private FutsalOwnerRepo futsalOwnerRepo;

    @Autowired
    private FutsalRepo futsalRepo;

    @Autowired
    private SlotRepo slotRepo;

    private static final Logger logger = LoggerFactory.getLogger(FutsalOwnerServiceImpl.class);
    @Override
    public FutsalOwnerDto signUpFutsalOwner(FutsalOwnerDto futsalOwnerDto) {
        FutsalOwnerModel futsalOwner=FutsalOwnerMapper.INSTANCE.futsalOwnerModelIntoFutsalOwnerDto(futsalOwnerDto);
        FutsalOwnerModel savedFutsalOwner=futsalOwnerRepo.save(futsalOwner);
        logger.info("Futsal Owner Signed Up successfully.");
        return FutsalOwnerMapper.INSTANCE.futsalOwnerDtoIntoFutsalOwnerModel(savedFutsalOwner);
    }

    @Override
    public FutsalListDto futsalRegistrationRequestToAdmin(UUID futsalOwnerId, FutsalListDto futsalDto) {
        FutsalOwnerModel futsalOwner =futsalOwnerRepo.getFutsalOwnerById(futsalOwnerId).orElseThrow(()->new ResourceNotFoundException("Futsal Not Found"));
        FutsalModel futsal =FutsalListMapper.INSTANCE.futsalListDtoIntoFutsalModel(futsalDto);
        futsalOwner.getFutsals().add(futsal);
        futsalOwnerRepo.save(futsalOwner);
        logger.info("Futsal registration request send to admin successfully");
        return FutsalListMapper.INSTANCE.futsalModelIntoFutsalListDto(futsal);
    }

    @Override
    public List<SlotDto> createFutsalSLots(UUID futsalId, SlotRequest slotRequest) {
        FutsalModel futsal = futsalRepo.getFutsalById(futsalId).orElseThrow(()->new ResourceNotFoundException("Futsal Id not found"));
        if(!futsal.isRegistered()){
            logger.error("Futsal not registered (futsal id : {})Status : Fail",futsalId);
            throw new ResourceNotFoundException("Not Registered");
        }

        LocalDateTime startTime = slotRequest.getStartTime();
        int numberOfSlots = slotRequest.getNumberOfSlots();
        int slotDurationMinutes = slotRequest.getSlotDurationMinutes();
        double slotPrice = slotRequest.getSlotPrice();

        List<SlotModel> slotList=new ArrayList<>();
        for (int i = 0; i < numberOfSlots; i++) {
            SlotModel slot = new SlotModel();
            slot.setStartTime(startTime);
            slot.setEndTime(startTime.plusMinutes(slotDurationMinutes));
            slot.setPrice(slotPrice);

            slotList.add(slot);

            startTime = startTime.plusMinutes(slotDurationMinutes);
        }
        futsal.getSlots().addAll(slotList);
        futsalRepo.save(futsal);
        logger.info("Futsal game slots created successfully.");
        return SlotMapper.INSTANCE.slotModelListIntoSlotDtoList(slotList);
    }

    @Override
    public TurnOverStats getTurnOverStats(UUID futsalOwnerId, UUID futsalId) {
        FutsalOwnerModel futsalOwner = futsalOwnerRepo.getFutsalOwnerById(futsalOwnerId).orElseThrow(() -> new ResourceNotFoundException("Futsal Owner not found"));
        FutsalModel futsal=futsalRepo.findById(futsalId).orElseThrow(()->new ResourceNotFoundException("Futsal Not Found Exception"));

        if(!futsal.isRegistered()){
            logger.error("Futsal not registered (futsal id : {})Status : Fail",futsalId);
            throw new ResourceNotFoundException("Not Registered");
        }

        LocalDateTime now = LocalDateTime.now();

        double dailyTurnover = calculateTurnoverForPeriod(futsalId, now.minusDays(1), now);
        logger.info("Futsal Daily turnover calculated successfully.");
        double weeklyTurnover = calculateTurnoverForPeriod(futsalId, now.minusWeeks(1), now);
        logger.info("Futsal weekly turnover calculated successfully");
        double monthlyTurnover = calculateTurnoverForPeriod(futsalId, now.minusMonths(1), now);
        logger.info("Futsal monthly turnover calculated successfully");
        return new TurnOverStats(dailyTurnover, weeklyTurnover, monthlyTurnover);
    }

    @Override
    public List<SlotDto> getOwnFutsalSlots(UUID futsalId) {

        FutsalModel futsal=futsalRepo.getFutsalById(futsalId).orElseThrow(()->new ResourceNotFoundException("Futsal Not found"));
        if(!futsal.isRegistered()){
            logger.error("Futsal not registered (futsal id : {})Status : Fail",futsalId);
            throw new ResourceNotFoundException("Not Registered");
        }
        List<SlotModel> slotList=futsal.getSlots();
        logger.info("Futsal list extracted from database successfully");
        return SlotMapper.INSTANCE.slotModelListIntoSlotDtoList(slotList);
    }

    @Override
    public SlotDto cancelBooking(UUID slotID) {
        SlotModel slot = slotRepo.getSlotById(slotID).orElseThrow(()->new ResourceNotFoundException("Slot not found"));
        if (!slot.isBooked()) {
            logger.error("Futsal is not booked (Slot id :{}) Status : Fail",slotID);
            throw new ResourceNotFoundException("Slot is not booked");
        }
        slot.setBookedByUser(null);
        slot.setBooked(false);
        logger.info("Futsal booking canceled successfully");
        return SlotMapper.INSTANCE.slotModelIntoSlotDto(slotRepo.save(slot));
    }

    @Override
    public SlotDto completeBooking(UUID slotId) {
        SlotModel slot = slotRepo.getSlotById(slotId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!slot.isCompleted()) {
            slot.setCompleted(true);

            UserModel bookedByUser = slot.getBookedByUser();

            slotRepo.save(slot);
            if (bookedByUser != null) {

                String userEmail = bookedByUser.getEmail();


//                UserModel user=slot.getBookedByUser();
//                int reward=user.getRewardPoint();
//                reward+=5;
//                user.setRewardPoint(reward);
//                userRepo.save(user);

                return SlotMapper.INSTANCE.slotModelIntoSlotDto(slotRepo.save(slot));
            } else {
                logger.error("User not found Status :Fail");
                throw new ResourceNotFoundException("No user found for the booked slot");
            }
        } else {
            logger.error("Booking is already marked as completed Status :Fail");
            throw new ResourceNotFoundException("Booking is already marked as completed");
        }

    }

    private double calculateTurnoverForPeriod(UUID futsalId, LocalDateTime start, LocalDateTime end) {
        List<SlotModel> slots = futsalRepo.findCompletedAndBookedSlotsForFutsal(futsalId,start,end);
        double totalTurnover=0.0d;
        for (SlotModel slot : slots) {
            totalTurnover +=slot.getPrice();
        }
        return totalTurnover;
    }


}
