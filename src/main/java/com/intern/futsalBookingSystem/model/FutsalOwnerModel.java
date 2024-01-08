package com.intern.futsalBookingSystem.model;

import com.intern.futsalBookingSystem.encryption.EncryptorDecryptor;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.NoArgsConstructor;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Data
@NoArgsConstructor
@Entity
public class FutsalOwnerModel implements UserDetails {


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

    @Column(length = 1000)
   private String photo;


    public FutsalOwnerModel(UUID id, String firstName, String lastname, String username, String password, String gmail, String phoneNumber, List<FutsalModel> futsals, String photo) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastname;
        this.username = username;
        this.password = password;
        this.gmail = gmail;
        this.phoneNumber = phoneNumber;
        this.futsals = futsals;
        this.photo = photo;
    };

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("futsalOwner"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

