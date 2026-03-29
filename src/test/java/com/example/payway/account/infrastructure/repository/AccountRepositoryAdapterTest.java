package com.example.payway.account.infrastructure.repository;

import com.example.payway.account.infrastructure.entity.AccountEntity;
import com.example.payway.account.infrastructure.mapper.AccountMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountRepositoryAdapterTest {

  @Mock
  private AccountEntityRepository entityRepository;

  @InjectMocks
  private AccountRepositoryAdapter adapter;

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void shouldCreateAnAccount_WhenDataIsValid() {
    String documentNumber = "12345678901";
    var entity = new AccountEntity(documentNumber);
    entity.setId(1L);

    when(entityRepository.save(any())).thenReturn(entity);

    var result = adapter.create(documentNumber);

    assertEquals(1L, result.id());
    verify(entityRepository, times(1)).save(any());
  }

  @Test
  void shouldReturnAnAccount_WhenAccountExists() {
    var documentNumber = "12345678901";
    var entity = new AccountEntity(documentNumber);
    entity.setId(1L);

    var accountVO = AccountMapper.toDomain(entity);

    when(entityRepository.getByDocumentNumber(documentNumber))
      .thenReturn(Optional.of(accountVO));

    var result = adapter.getByDocumentNumber(documentNumber);

    assertEquals(documentNumber, result.get().documentNumber());
    assertEquals(1L, result.get().id());
    assertInstanceOf(Optional.class, result);
  }

  @Test
  void shouldReturnTrueWhenDocumentAlreadyExists() {
    var documentNumber = "12345678901";

    when(entityRepository.existsByDocumentNumber(documentNumber))
      .thenReturn(true);

    var result = adapter.isDocumentAlreadyExists(documentNumber);

    assertTrue(result);

    verify(entityRepository, times(1))
      .existsByDocumentNumber(documentNumber);
  }

  @Test
  void shouldReturnFalseWhenDocumentAlreadyExists() {
    var documentNumber = "12345678901";

    when(entityRepository.existsByDocumentNumber(documentNumber))
      .thenReturn(false);

    var result = adapter.isDocumentAlreadyExists(documentNumber);

    assertFalse(result);

    verify(entityRepository, times(1))
      .existsByDocumentNumber(documentNumber);
  }
}
