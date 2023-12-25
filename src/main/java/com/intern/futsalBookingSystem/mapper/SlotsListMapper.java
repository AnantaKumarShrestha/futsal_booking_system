package com.intern.futsalBookingSystem.mapper;

import com.intern.futsalBookingSystem.dto.SlotsListDto;
import com.intern.futsalBookingSystem.model.SlotModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SlotsListMapper {

    SlotsListMapper INSTANCE= Mappers.getMapper(SlotsListMapper.class);

    SlotsListDto slotModelIntoSlotListDto(SlotModel slotModel);

    List<SlotsListDto> slotModelListIntoSlotListDto(List<SlotModel> slotModelList);

}
