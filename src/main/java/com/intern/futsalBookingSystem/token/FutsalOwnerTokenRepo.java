package com.intern.futsalBookingSystem.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FutsalOwnerTokenRepo extends JpaRepository<FutsalOwnerToken,Integer> {
    @Query("SELECT t FROM FutsalOwnerToken t WHERE t.user.id = :id AND (t.expired = false OR t.revoked = false)")
    List<FutsalOwnerToken> findAllValidTokenByUser(UUID id);

    Optional<FutsalOwnerToken> findByToken(String token);
}
