package com.example.payway.transaction.presentation.dto;

import com.example.payway.transaction.domain.vo.OperationTypeEnum;
import com.example.payway.transaction.domain.vo.TransactionInputVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateTransactionRequestDTO(
    @JsonProperty("account_id")
    @NotNull(message = "account_id must not be null")
    @Positive(message = "account_id must be positive")
    Long accountId,

    @JsonProperty("operation_type_id")
    @NotNull(message = "operation_type_id must not be null")
    @Positive(message = "operation_type_id must be positive")
    Integer operationTypeId,

    @NotNull(message = "amount must not be null")
    @Positive(message = "amount must be positive")
    BigDecimal amount
) {
    public TransactionInputVO toVO() {
        OperationTypeEnum.fromId(this.operationTypeId);

        return new TransactionInputVO(
            this.accountId,
            this.operationTypeId,
            this.amount
        );
    }
}
