package com.intern.futsalBookingSystem.mapper;

import com.intern.futsalBookingSystem.dto.FutsalDto;
import com.intern.futsalBookingSystem.dto.FutsalListDto;
import com.intern.futsalBookingSystem.model.FutsalModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FutsalListMapper {

    FutsalListMapper INSTANCE= Mappers.getMapper(FutsalListMapper.class);

    FutsalModel futsalListDtoIntoFutsalModel(FutsalListDto futsalListDto);

    FutsalListDto futsalModelIntoFutsalListDto(FutsalModel futsalModel);

    List<FutsalListDto>  futsalModelListIntoFutsalListDtoList(List<FutsalModel> futsalModelList);

}
