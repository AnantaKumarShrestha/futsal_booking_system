package com.intern.futsalBookingSystem.dto;

import com.intern.futsalBookingSystem.model.FutsalModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class FutsalOwnerDto {


    private UUID id;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private String gmail;

    private String phoneNumber;

    private List<FutsalModel> futsals;

}
