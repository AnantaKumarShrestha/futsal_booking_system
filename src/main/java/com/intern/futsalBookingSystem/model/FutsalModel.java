package com.intern.futsalBookingSystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@NamedQuery(name = "getFutsalById", query = "SELECT f FROM FutsalModel f WHERE f.id = :futsalId")
@NamedQuery(name = "getAllFutsal",query = "SELECT f FROM FutsalModel f")
@Data
@Entity
public class FutsalModel {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String futsalName;

    @Column(nullable = false, length = 50)
    private String futsalLocation;

    @Column(nullable = false, length = 200)
    private String futsalDescription;

    @ManyToOne(cascade = CascadeType.ALL)
    private FutsalOwnerModel futsalOwner;

    private boolean isRegistered;

 //   private String photo;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SlotModel> slots;

}
