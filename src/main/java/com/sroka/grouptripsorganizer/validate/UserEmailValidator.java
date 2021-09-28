package com.sroka.grouptripsorganizer.validate;

import com.sroka.grouptripsorganizer.exception.ValidationException;
import com.sroka.grouptripsorganizer.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class UserEmailValidator {
    private final UserRepository userRepository;

    public void validateEmail(String email, Errors errors) {
        if (!email.isBlank() && !isEmailUnique(email)) {
            errors.rejectValue("email", "error.emailExists", "error.emailExists");
        }

        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
    }

    private boolean isEmailUnique(String email) {
        return !userRepository.existsByEmailIgnoreCase(email);
    }
}