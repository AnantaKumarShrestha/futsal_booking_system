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


    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    UserDto userModelIntoUserDto(UserModel userModel);

    List<UserListDto> userModelListIntoUserListDtoList(List<UserModel> userModels);




}
