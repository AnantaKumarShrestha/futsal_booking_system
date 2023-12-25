package com.intern.futsalBookingSystem.db;

import com.intern.futsalBookingSystem.model.FutsalModel;
import com.intern.futsalBookingSystem.model.SlotModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FutsalRepo extends JpaRepository<FutsalModel, UUID> {


    @Query("SELECT s FROM FutsalModel f JOIN f.slots s WHERE s.completed = true AND s.isBooked = true AND f.id = :futsalId AND s.startTime BETWEEN :startDate AND :endDate")
    List<SlotModel> findCompletedAndBookedSlotsForFutsal(
            @Param("futsalId") UUID futsalId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query(name = "getFutsalById")
    Optional<FutsalModel> getFutsalById(@Param("futsalId") UUID futsalId);

    @Query(name = "getAllFutsal")
    List<FutsalModel> getAllFutsal();


}
