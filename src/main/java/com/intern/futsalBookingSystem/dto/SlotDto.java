package com.intern.futsalBookingSystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SlotDto {



    private UUID id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserListDto bookedByUser;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isBooked;

    private boolean completed;

    private double price;

}
