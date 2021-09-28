package com.sroka.grouptripsorganizer.scheduler;

import com.sroka.grouptripsorganizer.properties.FailLoginAttemptsProperties;
import com.sroka.grouptripsorganizer.repository.authentication.FailLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
@PropertySource("classpath:configuration.properties")
@Transactional
public class FailLoginAttemptCleaner {

    private final FailLoginRepository failLoginRepository;
    private final FailLoginAttemptsProperties failLoginAttemptsConfig;

    @Scheduled(fixedDelayString = "${configuration.scheduler.clearFailedLoginAttemptsTimeIntervalInMilliseconds}")
    public void clearExpiredFailLoginAttempts() {
        failLoginRepository.clearExpiredFailLoginAttempts(failLoginAttemptsConfig.getFailLoginAttemptExpirationInMinutes(),
                failLoginAttemptsConfig.getFailLoginAttemptsClearOffsetInMinutes());
    }
}