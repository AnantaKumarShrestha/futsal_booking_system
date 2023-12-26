package com.intern.futsalBookingSystem.dto;

import com.intern.futsalBookingSystem.model.AddressModel;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class UserListDto {

    private UUID id;

    private String firstName;

    private String lastName;

    private int age;

    private String gmail;

    private AddressModel address;

    private int rewardPoint;

}
