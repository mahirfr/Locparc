package com.mahir.locparc.service;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.springframework.stereotype.Service;

import static org.springframework.beans.MethodInvocationException.ERROR_CODE;

@Service
public class PasswordGenerator {

    // Random password generator
    public String generatePassayPassword() {
        org.passay.PasswordGenerator passwordGenerator = new org.passay.PasswordGenerator();

        // Régle d'au moins une lettre minuscule
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(1);

        // Régle d'au moins une lettre majuscule
        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(1);

        // Régle d'au moins un chiffre
        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(1);

        // Régle d'au moins un charactère spécial
        CharacterData specialChars = new CharacterData() {
            public String getErrorCode()  { return ERROR_CODE; }
            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };
        CharacterRule specialCharRule = new CharacterRule(specialChars);
        specialCharRule.setNumberOfCharacters(1);

        return passwordGenerator.generatePassword(10, lowerCaseRule,
                upperCaseRule, digitRule, specialCharRule);
    }
}
