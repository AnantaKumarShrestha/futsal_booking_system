package com.intern.futsalBookingSystem.mapper;

import com.intern.futsalBookingSystem.dto.UserDto;
import com.intern.futsalBookingSystem.dto.UserListDto;
import com.intern.futsalBookingSystem.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE= Mappers.getMapper(UserMapper.class);

    UserModel userDtoIntoUserModel(UserDto userDto);


    @Mapping(target = "email", source = "email", qualifiedByName = "maskEmail")
    @Mapping(target = "password", source = "password", qualifiedByName = "maskPassword")
    UserDto userModelIntoUserDto(UserModel userModel);

    List<UserListDto> userModelListIntoUserListDtoList(List<UserModel> userModels);

    @Named("maskPassword")
    default String maskPassword(String password) {
        StringBuilder stringBuilder=new StringBuilder();
        for (int i=0;i<password.length();i++){
            stringBuilder.append("*");
        }
        return stringBuilder.toString();
    }

    @Named("maskEmail")
    default String maskEmail(String email){
        int atIndex = email.indexOf('@');
        StringBuilder stringBuilder=new StringBuilder();

        for (int i=0;i<email.length();i++){

            if(i<4 || i>=atIndex){
                stringBuilder.append(email.charAt(i));
            }else{
                stringBuilder.append("*");
            }
        }
        return stringBuilder.toString();
    }


}
