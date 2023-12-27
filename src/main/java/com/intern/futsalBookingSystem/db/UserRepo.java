package com.intern.futsalBookingSystem.db;

import com.intern.futsalBookingSystem.model.AdminModel;
import com.intern.futsalBookingSystem.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<UserModel, UUID> {


    @Query(name = "getUserById")
    Optional<UserModel> getUserById(UUID userId);

    Optional<UserModel> findByEmailAndPassword(String username, String password);
}
