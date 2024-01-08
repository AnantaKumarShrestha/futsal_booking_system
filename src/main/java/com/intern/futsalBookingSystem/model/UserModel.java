package com.intern.futsalBookingSystem.model;

import com.intern.futsalBookingSystem.encryption.EncryptorDecryptor;
import com.intern.futsalBookingSystem.validator.UniqueEmail;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NamedQuery(name = "getUserById", query = "SELECT u FROM UserModel u WHERE u.id = :userId")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    @UniqueEmail
 //   @Unique()
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RatingModel> ratings;
    public UserModel(UUID id,String firstName, String lasName, int age, String email, String password, String gmail,AddressModel address,int rewardPoint,String photo) {
        this.id=id;
        this.firstName=firstName;
        this.lastName=lasName;
        this.age=age;
        this.email=email;
        this.password=password;
        this.gmail=gmail;
        this.address=address;
        this.rewardPoint=rewardPoint;
        this.photo=photo;
    }
}
