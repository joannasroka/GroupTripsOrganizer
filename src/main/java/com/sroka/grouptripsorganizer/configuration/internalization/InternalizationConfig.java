package com.sroka.grouptripsorganizer.configuration.internalization;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import static java.util.Locale.ENGLISH;

@Configuration
public class InternalizationConfig {
    public static final String TRANSLATION_MESSAGE_SOURCE_NAME = "translationMessageSource";
    public static final String MAIL_MESSAGE_SOURCE_NAME = "mailMessageSource";

    private static final String MESSAGES_PATH = "i18n/messages";
    private static final String ERROR_MESSAGES_PATH = "i18n/errors";
    private static final String MAIL_MESSAGES_PATH = "i18n/mails";

    @Bean(name = TRANSLATION_MESSAGE_SOURCE_NAME)
    public AllKeysResourceBundleMessageSource translationMessageSource() {
        AllKeysResourceBundleMessageSource messageSource = new AllKeysResourceBundleMessageSource();
        messageSource.setBasenames(MESSAGES_PATH, ERROR_MESSAGES_PATH);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(ENGLISH);
        return messageSource;
    }

    @Bean(name = MAIL_MESSAGE_SOURCE_NAME)
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames(MAIL_MESSAGES_PATH);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(ENGLISH);
        return messageSource;
    }
}