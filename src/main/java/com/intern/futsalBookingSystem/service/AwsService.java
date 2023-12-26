package com.intern.futsalBookingSystem.service;

import com.intern.futsalBookingSystem.model.FutsalModel;
import com.intern.futsalBookingSystem.model.FutsalOwnerModel;
import com.intern.futsalBookingSystem.model.UserModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AwsService {

    String getPhotoFromAws(String photo);

    List<FutsalModel> setFutsalPhotoIntoPresignUrl(List<FutsalModel> futsalModels);

    List<FutsalOwnerModel> setFutsalOwnerPhotoIntoUrl(List<FutsalOwnerModel> futsalOwnerModelList);

    List<UserModel> setUserPhotoIntoUrl(List<UserModel> userModels);

    String uploadPhotoIntoAws(MultipartFile file) throws IOException;

}
