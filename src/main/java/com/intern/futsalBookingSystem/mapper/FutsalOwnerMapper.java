package com.intern.futsalBookingSystem.mapper;

import com.intern.futsalBookingSystem.dto.FutsalOwnerDto;
import com.intern.futsalBookingSystem.model.FutsalModel;
import com.intern.futsalBookingSystem.model.FutsalOwnerModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FutsalOwnerMapper {

    FutsalOwnerMapper INSTANCE= Mappers.getMapper(FutsalOwnerMapper.class);

    FutsalOwnerModel futsalOwnerModelIntoFutsalOwnerDto(FutsalOwnerDto futsalOwnerDto);

    @Mapping(target = "username", source = "username", qualifiedByName = "markUsername")
//@Mapping(target = "password", source = "password", qualifiedByName = "maskPassword")
    FutsalOwnerDto futsalOwnerDtoIntoFutsalOwnerModel(FutsalOwnerModel futsalOwnerModel);

    List<FutsalOwnerDto> futsalOwnerListIntoFutsalOwnerDtoList(List<FutsalOwnerModel> futsalOwnerList);


    @Named("markUsername")
    default String markUsername(String username){

        int atIndex=username.indexOf('@');
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<username.length();i++){
            if(i<4||i>=atIndex){
                stringBuilder.append(username.charAt(i));
            }else{
                stringBuilder.append("*");
            }
        }

        return stringBuilder.toString();
    }

    @Named("maskPassword")
    default String markPassword(String password){
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<password.length();i++){
            stringBuilder.append("*");
        }
        return stringBuilder.toString();
    }



}
