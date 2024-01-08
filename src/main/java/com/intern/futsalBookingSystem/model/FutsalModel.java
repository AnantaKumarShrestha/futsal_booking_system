package com.intern.futsalBookingSystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NamedQuery(name = "getFutsalById", query = "SELECT f FROM FutsalModel f WHERE f.id = :futsalId")
@NamedQuery(name = "getAllFutsal",query = "SELECT f FROM FutsalModel f")
@NamedQuery(name = "getFutsalBySlotId", query = "SELECT f FROM FutsalModel f JOIN f.slots s WHERE s.id = :slotId")
@Data
@NoArgsConstructor
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

    private String photo;

    @OneToMany(mappedBy = "futsal", cascade = CascadeType.ALL)
    private List<RatingModel> ratings;

    public FutsalModel(UUID id, String futsalName, String futsalLocation, String futsalDescription, FutsalOwnerModel futsalOwner, boolean isRegistered, String photo) {
        this.id=id;
        this.futsalName=futsalName;
        this.futsalLocation=futsalLocation;
        this.futsalDescription=futsalDescription;
        this.futsalOwner=futsalOwner;
        this.isRegistered=isRegistered;
        this.photo=photo;
    }

}
