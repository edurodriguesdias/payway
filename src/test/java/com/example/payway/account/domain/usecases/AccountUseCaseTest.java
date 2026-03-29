package com.example.payway.account.domain.usecases;

import com.example.payway.account.domain.ports.AccountRepository;
import com.example.payway.account.domain.vo.AccountInputVO;
import com.example.payway.account.domain.vo.AccountVO;
import com.example.payway.common.exception.BusinessException;
import com.example.payway.common.exception.NotFoundException;
import com.example.payway.support.base.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountUseCaseTest extends BaseUnitTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountUseCase accountUseCase;

    @BeforeEach
    void setUp() {
    }

    @ParameterizedTest
    @MethodSource("dataProviderValidDocumentNumbers")
    void shouldCreateAccountSuccessfully_WhenDocumentNumberDoesNotExist(String documentNumber) {

        Long expectedId = 1L;

        AccountInputVO inputAccountVO = new AccountInputVO(documentNumber);
        AccountVO createdAccountVO = new AccountVO(expectedId, documentNumber);

        when(accountRepository.isDocumentAlreadyExists(documentNumber))
            .thenReturn(false);

        when(accountRepository.create(documentNumber))
            .thenReturn(createdAccountVO);

        AccountVO result = accountUseCase.createAccount(inputAccountVO);

        assertNotNull(result);
        assertEquals(expectedId, result.id());
        assertEquals(documentNumber, result.documentNumber());

        verify(accountRepository, times(1))
            .isDocumentAlreadyExists(documentNumber);

        verify(accountRepository, times(1))
            .create(documentNumber);

        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void shouldThrowBusinessException_WhenDocumentNumberAlreadyExists() {
       String registeredDocument = "12345678901";

       AccountInputVO accountInputVO = new AccountInputVO(registeredDocument);

       when(accountRepository.isDocumentAlreadyExists(registeredDocument))
           .thenReturn(true);

       assertThrows(BusinessException.class, () -> {
            accountUseCase.createAccount(accountInputVO);
       });

       verify(accountRepository, times(1))
           .isDocumentAlreadyExists(registeredDocument);
    }

    @Test
    void shouldReturnAnAccount_WhenDocumentNumberExists() {
        Long id = 1L;
        String documentNumber = "12345678901";

        AccountVO expectedAccountVO = new AccountVO(id, documentNumber);

        when(accountRepository.getByDocumentNumber(documentNumber))
            .thenReturn(Optional.of(expectedAccountVO));

        AccountVO accountVO = accountUseCase.getAccount(documentNumber);

        assertEquals(documentNumber, accountVO.documentNumber());
        assertEquals(id, accountVO.id());

        verify(accountRepository, times(1))
            .getByDocumentNumber(documentNumber);
    }

    @Test
    void shouldThrowException_WhenDocumentNumberDoesNotExists() {
        String wrongDocumentNumber = "12312312322";

        when(accountRepository.getByDocumentNumber(wrongDocumentNumber))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            accountUseCase.getAccount(wrongDocumentNumber);
        });

        verify(accountRepository, times(1))
            .getByDocumentNumber(wrongDocumentNumber);
    }

    static Stream<String> dataProviderValidDocumentNumbers () {
        return Stream.of(
            "12345678901",
            "98765432100",
            "11122233344",
            "80365614000106",
            "77660618000102"
        );
    }
}
