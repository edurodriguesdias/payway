package com.example.payway.account.presentation.dto;

import com.example.payway.account.domain.vo.AccountVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateAccountRequestDTO(

        @JsonProperty("document_number")
        @NotBlank(message = "document_number must not be blank")
        @Pattern(regexp = "\\d{11,14}", message = "document_number must be between 11 and 14 digits")
        String documentNumber
) {
        public AccountVO toVO() {
                return new AccountVO(null, this.documentNumber);
        }
}