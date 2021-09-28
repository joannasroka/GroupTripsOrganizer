package com.sroka.grouptripsorganizer.security;

import com.sroka.grouptripsorganizer.service.authentication.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {
    private final LoginAttemptService loginAttemptService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        final UserDetails userDetails = (UserDetails) event.getAuthentication().getPrincipal();
        loginAttemptService.clearFailLoginAttemptsAfterSuccessfulLogin(userDetails.getUsername());
    }
}