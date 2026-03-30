package com.example.payway.common.dto.validator.documentNumber;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DocumentNumberValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DocumentValidation {
  String message() default "documentNumber should be a valid CPF or CNPJ number";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
