package com.intern.futsalBookingSystem.model;

import com.intern.futsalBookingSystem.encryption.EncryptorDecryptor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
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

    @Pattern(message = "Enter a valid email",regexp = "/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$")
    @Column(nullable = false,unique = true)
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
