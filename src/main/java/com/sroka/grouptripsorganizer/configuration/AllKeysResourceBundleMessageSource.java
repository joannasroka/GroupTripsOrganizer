package com.sroka.grouptripsorganizer.configuration;

import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class AllKeysResourceBundleMessageSource extends ResourceBundleMessageSource {
    public Set<String> getKeys(Locale locale) {
        return getBasenameSet().stream()
                .flatMap(basename -> getResourceBundle(basename, locale).keySet().stream())
                .collect(Collectors.toSet());
    }
}