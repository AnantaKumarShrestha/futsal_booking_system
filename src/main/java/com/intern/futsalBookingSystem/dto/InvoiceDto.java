package com.intern.futsalBookingSystem.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class InvoiceDto {

    private int invoiceId;
    private UUID futsalId;
    private String customerName;
    private String date;
    private String gameStartTime;
    private String gameEndTime;
    private int price;

}
