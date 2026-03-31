package com.example.payway.transaction.domain.ports;

import com.example.payway.transaction.domain.vo.TransactionVO;
import com.example.payway.transaction.infrastructure.entity.TransactionEntity;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository {
    TransactionVO create(
        Long accountId,
        Integer operationTypeId,
        BigDecimal amount,
        BigDecimal balance
    );

    List<TransactionEntity> getOpenedTransactions(Long accountId);

    void update(TransactionEntity transaction);
}
