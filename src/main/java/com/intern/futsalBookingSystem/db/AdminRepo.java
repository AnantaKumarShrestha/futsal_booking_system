package com.intern.futsalBookingSystem.db;

import com.intern.futsalBookingSystem.model.AdminModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminRepo extends JpaRepository<AdminModel, UUID> {
}
