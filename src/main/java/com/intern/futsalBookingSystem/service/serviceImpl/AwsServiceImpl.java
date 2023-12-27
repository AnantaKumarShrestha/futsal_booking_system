package com.intern.futsalBookingSystem.service.serviceImpl;


import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.intern.futsalBookingSystem.model.FutsalModel;
import com.intern.futsalBookingSystem.model.FutsalOwnerModel;
import com.intern.futsalBookingSystem.model.InvoiceModel;
import com.intern.futsalBookingSystem.model.UserModel;
import com.intern.futsalBookingSystem.service.AwsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@Service
public class AwsServiceImpl implements AwsService {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.bucketName}")
    private String bucketName;


    @Override
    public String getPhotoFromAws(String fileName) {

        java.util.Date expiration = new java.util.Date();
        long min = expiration.getTime();
        min += 1000 * 30 * 1; // 2
        expiration.setTime(min);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName);
        generatePresignedUrlRequest.setMethod(HttpMethod.GET);
        generatePresignedUrlRequest.setExpiration(expiration);
        URL s = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        return String.valueOf(s);
    }

    @Override
    public List<FutsalModel> setFutsalPhotoIntoPresignUrl(List<FutsalModel> futsalModels) {

        for(FutsalModel futsal:futsalModels){
            futsal.setPhoto(getPhotoFromAws(futsal.getPhoto()));
        }

        return futsalModels;
    }

    @Override
    public List<FutsalOwnerModel> setFutsalOwnerPhotoIntoUrl(List<FutsalOwnerModel> futsalOwnerList) {

        for(FutsalOwnerModel futsalOwner: futsalOwnerList){
            futsalOwner.setPhoto(getPhotoFromAws(futsalOwner.getPhoto()));
        }

        return futsalOwnerList;
    }

    @Override
    public List<UserModel> setUserPhotoIntoUrl(List<UserModel> userList) {

        for(UserModel user:userList){
            user.setPhoto(getPhotoFromAws(user.getPhoto()));
        }

        return userList;
    }

    @Override
    public String uploadPhotoIntoAws(MultipartFile file) throws IOException {

        File modifiedFile=new File(file.getOriginalFilename());
        FileOutputStream os=new FileOutputStream(modifiedFile);
        os.write(file.getBytes());

        String fileName=System.currentTimeMillis()+"_"+file.getOriginalFilename();
        amazonS3.putObject(bucketName,fileName,modifiedFile);

        return fileName;
    }

    @Override
    public void deletePhotoInAwsServer(String photo) {

        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, photo);
        amazonS3.deleteObject(deleteObjectRequest);

    }


}
