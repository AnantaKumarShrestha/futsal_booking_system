package com.intern.futsalBookingSystem.mapper;

import com.intern.futsalBookingSystem.dto.FutsalDto;
import com.intern.futsalBookingSystem.dto.FutsalListDto;
import com.intern.futsalBookingSystem.model.FutsalModel;
import com.intern.futsalBookingSystem.model.RatingModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FutsalListMapper {

    FutsalListMapper INSTANCE= Mappers.getMapper(FutsalListMapper.class);

    FutsalModel futsalListDtoIntoFutsalModel(FutsalListDto futsalListDto);
    @Mapping(target = "rating", source = "ratings", qualifiedByName = "ratingFunction")
    FutsalListDto futsalModelIntoFutsalListDto(FutsalModel futsalModel);

    List<FutsalListDto>  futsalModelListIntoFutsalListDtoList(List<FutsalModel> futsalModelList);

    @Named("ratingFunction")
    default int ratingFunction(List<RatingModel> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            return 0;
        }

        int totalRating = 0;
        int numberOfRatings = 0;

        for (RatingModel rating : ratings) {
            totalRating += rating.getRating();
            numberOfRatings++;
        }

        return totalRating / numberOfRatings;
    }

}
