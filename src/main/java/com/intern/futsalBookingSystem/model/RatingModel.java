package com.intern.futsalBookingSystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class RatingModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private UserModel user;

    @ManyToOne
    private FutsalModel futsal;

    private int rating;


}
