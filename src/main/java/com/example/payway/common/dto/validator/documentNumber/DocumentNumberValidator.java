package com.example.payway.common.dto.validator.documentNumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DocumentNumberValidator implements ConstraintValidator<DocumentValidation, String> {

  private final CPFValidator cpfValidator = new CPFValidator();
  private final CNPJValidator cnpjValidator = new CNPJValidator();

  @Override
  public void initialize(DocumentValidation constraintAnnotation) {
    cpfValidator.initialize(constraintAnnotation);
    cnpjValidator.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isBlank()) {
      return true;
    }

    String cleanedValue = value.replaceAll("\\D", "");

    if (cleanedValue.length() == 11) {
      return cpfValidator.isValid(value, context);
    }

    if (cleanedValue.length() == 14) {
      return cnpjValidator.isValid(value, context);
    }

    return false;
  }
}
