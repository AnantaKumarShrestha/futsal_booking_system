package com.intern.futsalBookingSystem.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@NamedQuery(name = "findAllByBookedByUserId", query = "SELECT s FROM SlotModel s WHERE s.bookedByUser.id = :userId")
@NamedQuery(name = "getSlotById", query = "SELECT s FROM SlotModel s WHERE s.id = :slotId")
@Data
@Entity
public class SlotModel {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

//    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private UserModel bookedByUser;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    private boolean isBooked;

    private boolean completed;

    @Column(nullable = false, length = 10)
    private double price;


}
