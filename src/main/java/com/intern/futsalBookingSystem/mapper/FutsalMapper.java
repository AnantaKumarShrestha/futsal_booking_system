package com.intern.futsalBookingSystem.mapper;

import com.intern.futsalBookingSystem.dto.FutsalDto;
import com.intern.futsalBookingSystem.model.FutsalModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FutsalMapper {

    FutsalMapper INSTANCE= Mappers.getMapper(FutsalMapper.class);

    FutsalModel futsalDtoIntoFutsalModel(FutsalDto futsalDto);

    FutsalDto futsalModelIntoFutsalDto(FutsalModel futsalModel);

    List<FutsalDto> futsalModelListIntoFutsalDtoList(List<FutsalModel> futsalModels);


}
