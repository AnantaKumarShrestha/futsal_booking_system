package com.intern.futsalBookingSystem.db;

import com.intern.futsalBookingSystem.model.FutsalOwnerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FutsalOwnerRepo extends JpaRepository<FutsalOwnerModel, UUID> {

    @Query(name = "etFutsalOwnerById")
    Optional<FutsalOwnerModel> getFutsalOwnerById(@Param("futsalOwnerId") UUID futsalOwnerId);

}
