package com.example.payway.transaction.infrastructure.mapper;

import com.example.payway.transaction.domain.vo.TransactionVO;
import com.example.payway.transaction.infrastructure.entity.TransactionEntity;

public class TransactionMapper {
    public static TransactionVO toDomain(TransactionEntity transactionEntity) {
        return new TransactionVO(
            transactionEntity.getId(),
            transactionEntity.getAccountId(),
            transactionEntity.getOperationTypeId(),
            transactionEntity.getAmount(),
            transactionEntity.getEventDate()
        );
    }

    public static TransactionEntity toEntity(TransactionVO transactionVO) {
        TransactionEntity transactionEntity = new TransactionEntity(
            transactionVO.accountId(),
            transactionVO.operationTypeId(),
            transactionVO.amount()
        );
        transactionEntity.setId(transactionVO.id());
        transactionEntity.setEventDate(transactionVO.eventDate());
        return transactionEntity;
    }
}
