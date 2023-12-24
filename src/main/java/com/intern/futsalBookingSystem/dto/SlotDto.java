package com.intern.futsalBookingSystem.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class SlotDto {



    private UUID id;

    private UserDto bookedByUser;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isBooked;

    private boolean completed;

    private double price;

}
