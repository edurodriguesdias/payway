package com.example.payway.transaction.domain.vo;

import java.math.BigDecimal;

public record TransactionInputVO(
    Long accountId,
    Integer operationTypeId,
    BigDecimal amount
) {
}
