package com.intern.futsalBookingSystem.dto;

import com.intern.futsalBookingSystem.model.FutsalOwnerModel;
import com.intern.futsalBookingSystem.model.SlotModel;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class FutsalDto {


    private UUID id;


    private String futsalName;

    private String futsalLocation;

    private String futsalDescription;


    private FutsalOwnerModel futsalOwner;

    private boolean isRegistered;

    //   private String photo;

    private List<SlotModel> slots;

    private String photo;

}
