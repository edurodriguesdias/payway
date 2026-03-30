package com.example.payway.transaction.domain.vo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class OperationTypeEnumTest {

    @ParameterizedTest
    @CsvSource({
            "1, NORMAL_PURCHASE, Normal Purchase",
            "2, PURCHASE_WITH_INSTALLMENTS, Purchase with installments",
            "3, WITHDRAWAL, Withdrawal",
            "4, CREDIT_VOUCHER, Credit Voucher"
    })
    void shouldMapIdToCorrectOperationType(Integer id, String expectedEnum, String expectedDescription) {
        OperationTypeEnum operationTypeEnum = OperationTypeEnum.fromId(id);

        assertEquals(expectedEnum, operationTypeEnum.name());
        assertEquals(id, operationTypeEnum.getId());
        assertEquals(expectedDescription, operationTypeEnum.getDescription());
    }

    @Test
    void shouldThrowExceptionForInvalidId() {
        Integer invalidId = 999;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> OperationTypeEnum.fromId(invalidId)
        );

        assertEquals("Invalid operation type ID: 999", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForNullId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> OperationTypeEnum.fromId(null)
        );
    }

    @Test
    void shouldHaveExactlyFourOperationTypes() {
        assertEquals(4, OperationTypeEnum.values().length);
    }

    @Test
    void shouldGetCorrectIdForNormalPurchase() {
        assertEquals(1, OperationTypeEnum.NORMAL_PURCHASE.getId());
    }

    @Test
    void shouldGetCorrectDescriptionForWithdrawal() {
        assertEquals("Withdrawal", OperationTypeEnum.WITHDRAWAL.getDescription());
    }
}
