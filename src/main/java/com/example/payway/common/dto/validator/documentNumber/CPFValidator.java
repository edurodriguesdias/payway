package com.example.payway.common.dto.validator.documentNumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador de CPF (Cadastro de Pessoa Física).
 * CPF é um documento brasileiro com 11 dígitos numéricos.
 */
public class CPFValidator implements ConstraintValidator<DocumentValidation, String> {

    @Override
    public void initialize(DocumentValidation constraintAnnotation) {}

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.isBlank()) {
            return true;
        }

        String cleanedCpf = cpf.replaceAll("\\D", "");

        if (cleanedCpf.length() != 11) {
            return false;
        }

        if (cleanedCpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        return isValidCPFDigits(cleanedCpf);
    }

    private boolean isValidCPFDigits(String cpf) {
        int firstDigit = calculateDigit(cpf.substring(0, 9), 10);
        int secondDigit = calculateDigit(cpf.substring(0, 10), 11);
        return cpf.charAt(9) == Character.forDigit(firstDigit, 10)
                && cpf.charAt(10) == Character.forDigit(secondDigit, 10);
    }

    private int calculateDigit(String str, int weight) {
        int sum = 0;

        for (int i = 0; i < str.length(); i++) {
            int digit = Character.getNumericValue(str.charAt(i));
            sum += digit * weight--;
        }

        int remainder = sum % 11;

        return remainder < 2 ? 0 : 11 - remainder;
    }
}
