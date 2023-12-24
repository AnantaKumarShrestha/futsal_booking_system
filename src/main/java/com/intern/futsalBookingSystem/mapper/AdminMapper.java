package com.intern.futsalBookingSystem.mapper;

import com.intern.futsalBookingSystem.dto.AdminDto;
import com.intern.futsalBookingSystem.model.AdminModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminMapper {

    AdminMapper INSTANCE= Mappers.getMapper(AdminMapper.class);

    AdminModel adminDtoIntoAdminModel(AdminDto adminDto);

    AdminDto adminModelIntoAdminDto(AdminModel adminModel);

}
