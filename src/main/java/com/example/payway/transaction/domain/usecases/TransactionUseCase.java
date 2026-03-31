package com.example.payway.transaction.domain.usecases;

import com.example.payway.account.domain.ports.AccountRepository;
import com.example.payway.common.exception.NotFoundException;
import com.example.payway.transaction.domain.ports.TransactionRepository;
import com.example.payway.transaction.domain.vo.OperationTypeEnum;
import com.example.payway.transaction.domain.vo.TransactionInputVO;
import com.example.payway.transaction.domain.vo.TransactionVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

        OperationTypeEnum operationType = OperationTypeEnum.fromId(transactionInputVO.operationTypeId());

        BigDecimal amountNormalized = transactionInputVO.amount();
        BigDecimal balance;

        if (operationType.isDebit()) {
            amountNormalized = transactionInputVO.amount().negate();
            balance = amountNormalized;

            return transactionRepository.create(
                transactionInputVO.accountId(),
                transactionInputVO.operationTypeId(),
                amountNormalized,
                balance
            );
        }

        BigDecimal remainingAmount = transactionInputVO.amount();
        var openedTransactions = transactionRepository.getOpenedTransactions(transactionInputVO.accountId());

        for (var transaction : openedTransactions) {
            if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            BigDecimal debtAmount = transaction.getBalance().abs();

            if (remainingAmount.compareTo(debtAmount) >= 0) {
                transaction.setBalance(BigDecimal.ZERO);
                transactionRepository.update(transaction);

                remainingAmount = remainingAmount.subtract(debtAmount);
            } else {
                BigDecimal newBalance = transaction.getBalance().add(remainingAmount);
                transaction.setBalance(newBalance);
                transactionRepository.update(transaction);

                remainingAmount = BigDecimal.ZERO;
            }
        }

        return transactionRepository.create(
            transactionInputVO.accountId(),
            transactionInputVO.operationTypeId(),
            amountNormalized,
            remainingAmount
        );

    }
}
