package com.sroka.grouptripsorganizer.security;

import com.sroka.grouptripsorganizer.security.exception.WithMessageAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    ResponseWriter responseWriter;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        if (exception instanceof BadCredentialsException) {
            responseWriter.writeResponse(response, "error.badCredentials", HttpStatus.UNAUTHORIZED);
        } else if (exception instanceof WithMessageAuthenticationException) {
            responseWriter.writeResponse(response, exception.getMessage(), ((WithMessageAuthenticationException) exception).getHttpStatusCode());
        } else if (exception.getCause() != null && exception.getCause() instanceof WithMessageAuthenticationException) {
            // Spring Security wraps exceptions into InternalAuthenticationServiceException in AbstractAuthenticationProcessingFilter.doFilter method.
            // The origin exception thrown is preserved is the cause of AuthenticationException (exception.getCause()).
            responseWriter.writeResponse(response, exception.getMessage(), ((WithMessageAuthenticationException) exception.getCause()).getHttpStatusCode());
        } else {
            responseWriter.writeResponse(response, "error.unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }
}