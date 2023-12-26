package com.intern.futsalBookingSystem.model;

import com.intern.futsalBookingSystem.encryption.EncryptorDecryptor;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@NamedQuery(name = "getUserById", query = "SELECT u FROM UserModel u WHERE u.id = :userId")
@Data
@Entity
public class UserModel {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    @Convert(converter = EncryptorDecryptor.class)
    private String email;

    @Column(nullable = false, length = 50)
    @Convert(converter = EncryptorDecryptor.class)
    private String password;

    @Column(nullable = false, length = 50)
    private String gmail;

    @OneToOne(cascade = CascadeType.ALL)
    private AddressModel address;

    private int rewardPoint;

    private String photo;

}
