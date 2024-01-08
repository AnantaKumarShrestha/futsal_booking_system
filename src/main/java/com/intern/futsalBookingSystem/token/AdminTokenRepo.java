package com.intern.futsalBookingSystem.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminTokenRepo extends JpaRepository<AdminToken,Integer> {
    @Query("SELECT t FROM AdminToken t WHERE t.user.id = :id AND (t.expired = false OR t.revoked = false)")
    List<AdminToken> findAllValidTokenByUser(UUID id);

    Optional<AdminToken> findByToken(String token);
}
