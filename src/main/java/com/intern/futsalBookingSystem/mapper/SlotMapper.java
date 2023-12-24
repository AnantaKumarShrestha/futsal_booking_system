package com.intern.futsalBookingSystem.mapper;

import com.intern.futsalBookingSystem.dto.SlotDto;
import com.intern.futsalBookingSystem.model.SlotModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SlotMapper {

    SlotMapper INSTANCE= Mappers.getMapper(SlotMapper.class);

    SlotModel slotDtoIntoSlotModel(SlotDto slotDto);

    SlotDto slotModelIntoSlotDto(SlotModel slotModel);

    List<SlotDto> slotModelListIntoSlotDtoList(List<SlotModel> slotModelList);
}
