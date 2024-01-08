
package com.intern.futsalBookingSystem.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intern.futsalBookingSystem.db.AdminRepo;
import com.intern.futsalBookingSystem.db.FutsalOwnerRepo;
import com.intern.futsalBookingSystem.db.FutsalRepo;
import com.intern.futsalBookingSystem.db.UserRepo;
import com.intern.futsalBookingSystem.dto.*;
import com.intern.futsalBookingSystem.mapper.UserListMapper;
import com.intern.futsalBookingSystem.mapper.UserMapper;
import com.intern.futsalBookingSystem.model.*;
import com.intern.futsalBookingSystem.payload.SignInModel;
import com.intern.futsalBookingSystem.service.AwsService;
import com.intern.futsalBookingSystem.utils.MailUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminServiceImplTest {

    @InjectMocks
    private AdminServiceImpl adminService;

    @Mock
    private AdminRepo adminRepo;

    @Mock
    private FutsalRepo futsalRepo;

    @Mock
    private FutsalOwnerRepo futsalOwnerRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private MailUtils mailUtils;

    @Mock
    private AwsService awsService;

    @Mock
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void signUpTest() throws IOException {

        String adminData = "{'firstName': 'Gaurav', 'lastName': 'Kharel', 'username': 'gauravkharel123@gmail.com', 'password': 'admin', 'gmail': 'gauravkharel123@gmail.com', 'age':23,'phone':'9812121212'}";
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "file content".getBytes());
        AdminModel adminModel = new AdminModel(UUID.randomUUID(), "gaurav", "Kharel", "gauravkharel123@gmail.com", "admin", "gauravkharel123@gmail.com",23,"9812121212","profilePic");


        when(objectMapper.readValue(adminData, AdminModel.class)).thenReturn(adminModel);
        when(awsService.uploadPhotoIntoAws(file)).thenReturn("object_key");
        when(adminRepo.save(adminModel)).thenReturn(adminModel);
        when(awsService.getPhotoFromAws("object_key")).thenReturn("presignedUrl");


        AdminDto result = adminService.signUp(adminData, file);


        verify(objectMapper).readValue(adminData, AdminModel.class);
        verify(awsService).uploadPhotoIntoAws(file);
        verify(adminRepo).save(adminModel);
        verify(awsService).getPhotoFromAws("object_key");


        assertEquals("presignedUrl", result.getPhoto());
        assertEquals(adminModel.getFirstName(),result.getFirstName());
        assertEquals(adminModel.getLastName(),result.getLastName());
        assertEquals(adminModel.getGmail(),result.getGmail());
        assertEquals(adminModel.getPhoto(),result.getPhoto());
        assertEquals(adminModel.getAge(),result.getAge());
        assertEquals(adminModel.getPhone(),result.getPhone());
    }

    @Test
    public void getUserListTest(){

        AddressModel address1=new AddressModel(UUID.randomUUID(),"Bagmati","Chakrapat");


        List<UserModel> userList=new ArrayList<>();
        userList.add(new UserModel(UUID.randomUUID(),"Amish","Uprety",20,"amishuprety123@gmail.com","password","amishuprety123@gmail.com",address1,3,"profilepic.png"));
        userList.add(new UserModel(UUID.randomUUID(),"Ashish","Shrestha",20,"ashishshrestha123@gmail.com","password","ashishshrestha123@gmail.com",address1,6,"profilepic.png"));

        when(userRepo.findAll()).thenReturn(userList);
        when(awsService.setUserPhotoIntoUrl(userList)).thenReturn(userList);


        List<UserListDto> result = adminService.getAllUsers();


        verify(userRepo).findAll();
        verify(awsService).setUserPhotoIntoUrl(userList);


        assertNotNull(result);
        assertEquals(userList.size(), result.size());
    }


    @Test
    void getFutsalRegistrationRequestListTest() {

        List<FutsalModel> registeredFutsals = new ArrayList<>();
        registeredFutsals.add(new FutsalModel(UUID.randomUUID(), "Dukhu Futsal", "Baluwata,ktm", "Best Futsal around Baluwatar", null, false, "DukhuFutsal_Photo"));
        registeredFutsals.add(new FutsalModel(UUID.randomUUID(), "Tokha Futsal", "Tokha,Ktm", "Best Futsal around tokha", null, false, "TokhaFutsal_photo"));

        when(futsalRepo.findAll()).thenReturn(registeredFutsals);
        when(awsService.setFutsalPhotoIntoPresignUrl(registeredFutsals)).thenReturn(registeredFutsals);

        List<FutsalListDto> result = adminService.getFutsalRegistrationRequestList();

        verify(futsalRepo).findAll();
        verify(awsService).setFutsalPhotoIntoPresignUrl(registeredFutsals);

        assertEquals(2, result.size());
    }

    @Test
    void getFutsalRegistredListTest() {

        FutsalOwnerModel futsalOwner = new FutsalOwnerModel(UUID.randomUUID(), "Amish", "Upreti", "amishupreti123@gmail.com", "password", "amishupreti123gmail.com", "123456789", null, "profilepicr");

        List<FutsalModel> registeredFutsals = new ArrayList<>();
        registeredFutsals.add(new FutsalModel(UUID.randomUUID(), "Dukhu Futsal", "Baluwata,ktm", "Best Futsal around Baluwatar", futsalOwner, true, "DukhuFutsal_Photo"));
        registeredFutsals.add(new FutsalModel(UUID.randomUUID(), "Tokha Futsal", "Tokha,Ktm", "Best Futsal around tokha", futsalOwner, true, "TokhaFutsal_photo"));

        when(futsalRepo.findAll()).thenReturn(registeredFutsals);
        when(awsService.setFutsalPhotoIntoPresignUrl(registeredFutsals)).thenReturn(registeredFutsals);

        List<FutsalListDto> result = adminService.getFutsalRegistredList();

        verify(futsalRepo).findAll();
        verify(awsService).setFutsalPhotoIntoPresignUrl(registeredFutsals);

        assertEquals(2, result.size());
    }


    @Test
    void approveFutsalRegistrationRequestTest() {

        UUID futsalId = UUID.randomUUID();
        FutsalModel futsalRequest = new FutsalModel(futsalId, "Dukhu Futsal", "Baluwatar,ktm", "Best Futsal around baluwatar", null, false, "dukhufutsal_photo");
        FutsalOwnerModel futsalOwner = new FutsalOwnerModel(UUID.randomUUID(), "Amish", "Upreti", "amishupreti123@gmail.com", "password", "amishuprei123@gmail.com", "9812121212", null, "futsalownerpic");
        futsalRequest.setFutsalOwner(futsalOwner);

        when(futsalRepo.findById(futsalId)).thenReturn(java.util.Optional.of(futsalRequest));
        when(futsalOwnerRepo.findByFutsals_Id(futsalId)).thenReturn(java.util.Optional.of(futsalOwner));
        when(futsalRepo.save(futsalRequest)).thenReturn(futsalRequest);

        FutsalListDto result = adminService.approveFutsalRegistrationRequest(futsalId);

        verify(futsalRepo).findById(futsalId);
        verify(futsalOwnerRepo).findByFutsals_Id(futsalId);
        verify(futsalRepo).save(any(FutsalModel.class));
        verify(awsService).getPhotoFromAws("dukhufutsal_photo");

        assertEquals(futsalId, result.getId());
    }

    @Test
    void rejectFutsalRegistrationRequestTest() {

        UUID futsalId = UUID.randomUUID();
        FutsalModel futsalRequest = new FutsalModel(futsalId, "Dukhu Futsal", "Baluwatar,ktm", "Best Futsal around baluwatar", null, false, "dukhufutsal_photo");
        FutsalOwnerModel futsalOwner = new FutsalOwnerModel(UUID.randomUUID(), "Amish", "Upreti", "amishupreti123@gmail.com", "password", "amishuprei123@gmail.com", "9812121212", null, "futsalownerpic");
        futsalRequest.setFutsalOwner(futsalOwner);

        when(futsalRepo.findById(futsalId)).thenReturn(java.util.Optional.of(futsalRequest));
        when(futsalOwnerRepo.findByFutsals_Id(futsalId)).thenReturn(java.util.Optional.of(futsalOwner));

        adminService.rejectFutsalRegistrationRequest(futsalId);

        verify(futsalRepo).delete(futsalRequest);
    }

    @Test
    void signInTest() throws IOException {

        SignInModel signInModel = new SignInModel("gauravkharel123@gmail.com", "admin");
        AdminModel adminModel = new AdminModel(UUID.randomUUID(), "Gaurav", "Kharel", "gauravKharel123@gmail.com", "admin", "gauravKharel123@gmail.com",23,"9812121212","profilePic");

        when(adminRepo.findByUsernameAndPassword(signInModel.getUsername(), signInModel.getPassword())).thenReturn(java.util.Optional.of(adminModel));

        AdminDto result = adminService.signIn(signInModel);

        verify(adminRepo).findByUsernameAndPassword(signInModel.getUsername(), signInModel.getPassword());
        when(awsService.getPhotoFromAws("object_key")).thenReturn("presignedUrl");

        assertNotNull(result);
        assertEquals(adminModel.getId(), result.getId());
        assertEquals(adminModel.getFirstName(), result.getFirstName());
        assertEquals(adminModel.getLastName(), result.getLastName());
        assertEquals(adminModel.getGmail(),result.getGmail());
        assertEquals(adminModel.getAge(),result.getAge());
        assertEquals(adminModel.getPhone(),result.getPhone());

    }

    @Test
    void getFutsalOwnerListTest() throws IOException {

        List<FutsalOwnerModel> futsalOwnerList = Arrays.asList(
                new FutsalOwnerModel(UUID.randomUUID(), "Amish", "Upreti", "amsihupreti123@gmail.com", "password", "amishupreti123@gmail.com", "9812121212", null, "profilePic"),
                new FutsalOwnerModel(UUID.randomUUID(), "Ashish", "Shrestha", "ashishshrestha123@gmail.com", "password", "ashishshrestha123@gmail.com", "9812233445", null, "profilePic")
        );

        when(futsalOwnerRepo.findAll()).thenReturn(futsalOwnerList);
        when(awsService.setFutsalOwnerPhotoIntoUrl(futsalOwnerList)).thenReturn(futsalOwnerList);

        List<FutsalOwnerDto> result = adminService.getFutsalOwnerList();

        verify(futsalOwnerRepo).findAll();
        verify(awsService).setFutsalOwnerPhotoIntoUrl(futsalOwnerList);

        assertNotNull(result);
        assertEquals(futsalOwnerList.size(), result.size());
    }

    @Test
    void getFutsalListOfFutsalOwnerTest() throws IOException {


        UUID futsalOwnerId = UUID.randomUUID();
        FutsalOwnerModel futsalOwner = new FutsalOwnerModel(futsalOwnerId, "John", "Doe", "johnUsername", "johnPassword", "john@gmail.com", "123456789", null, "photoOwner1");
        List<FutsalModel> futsalList = Arrays.asList(
                new FutsalModel(UUID.randomUUID(), "Dukhu Futsal", "Baluwatar,ktm", "Best Futsal around baluwatar", null, false, "dukhufutsal_photo"),
                new FutsalModel(UUID.randomUUID(), "Tokha Futsal", "Tokha,Ktm", "Best Futsal around tokha", futsalOwner, true, "TokhaFutsal_photo")
        );

        futsalOwner.setFutsals(futsalList);

        when(futsalOwnerRepo.findById(futsalOwnerId)).thenReturn(java.util.Optional.of(futsalOwner));
        when(awsService.setFutsalPhotoIntoPresignUrl(futsalList)).thenReturn(futsalList);

        List<FutsalListDto> result = adminService.getFutsalListOfFutsalOwner(futsalOwnerId);

        verify(futsalOwnerRepo).findById(futsalOwnerId);
        verify(awsService).setFutsalPhotoIntoPresignUrl(futsalList);

        assertNotNull(result);
        assertEquals(futsalList.size(), result.size());

    }

}