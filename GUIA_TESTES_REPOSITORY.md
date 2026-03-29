# Guia: Como Testar Repositories no Spring Boot

## Visão Geral

Existem **2 abordagens principais** para testar repositories:

1. **Testes de Integração** - Com banco de dados real (em memória ou container)
2. **Testes Unitários** - Com mocks (apenas lógica do adapter)

---

## 1. Testes de Integração com Banco em Memória (✅ Recomendado)

Usa um banco de dados real para testes. É a forma mais confiável para testar repositories.

### Opção A: H2 (banco em memória - mais simples)

**Adicione no `pom.xml`:**
```xml
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <scope>test</scope>
</dependency>
```

**Exemplo de teste:**
```java
package com.example.payway.account.infrastructure.repository;

import com.example.payway.account.infrastructure.entity.AccountEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class AccountRepositoryAdapterTest {

  @Autowired
  private AccountEntityRepository entityRepository;

  private AccountRepositoryAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new AccountRepositoryAdapter(entityRepository);
  }

  @Test
  void shouldCreateAccount() {
    // When
    var result = adapter.create("12345678901");

    // Then
    assertNotNull(result);
    assertNotNull(result.id());
    assertEquals("12345678901", result.documentNumber());
  }

  @Test
  void shouldReturnAccountByDocumentNumber() {
    // Given
    adapter.create("12345678901");

    // When
    var result = adapter.getByDocumentNumber("12345678901");

    // Then
    assertTrue(result.isPresent());
    assertEquals("12345678901", result.get().documentNumber());
  }

  @Test
  void shouldReturnEmptyWhenDocumentNotFound() {
    // When
    var result = adapter.getByDocumentNumber("99999999999");

    // Then
    assertTrue(result.isEmpty());
  }

  @Test
  void shouldCheckIfDocumentExists() {
    // Given
    adapter.create("12345678901");

    // When
    var exists = adapter.isDocumentAlreadyExists("12345678901");
    var notExists = adapter.isDocumentAlreadyExists("99999999999");

    // Then
    assertTrue(exists);
    assertFalse(notExists);
  }
}
```

**Vantagens:**
- ✅ Rápido
- ✅ Fácil de configurar
- ✅ Não precisa instalar nada
- ✅ Testa queries SQL reais

**Desvantagens:**
- ⚠️ H2 não é 100% compatível com PostgreSQL
- ⚠️ Algumas queries específicas podem se comportar diferente

---

### Opção B: Testcontainers (PostgreSQL real - mais realista)

**Adicione no `pom.xml`:**
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-testcontainers</artifactId>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>org.testcontainers</groupId>
  <artifactId>postgresql</artifactId>
  <scope>test</scope>
</dependency>
```

**Exemplo de teste:**
```java
package com.example.payway.account.infrastructure.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryAdapterTest {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
    .withDatabaseName("testdb")
    .withUsername("test")
    .withPassword("test");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Autowired
  private AccountEntityRepository entityRepository;

  private AccountRepositoryAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new AccountRepositoryAdapter(entityRepository);
  }

  @Test
  void shouldCreateAccount() {
    // When
    var result = adapter.create("12345678901");

    // Then
    assertNotNull(result);
    assertNotNull(result.id());
    assertEquals("12345678901", result.documentNumber());
  }

  // ... mesmos testes da Opção A
}
```

**Vantagens:**
- ✅ Usa PostgreSQL real
- ✅ 100% compatível com produção
- ✅ Testa migrations do Flyway

**Desvantagens:**
- ⚠️ Mais lento que H2
- ⚠️ Requer Docker instalado
- ⚠️ Usa mais recursos

---

## 2. Testes Unitários com Mocks (apenas lógica do adapter)

**Quando usar:** Para testar apenas a lógica de transformação no adapter, sem banco real.

```java
package com.example.payway.account.infrastructure.repository;

import com.example.payway.account.domain.vo.AccountVO;
import com.example.payway.account.infrastructure.entity.AccountEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountRepositoryAdapterTest {

  @Mock
  private AccountEntityRepository entityRepository;

  @InjectMocks
  private AccountRepositoryAdapter adapter;

  @Test
  void shouldCreateAccount() {
    // Given
    var entity = new AccountEntity("12345678901");
    entity.setId(1L);
    when(entityRepository.save(any(AccountEntity.class))).thenReturn(entity);

    // When
    var result = adapter.create("12345678901");

    // Then
    assertNotNull(result);
    assertEquals(1L, result.id());
    assertEquals("12345678901", result.documentNumber());
    verify(entityRepository, times(1)).save(any(AccountEntity.class));
  }

  @Test
  void shouldReturnAccountByDocumentNumber() {
    // Given
    var accountVO = new AccountVO(1L, "12345678901");
    when(entityRepository.getByDocumentNumber("12345678901"))
      .thenReturn(Optional.of(accountVO));

    // When
    var result = adapter.getByDocumentNumber("12345678901");

    // Then
    assertTrue(result.isPresent());
    assertEquals("12345678901", result.get().documentNumber());
    verify(entityRepository).getByDocumentNumber("12345678901");
  }

  @Test
  void shouldCheckIfDocumentExists() {
    // Given
    when(entityRepository.existsByDocumentNumber("12345678901")).thenReturn(true);

    // When
    var exists = adapter.isDocumentAlreadyExists("12345678901");

    // Then
    assertTrue(exists);
    verify(entityRepository).existsByDocumentNumber("12345678901");
  }
}
```

**Vantagens:**
- ✅ Muito rápido
- ✅ Não precisa de banco
- ✅ Testa isoladamente

**Desvantagens:**
- ⚠️ Não testa queries SQL reais
- ⚠️ Não detecta problemas de mapeamento JPA
- ⚠️ Não testa migrations

---

## Comparação e Recomendação

| Abordagem | Velocidade | Confiabilidade | Complexidade | Quando usar |
|-----------|------------|----------------|--------------|-------------|
| **H2** | ⚡⚡⚡ | ⭐⭐⭐ | 🟢 Baixa | **Recomendado para começar** |
| **Testcontainers** | ⚡⚡ | ⭐⭐⭐⭐⭐ | 🟡 Média | Queries complexas, CI/CD |
| **Mocks** | ⚡⚡⚡⚡ | ⭐⭐ | 🟢 Baixa | Apenas lógica do adapter |

### Minha Recomendação

1. **Comece com H2** - É o melhor custo-benefício
2. **Use Testcontainers** se tiver queries SQL específicas do PostgreSQL
3. **Use Mocks** apenas para testar lógica de transformação (não substitui testes de integração)

---

## Problema Identificado no Código

⚠️ **Atenção:** O método `getByDocumentNumber` no `AccountEntityRepository` tem um problema:

```java
// ❌ ERRADO - Repository não deve retornar VO diretamente
Optional<AccountVO> getByDocumentNumber(String documentNumber);

// ✅ CORRETO - Repository deve retornar Entity
Optional<AccountEntity> findByDocumentNumber(String documentNumber);
```

**Correção necessária:**

No `AccountEntityRepository.java`:
```java
Optional<AccountEntity> findByDocumentNumber(String documentNumber);
```

No `AccountRepositoryAdapter.java`:
```java
@Override
public Optional<AccountVO> getByDocumentNumber(String documentNumber) {
  return entityRepository.findByDocumentNumber(documentNumber)
    .map(AccountMapper::toDomain);
}
```

---

## Configuração para Testes com H2

Se for usar H2, crie: `src/test/resources/application-test.properties`

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

---

## Para Começar Agora

1. Adicione H2 no `pom.xml`
2. Corrija o `AccountEntityRepository` (retornar `AccountEntity` em vez de `AccountVO`)
3. Crie os testes com `@DataJpaTest`
4. Execute: `mvn test`

**Dúvidas?** Consulte a documentação oficial:
- H2: https://www.h2database.com/
- Testcontainers: https://testcontainers.com/
- Spring Data JPA Testing: https://docs.spring.io/spring-boot/docs/current/reference/html/data.html#data.sql.jpa-and-spring-data
