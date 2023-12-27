package com.intern.futsalBookingSystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class InvoiceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int invoiceId;
    private UUID futsalId;
    private String customerName;
    private String date;
    private String gameStartTime;
    private String gameEndTime;
    private int price;


}
