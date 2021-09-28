package com.sroka.grouptripsorganizer.repository.authentication;

import com.sroka.grouptripsorganizer.entity.authentication.FailedLoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FailLoginRepository extends JpaRepository<FailedLoginAttempt, Long> {
    Optional<FailedLoginAttempt> findByEmail(String email);

    @Modifying
    @Query("DELETE FROM FailedLoginAttempt f WHERE TIMESTAMPADD( MINUTE, :lockDuration + :offset,  f.lastFailAttempt ) <= CURRENT_TIMESTAMP()")
    void clearExpiredFailLoginAttempts(@Param("lockDuration") Integer lockDurationInMinutes, @Param("offset") Integer offsetInMinutes);
}