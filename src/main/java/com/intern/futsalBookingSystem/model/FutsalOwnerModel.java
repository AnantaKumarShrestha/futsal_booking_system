package com.intern.futsalBookingSystem.model;

import com.intern.futsalBookingSystem.encryption.EncryptorDecryptor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
public class FutsalOwnerModel {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 50,unique = true)
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

   private String photo;
    public FutsalOwnerModel(UUID id, String firstName, String lastname, String username, String password, String gmail, String phoneNumber, List<FutsalModel> futsals, String photo) {
        this.id=id;
        this.firstName=firstName;
        this.lastName=lastname;
        this.username=username;
        this.password=password;
        this.gmail=gmail;
        this.phoneNumber=phoneNumber;
        this.futsals=futsals;
        this.photo=photo;
    }

}
