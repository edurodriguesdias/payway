package com.example.payway.account.infrastructure.mapper;

import com.example.payway.account.domain.vo.AccountVO;
import com.example.payway.account.infrastructure.entity.AccountEntity;

public class AccountMapper {
    public static AccountVO toDomain(AccountEntity accountEntity) {
        return new AccountVO(
          accountEntity.getId(),
          accountEntity.getDocumentNumber()
        );
    }

    public static AccountEntity toEntity(AccountVO accountVO) {
        AccountEntity accountEntity = new AccountEntity(accountVO.documentNumber());
        accountEntity.setId(accountVO.id());
        return accountEntity;
    }
}
