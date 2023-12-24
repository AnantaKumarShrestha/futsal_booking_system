package com.intern.futsalBookingSystem.db;

import com.intern.futsalBookingSystem.model.FutsalModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FutsalRepo extends JpaRepository<FutsalModel, UUID> {
}
