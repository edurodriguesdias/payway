package com.example.payway.transaction.domain.ports;

import com.example.payway.transaction.domain.vo.TransactionVO;

public interface TransactionRepository {
    TransactionVO create(Long accountId, Integer operationTypeId, java.math.BigDecimal amount);
}
