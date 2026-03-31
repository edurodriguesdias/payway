package com.example.payway.transaction.domain.vo;

import java.util.Arrays;

public enum OperationTypeEnum {

    NORMAL_PURCHASE(1, "Normal Purchase"),
    PURCHASE_WITH_INSTALLMENTS(2, "Purchase with installments"),
    WITHDRAWAL(3, "Withdrawal"),
    CREDIT_VOUCHER(4, "Credit Voucher");

    private final Integer id;
    private final String description;

    OperationTypeEnum(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public Integer getId() {
      return id;
    }

    public String getDescription() {
      return description;
    }

    public Boolean isDebit() {
        return this != CREDIT_VOUCHER;
    }

    public static OperationTypeEnum fromId(Integer id) {
        return Arrays.stream(values())
          .filter(type -> type.id.equals(id))
          .findFirst()
          .orElseThrow(() -> new IllegalArgumentException(
                  "Invalid operation type ID: " + id
          ));
    }
}
