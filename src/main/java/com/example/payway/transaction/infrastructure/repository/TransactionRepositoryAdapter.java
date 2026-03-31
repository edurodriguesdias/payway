package com.example.payway.transaction.infrastructure.repository;

import com.example.payway.transaction.domain.ports.TransactionRepository;
import com.example.payway.transaction.domain.vo.TransactionVO;
import com.example.payway.transaction.infrastructure.entity.TransactionEntity;
import com.example.payway.transaction.infrastructure.mapper.TransactionMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class TransactionRepositoryAdapter implements TransactionRepository {

    private final TransactionEntityRepository entityRepository;

    public TransactionRepositoryAdapter(TransactionEntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public TransactionVO create(
        Long accountId,
        Integer operationTypeId,
        BigDecimal amount,
        BigDecimal balance
    ) {
        var transactionEntity = new TransactionEntity(
            accountId,
          operationTypeId,
          amount,
          balance
        );
        var savedEntity = entityRepository.save(transactionEntity);
        return TransactionMapper.toDomain(savedEntity);
    }

    public List<TransactionEntity> getOpenedTransactions(Long accountId) {
        return entityRepository.findOpenedTransactions(accountId);
    }

    public void update(TransactionEntity transaction) {
        entityRepository.save(transaction);
    }
}
