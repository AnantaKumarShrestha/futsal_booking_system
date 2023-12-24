package com.intern.futsalBookingSystem.model;

import com.intern.futsalBookingSystem.encryption.EncryptorDecryptor;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
public class FutsalOwnerModel {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 50)
    @Convert(converter = EncryptorDecryptor.class)
    private String username;

    @Column(nullable = false, length = 50)
    @Convert(converter = EncryptorDecryptor.class)
    private String password;

    @Column(nullable = false, length = 50)
    private String gmail;

    @Column(nullable = false, length = 50)
    private String phoneNumber;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "futsalId")
    private List<FutsalModel> futsals;



}
