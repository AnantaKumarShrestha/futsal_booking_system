package com.intern.futsalBookingSystem.mapper;

import com.intern.futsalBookingSystem.dto.UserDto;
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


  //  @Mapping(target = "email", source = "email", qualifiedByName = "maskEmail")
    @Mapping(target = "password", source = "password", qualifiedByName = "maskPassword")
    UserDto userModelIntoUserDto(UserModel userModel);

    List<UserDto> userModelListIntoUserDtoList(List<UserModel> userModels);

    @Named("maskPassword")
    default String maskPassword(String email) {
        return "*****";
    }


}
