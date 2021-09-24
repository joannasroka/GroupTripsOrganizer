package com.sroka.grouptripsorganizer.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationContextService {
    public Long getCurrentUserId() {
        AppUserDetails appUserDetails = getAppUserDetails();
        return appUserDetails.getId();
    }

    private AppUserDetails getAppUserDetails() {
        Optional<Authentication> authentication = Optional.ofNullable(SecurityContextHolder.getContext()
                .getAuthentication());

        return (AppUserDetails) authentication.orElseThrow(() -> new IllegalStateException("No authentication information is available."))
                .getPrincipal();
    }
}