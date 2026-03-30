package com.example.payway.account.presentation.dto;

import com.example.payway.account.domain.vo.AccountInputVO;
import com.example.payway.common.dto.validator.documentNumber.DocumentValidation;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequestDTO(
    @JsonProperty("document_number")
    @NotBlank(message = "document_number must not be blank")
    @DocumentValidation
    String documentNumber
) {
        public AccountInputVO toDomain() {
            return new AccountInputVO(this.documentNumber);
        }
}