package com.intern.futsalBookingSystem.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intern.futsalBookingSystem.db.*;
import com.intern.futsalBookingSystem.dto.*;
import com.intern.futsalBookingSystem.exception.ResourceNotFoundException;
import com.intern.futsalBookingSystem.mapper.*;
import com.intern.futsalBookingSystem.model.*;
import com.intern.futsalBookingSystem.payload.AuthenticationResponse;
import com.intern.futsalBookingSystem.payload.SignInModel;
import com.intern.futsalBookingSystem.payload.SlotRequest;
import com.intern.futsalBookingSystem.payload.TurnOverStats;
import com.intern.futsalBookingSystem.security.JwtService;
import com.intern.futsalBookingSystem.service.FutsalOwnerService;
import com.intern.futsalBookingSystem.token.FutsalOwnerToken;
import com.intern.futsalBookingSystem.token.FutsalOwnerTokenRepo;
import com.intern.futsalBookingSystem.token.TokenType;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
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

    @Autowired
    private AwsServiceImpl awsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private FutsalOwnerTokenRepo futsalOwnerTokenRepo;

    private static final Logger logger = LoggerFactory.getLogger(FutsalOwnerServiceImpl.class);
    @Override
    public FutsalOwnerDto signUpFutsalOwner(String futsalOwner, MultipartFile photo) throws IOException {
        FutsalOwnerModel futsalOwnerModel=objectMapper.readValue(futsalOwner,FutsalOwnerModel.class);
        futsalOwnerModel.setPhoto(awsService.uploadPhotoIntoAws(photo));
        FutsalOwnerModel savedFutsalOwner=futsalOwnerRepo.save(futsalOwnerModel);
        logger.info("Futsal Owner Signed Up successfully.");
        savedFutsalOwner.setPhoto(awsService.getPhotoFromAws(savedFutsalOwner.getPhoto()));
        logger.info("Extracted futsal owner photo from aws server successfully");
        return FutsalOwnerMapper.INSTANCE.futsalOwnerDtoIntoFutsalOwnerModel(savedFutsalOwner);
    }

    @Override
    public FutsalListDto futsalRegistrationRequestToAdmin(UUID futsalOwnerId, String futsal,MultipartFile photo) throws IOException {
        FutsalOwnerModel futsalOwner =futsalOwnerRepo.getFutsalOwnerById(futsalOwnerId).orElseThrow(()->new ResourceNotFoundException("Futsal owner Not Found"));
        FutsalModel futsalModel =objectMapper.readValue(futsal,FutsalModel.class);
        futsalModel.setPhoto(awsService.uploadPhotoIntoAws(photo));
        futsalOwner.getFutsals().add(futsalModel);
        futsalOwnerRepo.save(futsalOwner);
        logger.info("Futsal registration request send to admin successfully");
        futsalModel.setPhoto(awsService.getPhotoFromAws(futsalModel.getPhoto()));
        logger.info("Extracted futsal photo from aws server successfully");
        return FutsalListMapper.INSTANCE.futsalModelIntoFutsalListDto(futsalModel);
    }

    @Override
    public List<SlotDto> createFutsalSLots(UUID futsalId, SlotRequest slotRequest) {
        FutsalModel futsal = futsalRepo.getFutsalById(futsalId).orElseThrow(()->new ResourceNotFoundException("Futsal Id not found"));
        if(!futsal.isRegistered()){
            logger.error("Futsal not registered (futsal id : {})Status : Fail",futsalId);
            throw new ResourceNotFoundException("Futsal Not Registered");
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
    @Transactional
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
                LocalDate date= LocalDate.now();
                invoiceModel.setDate(String.valueOf(date));

                InvoiceModel savedInvoice=invoiceRepo.save(invoiceModel);
                String invoiceId= String.valueOf(savedInvoice.getInvoiceId());
                String startTime= String.valueOf(slot.getStartTime());
                String endTime= String.valueOf(slot.getEndTime());
                String price= String.valueOf(slot.getPrice());
                String sslotId= String.valueOf(slot.getId());



                byte[] bytes=paymentPDF.asByteInvoice(futsal.getFutsalName(),futsal.getFutsalLocation(),invoiceId ,invoiceModel.getCustomerName(),sslotId,startTime,endTime,price);
                emailWithAttachment.sendEmailWithAttachment(userEmail,"Invoice","Bill",bytes,"invoice.pdf");

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
    public List<InvoiceDto> invoiceExcelFile(UUID futsalId) {

        FutsalOwnerModel futsalOwner=futsalOwnerRepo.findByFutsals_Id(futsalId).orElseThrow(()->new ResourceNotFoundException("Futsal Not Found"));
        List<InvoiceModel> data = invoiceRepo.findAllByFutsalId(futsalId);
        String subject="Futsal Statement";
        String message="";

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");


            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("SN");
            headerRow.createCell(1).setCellValue("ID");
            headerRow.createCell(2).setCellValue("Date");
            headerRow.createCell(3).setCellValue("Customer Name");
            headerRow.createCell(4).setCellValue("Price");


            int rowNum = 1;
            for (InvoiceModel invoiceData : data) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum-1);
                row.createCell(1).setCellValue(String.valueOf(invoiceData.getInvoiceId()));
                row.createCell(2).setCellValue(invoiceData.getDate());
                row.createCell(3).setCellValue(invoiceData.getCustomerName());
                row.createCell(4).setCellValue(invoiceData.getPrice());

            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            workbook.write(byteArrayOutputStream);


            byte[] excelBytes = byteArrayOutputStream.toByteArray();

            emailWithAttachment.sendEmailWithAttachment(futsalOwner.getGmail(),subject,message,excelBytes,"Futsal_Statement.xlsx");


        } catch (IOException e) {
            e.printStackTrace();
        }

        return InvoiceMapper.INSTANCE.invoiceModelListIntoInvoice(data);

    }

    @Override
    public FutsalOwnerDto SignIn(SignInModel signInModel) {
        FutsalOwnerModel futsalOwner=futsalOwnerRepo.findByUsernameAndPassword(signInModel.getUsername(),signInModel.getPassword()).orElseThrow(()->new ResourceNotFoundException("Futsal owner not found"));
        futsalOwner.setPhoto(awsService.getPhotoFromAws(futsalOwner.getPhoto()));
        return FutsalOwnerMapper.INSTANCE.futsalOwnerDtoIntoFutsalOwnerModel(futsalOwner);
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

    @Override
    public AuthenticationResponse authenticate(SignInModel request) {
        FutsalOwnerModel futsalOwner = futsalOwnerRepo.findByUsername(request.getUsername()).orElseThrow(()->new ResourceNotFoundException("Futsal Owner not Found"));
        String jwtToken = jwtService.generateToken(futsalOwner);
        String refreshToken = jwtService.generateRefreshToken(futsalOwner);
        saveUserToken(futsalOwner,jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(FutsalOwnerModel user, String jwtToken) {
        var token = FutsalOwnerToken.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        futsalOwnerTokenRepo.save(token);
    }


}
