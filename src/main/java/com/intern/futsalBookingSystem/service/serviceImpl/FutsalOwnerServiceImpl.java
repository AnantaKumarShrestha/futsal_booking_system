package com.intern.futsalBookingSystem.service.serviceImpl;

import com.intern.futsalBookingSystem.db.*;
import com.intern.futsalBookingSystem.dto.FutsalDto;
import com.intern.futsalBookingSystem.dto.FutsalListDto;
import com.intern.futsalBookingSystem.dto.FutsalOwnerDto;
import com.intern.futsalBookingSystem.dto.SlotDto;
import com.intern.futsalBookingSystem.exception.ResourceNotFoundException;
import com.intern.futsalBookingSystem.mapper.FutsalListMapper;
import com.intern.futsalBookingSystem.mapper.FutsalMapper;
import com.intern.futsalBookingSystem.mapper.FutsalOwnerMapper;
import com.intern.futsalBookingSystem.mapper.SlotMapper;
import com.intern.futsalBookingSystem.model.*;
import com.intern.futsalBookingSystem.payload.SlotRequest;
import com.intern.futsalBookingSystem.payload.TurnOverStats;
import com.intern.futsalBookingSystem.service.FutsalOwnerService;
import com.intern.futsalBookingSystem.utils.EmailWithAttachment;
import com.intern.futsalBookingSystem.utils.PaymentPDF;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
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

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailWithAttachment emailWithAttachment;

    @Autowired
    private PaymentPDF paymentPDF;

    @Autowired
    private InvoiceRepo invoiceRepo;

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
    public SlotDto completeBooking(UUID slotId) throws IOException {
        SlotModel slot = slotRepo.getSlotById(slotId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!slot.isCompleted()) {
            slot.setCompleted(true);

            UserModel bookedByUser = slot.getBookedByUser();

            slotRepo.save(slot);
            if (bookedByUser != null) {

                String userEmail = bookedByUser.getEmail();

               FutsalModel futsal=futsalRepo.getFutsalBySlots_Id(slotId);

                InvoiceModel invoiceModel=new InvoiceModel();
                invoiceModel.setFutsalId(futsal.getId());
                invoiceModel.setCustomerName(slot.getBookedByUser().getFirstName()+" "+slot.getBookedByUser().getLastName());
                invoiceModel.setPrice((int) slot.getPrice());
                invoiceModel.setGameStartTime(String.valueOf(slot.getStartTime()));
                invoiceModel.setGameEndTime(String.valueOf(slot.getEndTime()));

                InvoiceModel savedInvoice=invoiceRepo.save(invoiceModel);
                String invoiceId= String.valueOf(savedInvoice.getInvoiceId());
                String startTime= String.valueOf(slot.getStartTime());
                String endTime= String.valueOf(slot.getEndTime());
                String price= String.valueOf(slot.getPrice());
                String sslotId= String.valueOf(slot.getId());


                byte[] bytes=paymentPDF.asByteInvoice(futsal.getFutsalName(),futsal.getFutsalLocation(),invoiceId ,invoiceModel.getCustomerName(),sslotId,startTime,endTime,price);
                emailWithAttachment.sendEmailWithAttachment(userEmail,"Invoice","Bill",bytes);

                int currentPoint=slot.getBookedByUser().getRewardPoint();
                int rewardPoint=calculateRewardPoints(slot.getStartTime(),slot.getEndTime());
                currentPoint+=rewardPoint;
                logger.info("Reward point calculated successfully");
                UserModel user=slot.getBookedByUser();
                user.setRewardPoint(currentPoint);
                System.out.println(currentPoint);
                userRepo.save(user);
                slot.setBookedByUser(user);
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

    @Override
    public void invoiceExcelFile(UUID futsalId) {

        List<InvoiceModel> data = invoiceRepo.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("YourEntityData");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Date");
            headerRow.createCell(2).setCellValue("Customer Name");
            headerRow.createCell(3).setCellValue("Price");

            // Fill data rows
            int rowNum = 1;
            for (InvoiceModel invoiceData : data) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(String.valueOf(invoiceData.getInvoiceId()));
                row.createCell(1).setCellValue(invoiceData.getDate());
                row.createCell(2).setCellValue(invoiceData.getCustomerName());
                row.createCell(3).setCellValue(invoiceData.getPrice());
                // Add more cells as needed for other fields
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            // Write the workbook to the ByteArrayOutputStream
            workbook.write(byteArrayOutputStream);

            // Get the bytes
            byte[] excelBytes = byteArrayOutputStream.toByteArray();

        //    emailWithAttachment.sendEmailWithAttachment();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private int calculateRewardPoints(LocalDateTime startTime, LocalDateTime endTime) {

        Duration duration = Duration.between(startTime, endTime);
        long minutes = duration.toMinutes();

        int rewardPointsPerInterval = 1;
        return (int) (minutes / 10) * rewardPointsPerInterval;
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
