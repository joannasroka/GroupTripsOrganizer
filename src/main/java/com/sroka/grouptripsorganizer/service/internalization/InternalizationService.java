package com.sroka.grouptripsorganizer.service.internalization;

import com.sroka.grouptripsorganizer.configuration.internalization.AllKeysResourceBundleMessageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.sroka.grouptripsorganizer.configuration.internalization.InternalizationConfig.TRANSLATION_MESSAGE_SOURCE_NAME;

@Service
@RequiredArgsConstructor
public class InternalizationService {

    @Autowired
    @Qualifier(TRANSLATION_MESSAGE_SOURCE_NAME)
    private AllKeysResourceBundleMessageSource messageSource;

    public Map<String, String> getTranslations(Locale locale) {
        return messageSource.getKeys(locale).stream()
                .collect(Collectors.toMap(Function.identity(), key -> messageSource.getMessage(key, null, locale)));
    }
}