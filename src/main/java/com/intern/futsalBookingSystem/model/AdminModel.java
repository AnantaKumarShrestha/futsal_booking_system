package com.intern.futsalBookingSystem.model;


import com.intern.futsalBookingSystem.encryption.EncryptorDecryptor;
import com.intern.futsalBookingSystem.enums.Gender;
import com.intern.futsalBookingSystem.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Data
@Entity
public class AdminModel implements UserDetails{


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
    private String username;

    @Column(nullable = false, length = 120)
    @Convert(converter = EncryptorDecryptor.class)
    private String password;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false, length = 5)
//    private Role role;

    private String photo;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("admin"));
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
