package com.sroka.grouptripsorganizer.scheduler;

import com.sroka.grouptripsorganizer.repository.authentication.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
@PropertySource("classpath:configuration.properties")
@RequiredArgsConstructor
@Transactional
public class TokensPurgeTask {
    private final VerificationTokenRepository tokenRepository;

    @Scheduled(fixedDelayString = "${configuration.scheduler.clearExpiredTokensTimeIntervalInMilliseconds}")
    public void purgeExpired() {
        LocalDateTime now = LocalDateTime.now();
        tokenRepository.deleteByExpiryDateLessThan(now);
    }
}
