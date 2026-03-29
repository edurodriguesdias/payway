package com.example.payway.account.infrastructure.mapper;

import com.example.payway.account.domain.vo.AccountVO;
import com.example.payway.account.infrastructure.entity.AccountEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountMapperTest {

  @Test
  void shouldReturnDomain_WhenEntityGiven() {
    Long accountId = 1L;
    String documentNumber = "12345678901";

    AccountEntity accountEntity = new AccountEntity(documentNumber);
    accountEntity.setId(accountId);

    var domain = AccountMapper.toDomain(accountEntity);

    assertInstanceOf(AccountVO.class, domain);
    assertEquals(documentNumber, domain.documentNumber());
  }

  @Test
  void shouldReturnEntity_WhenDomainGiven() {
    Long accountId = 1L;
    String documentNumber = "12345678901";

    var accountVO = new AccountVO(accountId, documentNumber);
    var entity = AccountMapper.toEntity(accountVO);

    assertInstanceOf(AccountEntity.class, entity);
    assertEquals(documentNumber, entity.getDocumentNumber());
  }

  @Test
  void shouldCreateAnMapperInstance() {
    var mapper = new AccountMapper();

    assertInstanceOf(AccountMapper.class, mapper);
  }
}
