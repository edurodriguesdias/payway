package com.example.payway.transaction.domain.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionVO(
    Long id,
    Long accountId,
    Integer operationTypeId,
    BigDecimal amount,
    LocalDateTime eventDate
) {
}
