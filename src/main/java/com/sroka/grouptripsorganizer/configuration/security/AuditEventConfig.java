package com.sroka.grouptripsorganizer.configuration.security;

import org.springframework.boot.actuate.security.AuthenticationAuditListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditEventConfig {
    @Bean
    public AuthenticationAuditListener authenticationAuditListener() {
        return new AuthenticationAuditListener();
    }
}
