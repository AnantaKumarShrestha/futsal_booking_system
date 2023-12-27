package com.intern.futsalBookingSystem.dto;

import com.intern.futsalBookingSystem.model.AddressModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
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

    private String email;

    private String password;

    private String gmail;

    private AddressModel address;

    private int rewardPoint;

    private String photo;



}
