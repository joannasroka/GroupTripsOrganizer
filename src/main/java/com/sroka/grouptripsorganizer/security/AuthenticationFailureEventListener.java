package com.sroka.grouptripsorganizer.security;

import com.sroka.grouptripsorganizer.service.authentication.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFailureEventListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    private final LoginAttemptService loginAttemptService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        final String userName = event.getAuthentication().getPrincipal().toString();

        loginAttemptService.loginFailed(userName);
        loginAttemptService.checkIfUsernameLocked(userName);
    }
}
