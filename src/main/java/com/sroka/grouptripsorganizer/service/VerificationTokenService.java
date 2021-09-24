package com.sroka.grouptripsorganizer.service;


import com.sroka.grouptripsorganizer.dto.AccountActivationDto;
import com.sroka.grouptripsorganizer.entity.account_activation.VerificationToken;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.AccountAlreadyActivatedException;
import com.sroka.grouptripsorganizer.exception.InvalidPasswordException;
import com.sroka.grouptripsorganizer.exception.TokenNotFoundException;
import com.sroka.grouptripsorganizer.repository.UserRepository;
import com.sroka.grouptripsorganizer.repository.VerificationTokenRepository;
import com.sroka.grouptripsorganizer.validate.CustomPasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.sroka.grouptripsorganizer.entity.account_activation.AccountStatus.ACTIVE;

@Service
@RequiredArgsConstructor
@Transactional
public class VerificationTokenService {
    private final VerificationTokenRepository tokenRepository;
    private final CustomPasswordValidator customPasswordValidator;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ActivationEmailService activationEmailService;

    public VerificationToken create(User user) {
        tokenRepository.findByUser(user).
                ifPresent(tokenRepository::delete);

        String token = generateToken();

        return tokenRepository.save(new VerificationToken(user, token));
    }

    public void validateToken(String token) {
        tokenRepository.findByTokenAndExpiryDateGreaterThanEqual(token, LocalDateTime.now())
                .orElseThrow(TokenNotFoundException::new);
    }

    public void activateAccount(AccountActivationDto accountActivation) {
        VerificationToken verificationToken = tokenRepository.findByTokenAndExpiryDateGreaterThanEqual(accountActivation.getToken(), LocalDateTime.now())
                .orElseThrow(TokenNotFoundException::new);

        User user = verificationToken.getUser();
        String userPassword = accountActivation.getPassword().trim();

        if (!customPasswordValidator.validate(userPassword)) {
            throw new InvalidPasswordException();
        }

        String encodedPassword = passwordEncoder.encode(userPassword);
        user.setPassword(encodedPassword);
        user.setAccountStatus(ACTIVE);

        tokenRepository.delete(verificationToken);
    }

    public void resend(Long executorId, Long userId) {
        User executingUser = userRepository.getById(executorId);
        User userToResendToken = userRepository.getById(userId);

        if (userToResendToken.getAccountStatus() == ACTIVE) {
            throw new AccountAlreadyActivatedException();
        }

        VerificationToken verificationToken = create(userToResendToken);

        activationEmailService.sendActivationMail(verificationToken);
    }

    private String generateToken() {
        String token;
        do {
            token = UUID.randomUUID().toString();
        }
        while (tokenRepository.existsByToken(token));

        return token;
    }

}