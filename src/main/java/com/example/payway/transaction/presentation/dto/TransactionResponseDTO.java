package com.example.payway.transaction.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ResponseBody
public record TransactionResponseDTO(
    Long id,
    @JsonProperty("account_id")
    Long accountId,
    @JsonProperty("operation_type_id")
    Integer operationTypeId,
    BigDecimal amount,
    @JsonProperty("event_date")
    LocalDateTime eventDate
) {
    public static TransactionResponseDTO build(
        Long id,
        Long accountId,
        Integer operationTypeId,
        BigDecimal amount,
        LocalDateTime eventDate
    ) {
        return new TransactionResponseDTO(
            id,
            accountId,
            operationTypeId,
            amount,
            eventDate
        );
    }
}
