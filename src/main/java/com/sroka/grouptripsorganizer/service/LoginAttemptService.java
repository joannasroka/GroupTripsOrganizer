package com.sroka.grouptripsorganizer.service;

import com.sroka.grouptripsorganizer.entity.authentication.FailedLoginAttempt;
import com.sroka.grouptripsorganizer.properties.FailLoginAttemptsProperties;
import com.sroka.grouptripsorganizer.repository.authentication.FailLoginRepository;
import com.sroka.grouptripsorganizer.security.exception.UserLockedAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginAttemptService {
    private final FailLoginAttemptsProperties failLoginAttemptsConfig;

    private final FailLoginRepository failLoginRepository;

    public void loginFailed(String email) {
        Optional<FailedLoginAttempt> failedLoginAttemptOpt = failLoginRepository.findByEmail(email);

        if (failedLoginAttemptOpt.isEmpty()) {
            failLoginRepository.save(new FailedLoginAttempt(email, 1, LocalDateTime.now(), false));
            return;
        }

        FailedLoginAttempt failedLoginAttempt = failedLoginAttemptOpt.get();

        if (failedLoginAttempt.isLocked()) {
            throw new UserLockedAuthenticationException();
        }

        incrementFailAtempts(failedLoginAttempt);
        updateAttemptTime(failedLoginAttempt);

        if (isFailLoginTresholdExceeded(failedLoginAttempt)) {
            failedLoginAttempt.setLocked(true);
        }
    }

    public void checkIfUsernameLocked(String email) {
        Optional<FailedLoginAttempt> failedLoginAttemptOpt = failLoginRepository.findByEmail(email);

        if (failedLoginAttemptOpt.isEmpty()) {
            return;
        }

        FailedLoginAttempt failedLoginAttempt = failedLoginAttemptOpt.get();
        if (failedLoginAttempt.isLocked()) {
            boolean isLockPassed = failedLoginAttempt.getLastFailAttempt().plusMinutes(failLoginAttemptsConfig.getFailLoginAttemptExpirationInMinutes()).isBefore(LocalDateTime.now());

            if (!isLockPassed) {
                throw new UserLockedAuthenticationException();
            }

            clear(failedLoginAttempt);
        }
    }

    public void clearFailLoginAttemptsAfterSuccessfulLogin(String email) {
        Optional<FailedLoginAttempt> failedLoginAttempt = failLoginRepository.findByEmail(email);

        if (failedLoginAttempt.isPresent()) {
            clear(failedLoginAttempt.get());
        }
    }

    private boolean isFailLoginTresholdExceeded(FailedLoginAttempt failedLoginAttempt) {
        return failedLoginAttempt.getFailAttempts() >= failLoginAttemptsConfig.getMaxLoginFailedAttempts();
    }

    private void incrementFailAtempts(FailedLoginAttempt failedLoginAttempt) {
        failedLoginAttempt.setFailAttempts(failedLoginAttempt.getFailAttempts() + 1);
    }

    private void updateAttemptTime(FailedLoginAttempt failedLoginAttempt) {
        failedLoginAttempt.setLastFailAttempt(LocalDateTime.now());
    }

    private void clear(FailedLoginAttempt failedLoginAttempt) {
        failLoginRepository.delete(failedLoginAttempt);
    }
}