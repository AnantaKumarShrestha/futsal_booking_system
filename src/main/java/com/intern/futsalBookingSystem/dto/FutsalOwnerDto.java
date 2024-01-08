package com.intern.futsalBookingSystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String username;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    private String gmail;

    private String phoneNumber;

    private String photo;


}
