package com.sroka.grouptripsorganizer.controller.internalization;

import com.sroka.grouptripsorganizer.service.internalization.InternalizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static java.util.Locale.forLanguageTag;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/translations")
public class InternalizationController {

    private final InternalizationService internalizationService;

    @GetMapping
    public Map<String, String> getTranslations(@RequestParam(defaultValue = "en", required = false) String lang) {
        return internalizationService.getTranslations(forLanguageTag(lang));
    }
}
