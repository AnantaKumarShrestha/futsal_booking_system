package com.intern.futsalBookingSystem.db;

import com.intern.futsalBookingSystem.model.FutsalModel;
import com.intern.futsalBookingSystem.model.RatingModel;
import com.intern.futsalBookingSystem.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RatingRepo extends JpaRepository<RatingModel, UUID> {

    @Query("SELECT r FROM RatingModel r WHERE r.user = :user AND r.futsal = :futsal")
    Optional<RatingModel> getRatingByUserAndFutsal(UserModel user, FutsalModel futsal);
}
