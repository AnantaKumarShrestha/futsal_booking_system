package com.intern.futsalBookingSystem.token;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Integer> {

  @Query("SELECT t FROM UserToken t WHERE t.user.id = :id AND (t.expired = false OR t.revoked = false)")
  List<UserToken> findAllValidTokenByUser(UUID id);

  Optional<UserToken> findByToken(String token);
}
