package com.example.payway.transaction.presentation;

import com.example.payway.transaction.domain.usecases.TransactionUseCase;
import com.example.payway.transaction.domain.vo.OperationTypeEnum;
import com.example.payway.transaction.domain.vo.TransactionInputVO;
import com.example.payway.transaction.domain.vo.TransactionVO;
import com.example.payway.transaction.presentation.dto.CreateTransactionRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionUseCase transactionUseCase;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    void shouldCreateTransactionSuccessfully() {
        Long accountId = 1L;
        Integer operationTypeId = 1;
        BigDecimal amount = new BigDecimal("123.45");
        LocalDateTime eventDate = LocalDateTime.now();

        CreateTransactionRequestDTO requestDTO = new CreateTransactionRequestDTO(
            accountId,
            operationTypeId,
            amount
        );

        TransactionVO transactionVO = new TransactionVO(
            1L,
            accountId,
            operationTypeId,
            amount,
            eventDate
        );

        when(transactionUseCase.createTransaction(any(TransactionInputVO.class)))
            .thenReturn(transactionVO);

        var response = transactionController.createTransaction(requestDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        assertEquals(accountId, response.getBody().accountId());
        assertEquals(operationTypeId, response.getBody().operation().id());
        assertEquals(amount, response.getBody().amount());
        assertNotNull(response.getBody().eventDate());

        verify(transactionUseCase, times(1))
            .createTransaction(any(TransactionInputVO.class));
    }
}
