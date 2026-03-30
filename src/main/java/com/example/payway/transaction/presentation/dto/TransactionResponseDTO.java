package com.example.payway.transaction.presentation.dto;

import com.example.payway.transaction.domain.vo.OperationTypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ResponseBody
public record TransactionResponseDTO(
    Long id,
    @JsonProperty("account_id")
    Long accountId,
    @JsonProperty("operation")
    OperationResponseDTO operation,
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
        var operationType = OperationTypeEnum.fromId(operationTypeId);
        var operationResponse = new OperationResponseDTO(
            operationType.getId(),
            operationType.getDescription()
        );

        return new TransactionResponseDTO(
            id,
            accountId,
            operationResponse,
            amount,
            eventDate
        );
    }
}
