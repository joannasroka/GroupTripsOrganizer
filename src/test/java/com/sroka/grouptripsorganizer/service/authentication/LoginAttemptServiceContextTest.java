package com.sroka.grouptripsorganizer.service.authentication;


import com.sroka.grouptripsorganizer.builder.authentication.FailedLoginAttemptBuilder;
import com.sroka.grouptripsorganizer.entity.authentication.FailedLoginAttempt;
import com.sroka.grouptripsorganizer.repository.authentication.FailLoginRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class LoginAttemptServiceContextTest {

    @Autowired
    FailLoginRepository failLoginRepository;

    @Autowired
    LoginAttemptService loginAttemptService;

    @Rollback
    @Transactional
    @Test
    @DisplayName("Should update FailedLoginAttempt record when record exists and invoked loginFailed()")
    void shouldUpdateFailedLoginAttemptWhenInvokedLoginFailed() {
        //given
        int failAttemptsBeforeFailedLogin = 2;
        LocalDateTime lastFailedAttemptDate = LocalDateTime.now().minusHours(2);
        int expectedFailAttemptsAfterFailedLogin = 3;
        boolean expectedLockStatus = false;

        FailedLoginAttempt failedLoginAttempt = FailedLoginAttemptBuilder.get()
                .withLastFailAttempt(lastFailedAttemptDate)
                .withFailAttempts(failAttemptsBeforeFailedLogin)
                .build();

        failedLoginAttempt = failLoginRepository.save(failedLoginAttempt);

        //when
        loginAttemptService.loginFailed(failedLoginAttempt.getEmail());

        //then
        FailedLoginAttempt actualFailedLoginAttempt = failLoginRepository.getById(failedLoginAttempt.getId());
        assertTrue(lastFailedAttemptDate.isBefore(actualFailedLoginAttempt.getLastFailAttempt()));
        assertEquals(expectedFailAttemptsAfterFailedLogin, actualFailedLoginAttempt.getFailAttempts());
        assertEquals(expectedLockStatus, actualFailedLoginAttempt.isLocked());
    }

    @Rollback
    @Transactional
    @Test
    @DisplayName("Should lock username when fail attempts threshold is exceeded and invoked loginFailed()")
    void shouldLockUsernameWhenFailAttemptsThresholdExceededAndInvokedLoginFailed() {
        // given
        LocalDateTime lastFailedAttemptDate = LocalDateTime.now().minusMinutes(2);

        FailedLoginAttempt failedLoginAttempt = FailedLoginAttemptBuilder.get()
                .withLastFailAttempt(lastFailedAttemptDate)
                .withFailAttempts(4)
                .build();

        failedLoginAttempt = failLoginRepository.save(failedLoginAttempt);

        // when
        loginAttemptService.loginFailed(failedLoginAttempt.getEmail());

        // then
        FailedLoginAttempt failedLoginAttemptAfterLoginFailed = failLoginRepository.getById(failedLoginAttempt.getId());

        assertTrue(failedLoginAttemptAfterLoginFailed.isLocked());
        assertTrue(lastFailedAttemptDate.isBefore(failedLoginAttemptAfterLoginFailed.getLastFailAttempt()));
    }
}