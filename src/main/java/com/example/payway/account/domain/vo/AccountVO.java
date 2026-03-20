package com.example.payway.account.domain.vo;

public record AccountVO(
        Long id,
        String documentNumber
) {
    @Override
    public Long id() {
        return this.id;
    }

    @Override
    public String documentNumber() {
        return this.documentNumber;
    }
}
