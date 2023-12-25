package com.intern.futsalBookingSystem.mapper;

import com.intern.futsalBookingSystem.dto.AdminDto;
import com.intern.futsalBookingSystem.model.AdminModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminMapper {

    AdminMapper INSTANCE= Mappers.getMapper(AdminMapper.class);


    AdminModel adminDtoIntoAdminModel(AdminDto adminDto);

    @Mapping(source = "username",target = "username",qualifiedByName = "markUsername")
    @Mapping(source = "password",target = "password",qualifiedByName = "markPassword")
    AdminDto adminModelIntoAdminDto(AdminModel adminModel);


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

    @Named("markPassword")
    default String markPassword(String password){
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<password.length();i++){
            stringBuilder.append("*");
        }
        return stringBuilder.toString();
    }

}
