package com.example.payway.transaction.domain.usecases;

import com.example.payway.transaction.domain.ports.TransactionRepository;
import com.example.payway.transaction.domain.vo.TransactionInputVO;
import com.example.payway.transaction.domain.vo.TransactionVO;
import org.springframework.stereotype.Service;

@Service
public class TransactionUseCase {

    private final TransactionRepository transactionRepository;

    TransactionUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionVO createTransaction(TransactionInputVO transactionInputVO) {
        return transactionRepository.create(
            transactionInputVO.accountId(),
            transactionInputVO.operationTypeId(),
            transactionInputVO.amount()
        );
    }
}
