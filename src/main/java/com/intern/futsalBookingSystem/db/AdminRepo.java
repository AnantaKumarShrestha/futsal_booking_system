package com.intern.futsalBookingSystem.db;

import com.intern.futsalBookingSystem.model.AdminModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminRepo extends JpaRepository<AdminModel, UUID> {

    Optional<AdminModel> findByUsernameAndPassword(String username, String password);

    Optional<AdminModel> findByUsername(String username);
}
