package com.intern.futsalBookingSystem.payload;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SlotRequest {

    private LocalDateTime startTime;
    private int numberOfSlots;
    private int slotDurationMinutes;
    private double slotPrice;
}
