package com.intern.futsalBookingSystem.db;

import com.intern.futsalBookingSystem.model.SlotModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SlotRepo extends JpaRepository<SlotModel, UUID> {

    @Query(name = "findAllByBookedByUserId")
    List<SlotModel> findAllByBookedByUserId(@Param("userId") UUID userId);

     @Query(name = "getSlotById")
    Optional<SlotModel> getSlotById(@Param("slotId") UUID slotId);
}
