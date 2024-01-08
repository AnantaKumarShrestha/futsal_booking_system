package com.intern.futsalBookingSystem.mapper;

import com.intern.futsalBookingSystem.dto.FutsalDto;
import com.intern.futsalBookingSystem.model.FutsalModel;
import com.intern.futsalBookingSystem.model.RatingModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FutsalMapper {

    FutsalMapper INSTANCE= Mappers.getMapper(FutsalMapper.class);

    FutsalModel futsalDtoIntoFutsalModel(FutsalDto futsalDto);

    @Mapping(target = "rating", source = "ratings", qualifiedByName = "ratingFunction")
    FutsalDto futsalModelIntoFutsalDto(FutsalModel futsalModel);

    List<FutsalDto> futsalModelListIntoFutsalDtoList(List<FutsalModel> futsalModels);

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
