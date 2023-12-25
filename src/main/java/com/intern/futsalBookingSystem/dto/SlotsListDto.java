package com.intern.futsalBookingSystem.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SlotsListDto {



    private UUID id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isBooked;

    private boolean completed;

    private double price;

}
