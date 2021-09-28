package com.sroka.grouptripsorganizer.entity.authentication;

import com.sroka.grouptripsorganizer.entity.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class FailedLoginAttempt extends BaseEntity {
    @Column(name = "email", length = 250, unique = true, nullable = false, updatable = false)
    private String email;

    @Column(name = "fail_attempts", nullable = false)
    private int failAttempts;

    @Column(name = "last_fail_attempt", nullable = false)
    private LocalDateTime lastFailAttempt;

    @Column(name = "locked", nullable = false)
    private boolean locked;
}