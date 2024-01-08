package com.intern.futsalBookingSystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.intern.futsalBookingSystem.model.FutsalOwnerModel;
import com.intern.futsalBookingSystem.model.SlotModel;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class FutsalListDto {

    private UUID id;


    private String futsalName;

    private String futsalLocation;

    private String futsalDescription;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FutsalOwnerModel futsalOwner;

    private boolean isRegistered;

    private String photo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int rating;





}
