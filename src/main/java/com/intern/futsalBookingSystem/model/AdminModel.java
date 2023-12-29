package com.intern.futsalBookingSystem.model;


import com.intern.futsalBookingSystem.encryption.EncryptorDecryptor;
import com.intern.futsalBookingSystem.enums.Gender;
import com.intern.futsalBookingSystem.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class AdminModel {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 50)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    private int age;

    @Column(nullable = false, length = 120)
    private String gmail;

    @Column(nullable = false, length = 120,unique = true)
    @Convert(converter = EncryptorDecryptor.class)
    @Email
    private String username;

    @Column(nullable = false, length = 120)
    @Convert(converter = EncryptorDecryptor.class)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 5)
    private Role role;

    private String photo;

}
