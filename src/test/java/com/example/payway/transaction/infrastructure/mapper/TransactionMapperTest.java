package com.example.payway.transaction.infrastructure.mapper;

import com.example.payway.transaction.domain.vo.TransactionVO;
import com.example.payway.transaction.infrastructure.entity.TransactionEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionMapperTest {

    @Test
    void shouldReturnDomain_WhenEntityGiven() {
        Long transactionId = 1L;
        Long accountId = 1L;
        Integer operationTypeId = 1;
        BigDecimal amount = new BigDecimal("123.45");

        TransactionEntity entity = new TransactionEntity(accountId, operationTypeId, amount);
        entity.setId(transactionId);

        var domain = TransactionMapper.toDomain(entity);

        assertInstanceOf(TransactionVO.class, domain);
        assertEquals(transactionId, domain.id());
        assertEquals(accountId, domain.accountId());
        assertEquals(operationTypeId, domain.operationTypeId());
        assertEquals(amount, domain.amount());
        assertNotNull(domain.eventDate());
    }

    @Test
    void shouldReturnEntity_WhenDomainGiven() {
        Long transactionId = 1L;
        Long accountId = 1L;
        Integer operationTypeId = 1;
        BigDecimal amount = new BigDecimal("123.45");
        LocalDateTime eventDate = LocalDateTime.now();

        var transactionVO = new TransactionVO(transactionId, accountId, operationTypeId, amount, eventDate);
        var entity = TransactionMapper.toEntity(transactionVO);

        assertInstanceOf(TransactionEntity.class, entity);
        assertEquals(transactionId, entity.getId());
        assertEquals(accountId, entity.getAccountId());
        assertEquals(operationTypeId, entity.getOperationTypeId());
        assertEquals(amount, entity.getAmount());
        assertEquals(eventDate, entity.getEventDate());
    }

    @Test
    void shouldCreateAMapperInstance() {
        var mapper = new TransactionMapper();

        assertInstanceOf(TransactionMapper.class, mapper);
    }
}
