package com.sroka.grouptripsorganizer.validate;

import lombok.NoArgsConstructor;
import org.passay.CharacterData;
import org.passay.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
public class CustomPasswordValidator {
    public static final String SPECIAL_CHARACTERS = "!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~";
    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_MAX_LENGTH = 60;
    private static final int LOWER_CASE_CHARACTERS_AMOUNT = 1;
    private static final int UPPER_CASE_CHARACTERS_AMOUNT = 1;
    private static final int SPECIAL_CHARACTERS_AMOUNT = 1;
    private static final int DIGITS_AMOUNT = 1;

    private static PasswordValidator PASSWORD_VALIDATOR;
    private static LengthRule PASSWORD_LENGTH_RULE;
    private static CharacterData LOWER_CASE_CHARS;
    private static CharacterRule LOWER_CASE_RULE;
    private static CharacterData UPPER_CASE_CHARS;
    private static CharacterRule UPPER_CASE_RULE;
    private static CharacterData DIGIT_CHARS;
    private static CharacterRule DIGIT_RULE;
    private static CharacterRule SPECIAL_CHAR_RULE;

    static {
        PASSWORD_LENGTH_RULE = new LengthRule(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH);

        LOWER_CASE_CHARS = EnglishCharacterData.LowerCase;
        LOWER_CASE_RULE = new CharacterRule(LOWER_CASE_CHARS);
        LOWER_CASE_RULE.setNumberOfCharacters(LOWER_CASE_CHARACTERS_AMOUNT);

        UPPER_CASE_CHARS = EnglishCharacterData.UpperCase;
        UPPER_CASE_RULE = new CharacterRule(UPPER_CASE_CHARS);
        UPPER_CASE_RULE.setNumberOfCharacters(UPPER_CASE_CHARACTERS_AMOUNT);

        DIGIT_CHARS = EnglishCharacterData.Digit;
        DIGIT_RULE = new CharacterRule(DIGIT_CHARS);
        DIGIT_RULE.setNumberOfCharacters(SPECIAL_CHARACTERS_AMOUNT);

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return "ERROR_CODE";
            }

            public String getCharacters() {
                return SPECIAL_CHARACTERS;
            }
        };
        SPECIAL_CHAR_RULE = new CharacterRule(specialChars);
        SPECIAL_CHAR_RULE.setNumberOfCharacters(DIGITS_AMOUNT);

        PASSWORD_VALIDATOR = new PasswordValidator(List.of(UPPER_CASE_RULE, LOWER_CASE_RULE, DIGIT_RULE, SPECIAL_CHAR_RULE, PASSWORD_LENGTH_RULE));
    }

    public boolean validate(String password) {
        return PASSWORD_VALIDATOR.validate(new PasswordData(password)).isValid();
    }
}