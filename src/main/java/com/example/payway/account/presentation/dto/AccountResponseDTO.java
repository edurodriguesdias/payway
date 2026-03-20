package com.example.payway.account.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public record AccountResponseDTO(
        Long id,
        @JsonProperty("document_number")
        String documentNumber
) {

    public static AccountResponseDTO build(Long id, String documentNumber) {
        return new AccountResponseDTO(
                id,
                documentNumber
        );
    }
}
