package com.sroka.grouptripsorganizer.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@PropertySource("classpath:configuration.properties")
@ConfigurationProperties(prefix = "configuration.security")
public class FailLoginAttemptsProperties {
    private Integer maxLoginFailedAttempts;

    private Integer failLoginAttemptExpirationInMinutes;

    private Integer failLoginAttemptsClearOffsetInMinutes;
}