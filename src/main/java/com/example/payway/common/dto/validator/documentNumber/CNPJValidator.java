package com.example.payway.common.dto.validator.documentNumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CNPJValidator implements ConstraintValidator<DocumentValidation, String> {

    @Override
    public void initialize(DocumentValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(String cnpj, ConstraintValidatorContext context) {
        if (cnpj == null || cnpj.isBlank()) {
            return true;
        }

        String cleanedCnpj = cnpj.replaceAll("\\D", "");

        if (cleanedCnpj.length() != 14) {
            return false;
        }

        if (cleanedCnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        return isValidCNPJDigits(cleanedCnpj);
    }


    private boolean isValidCNPJDigits(String cnpj) {
        int firstDigit = calculateFirstDigit(cnpj.substring(0, 12));

        int secondDigit = calculateSecondDigit(cnpj.substring(0, 13));

        return cnpj.charAt(12) == Character.forDigit(firstDigit, 10)
                && cnpj.charAt(13) == Character.forDigit(secondDigit, 10);
    }

    private int calculateFirstDigit(String str) {
        int[] weights = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        return calculateDigit(str, weights);
    }

    private int calculateSecondDigit(String str) {
        int[] weights = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        return calculateDigit(str, weights);
    }

    private int calculateDigit(String str, int[] weights) {
        int sum = 0;

        for (int i = 0; i < str.length(); i++) {
            int digit = Character.getNumericValue(str.charAt(i));
            sum += digit * weights[i];
        }

        int remainder = sum % 11;

        return remainder < 2 ? 0 : 11 - remainder;
    }
}
