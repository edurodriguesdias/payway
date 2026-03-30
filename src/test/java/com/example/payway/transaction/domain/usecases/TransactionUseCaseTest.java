package com.example.payway.transaction.domain.usecases;

import com.example.payway.account.domain.ports.AccountRepository;
import com.example.payway.account.domain.vo.AccountVO;
import com.example.payway.common.exception.NotFoundException;
import com.example.payway.transaction.domain.ports.TransactionRepository;
import com.example.payway.transaction.domain.vo.TransactionInputVO;
import com.example.payway.transaction.domain.vo.TransactionVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionUseCase transactionUseCase;

    @Test
    void shouldCreateTransactionSuccessfully() {
        Long accountId = 1L;
        Integer operationTypeId = 1;
        BigDecimal amount = new BigDecimal("123.45");
        LocalDateTime eventDate = LocalDateTime.now();

        TransactionInputVO inputVO = new TransactionInputVO(accountId, operationTypeId, amount);
        TransactionVO createdVO = new TransactionVO(1L, accountId, operationTypeId, amount, eventDate);
        AccountVO accountVO = new AccountVO(accountId, "12345678901");

        when(accountRepository.getById(accountId)).thenReturn(Optional.of(accountVO));
        when(transactionRepository.create(accountId, operationTypeId, amount))
            .thenReturn(createdVO);

        TransactionVO result = transactionUseCase.createTransaction(inputVO);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(accountId, result.accountId());
        assertEquals(operationTypeId, result.operationTypeId());
        assertEquals(amount, result.amount());
        assertNotNull(result.eventDate());

        verify(accountRepository, times(1)).getById(accountId);
        verify(transactionRepository, times(1))
            .create(accountId, operationTypeId, amount);

        verifyNoMoreInteractions(accountRepository, transactionRepository);
    }

    @Test
    void shouldThrowNotFoundException_WhenAccountDoesNotExist() {
        Long accountId = 999L;
        Integer operationTypeId = 1;
        BigDecimal amount = new BigDecimal("123.45");

        TransactionInputVO inputVO = new TransactionInputVO(accountId, operationTypeId, amount);

        when(accountRepository.getById(accountId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> transactionUseCase.createTransaction(inputVO)
        );

        assertEquals("Account not found", exception.getMessage());

        verify(accountRepository, times(1)).getById(accountId);
        verifyNoInteractions(transactionRepository);
    }
}
