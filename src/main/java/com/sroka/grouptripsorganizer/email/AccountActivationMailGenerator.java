package com.sroka.grouptripsorganizer.email;


import com.sroka.grouptripsorganizer.utils.LocalDateTimeFormatter;
import com.sroka.grouptripsorganizer.utils.ResourceReader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Locale;

import static com.sroka.grouptripsorganizer.configuration.internalization.InternalizationConfig.MAIL_MESSAGE_SOURCE_NAME;
import static java.text.MessageFormat.format;

@Component
@RequiredArgsConstructor
public class AccountActivationMailGenerator {
    private final String SUBJECT_KEY = "mail.activation.subject";
    private final String OPENING_KEY = "mail.opening";
    private final String BODY_KEY = "mail.activation.body";
    private final String LINK_DESCRIPTION_KEY = "mail.activation.linkDescription";
    private final String LINK_EXPIRATION_INFO_KEY = "mail.activation.linkExpirationInfo";
    private final String CLOSING_KEY = "mail.closing";
    private final String COMPANY_NAME_KEY = "mail.companyName";

    @Autowired
    @Qualifier(MAIL_MESSAGE_SOURCE_NAME)
    private final MessageSource messageSource;

    @Value("classpath:/mail/account_activation_mail.html")
    private Resource mailResource;

    private String mail;

    @PostConstruct
    private void init() {
        mail = ResourceReader.asString(mailResource);
    }

    public String generate(String openingName, String activationLink, LocalDateTime linkExpirationTime, Locale locale) {
        return replaceMailWithTranslations(openingName, activationLink, linkExpirationTime, locale, mail);
    }

    private String replaceMailWithTranslations(String openingName, String activationLink, LocalDateTime linkExpirationTime, Locale locale, String mail) {
        String subject = messageSource.getMessage(SUBJECT_KEY, null, locale);
        String opening = messageSource.getMessage(OPENING_KEY, new Object[]{openingName}, locale);
        String body = messageSource.getMessage(BODY_KEY, null, locale);
        String linkDescription = messageSource.getMessage(LINK_DESCRIPTION_KEY, null, locale);
        String linkExpiration = messageSource.getMessage(LINK_EXPIRATION_INFO_KEY, new Object[]{LocalDateTimeFormatter.format(linkExpirationTime)}, locale);
        String closing = messageSource.getMessage(CLOSING_KEY, null, locale);
        String companyName = messageSource.getMessage(COMPANY_NAME_KEY, null, locale);

        return format(mail, subject, opening, body, activationLink, linkDescription, linkExpiration, closing, companyName);
    }
}