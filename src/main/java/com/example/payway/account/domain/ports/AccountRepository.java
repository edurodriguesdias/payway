package com.example.payway.account.domain.ports;

import com.example.payway.account.domain.vo.AccountVO;

import java.util.Optional;

public interface AccountRepository {
  AccountVO create(String documentNumber);
  Optional<AccountVO> getByDocumentNumber(String documentNumber);
  Boolean isDocumentAlreadyExists(String documentNumber);
}
