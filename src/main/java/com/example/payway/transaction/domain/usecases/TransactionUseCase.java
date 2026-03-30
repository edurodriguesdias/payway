package com.example.payway.transaction.domain.usecases;

import com.example.payway.account.domain.ports.AccountRepository;
import com.example.payway.common.exception.NotFoundException;
import com.example.payway.transaction.domain.ports.TransactionRepository;
import com.example.payway.transaction.domain.vo.TransactionInputVO;
import com.example.payway.transaction.domain.vo.TransactionVO;
import org.springframework.stereotype.Service;

@Service
public class TransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    TransactionUseCase(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public TransactionVO createTransaction(TransactionInputVO transactionInputVO) {
        accountRepository.getById(transactionInputVO.accountId())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        return transactionRepository.create(
            transactionInputVO.accountId(),
            transactionInputVO.operationTypeId(),
            transactionInputVO.amount()
        );
    }
}
