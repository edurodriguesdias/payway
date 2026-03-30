package com.example.payway.transaction.domain.usecases;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

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

        when(transactionRepository.create(accountId, operationTypeId, amount))
            .thenReturn(createdVO);

        TransactionVO result = transactionUseCase.createTransaction(inputVO);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(accountId, result.accountId());
        assertEquals(operationTypeId, result.operationTypeId());
        assertEquals(amount, result.amount());
        assertNotNull(result.eventDate());

        verify(transactionRepository, times(1))
            .create(accountId, operationTypeId, amount);

        verifyNoMoreInteractions(transactionRepository);
    }
}
