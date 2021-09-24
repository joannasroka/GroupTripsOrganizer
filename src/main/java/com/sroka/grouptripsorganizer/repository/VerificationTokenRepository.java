package com.sroka.grouptripsorganizer.repository;

import com.sroka.grouptripsorganizer.entity.account_activation.VerificationToken;
import com.sroka.grouptripsorganizer.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    boolean existsByToken(String token);

    Optional<VerificationToken> findByTokenAndExpiryDateGreaterThanEqual(String token, LocalDateTime now);

    Optional<VerificationToken> findByUser(User user);

    void deleteByExpiryDateLessThan(LocalDateTime now);
}
