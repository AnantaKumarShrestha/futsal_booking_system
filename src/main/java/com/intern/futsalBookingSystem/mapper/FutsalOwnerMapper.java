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

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    FutsalOwnerDto futsalOwnerDtoIntoFutsalOwnerModel(FutsalOwnerModel futsalOwnerModel);

    List<FutsalOwnerDto> futsalOwnerListIntoFutsalOwnerDtoList(List<FutsalOwnerModel> futsalOwnerList);





}
