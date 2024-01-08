package com.intern.futsalBookingSystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.intern.futsalBookingSystem.model.AddressModel;
import com.intern.futsalBookingSystem.validator.UniqueEmail;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class UserDto {

    private UUID id;

    private String firstName;

    private String lastName;

    private int age;

    @UniqueEmail
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    private String gmail;

    private AddressModel address;

    private int rewardPoint;

    private String photo;



}
