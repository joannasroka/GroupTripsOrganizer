package com.sroka.grouptripsorganizer.service;

import com.sroka.grouptripsorganizer.email.AccountActivationMailGenerator;
import com.sroka.grouptripsorganizer.email.EmailSender;
import com.sroka.grouptripsorganizer.entity.account_activation.VerificationToken;
import com.sroka.grouptripsorganizer.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
@Transactional
@RequiredArgsConstructor
public class ActivationEmailService {
    private final EmailSender emailSender;
    private final AccountActivationMailGenerator activationMailGenerator;

    private final Locale POLISH_LOCALE_LANGUAGE = Locale.forLanguageTag("pl");

    @Autowired
    @Qualifier("mailMessageSource")
    private final MessageSource messageSource;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${configuration.security.accountActivationUrl}")
    private String accountActivationUrl;

    public void sendActivationMail(VerificationToken verificationToken) {
        User user = verificationToken.getUser();
        String body = generateMailBody(user, verificationToken.getExpiryDate(), accountActivationUrl + verificationToken.getToken());
        String topic = messageSource.getMessage("mail.activation.title", null, POLISH_LOCALE_LANGUAGE);

        emailSender.send(user.getEmail(), from, topic, body, true);
    }

    private String generateMailBody(User user, LocalDateTime linkExpirationTime, String activationLink) {
        String openingName = user.getFirstName() + " " + user.getLastName();

        return activationMailGenerator.generate(openingName, activationLink, linkExpirationTime, POLISH_LOCALE_LANGUAGE);
    }
}