package com.sroka.grouptripsorganizer.service.account_activation;


import com.sroka.grouptripsorganizer.entity.account_activation.VerificationToken;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.service.email.ActivationEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AccountActivationService {
    private final ActivationEmailService activationEmailService;
    private final VerificationTokenService verificationTokenService;

    public void sendActivationMail(User user) {
        VerificationToken verificationToken = verificationTokenService.create(user);
        Locale language;

        activationEmailService.sendActivationMail(verificationToken);
    }
}
