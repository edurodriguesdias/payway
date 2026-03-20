package com.example.payway.account.domain.useCase;

import com.example.payway.account.domain.ports.AccountRepository;
import com.example.payway.account.domain.vo.AccountVO;
import com.example.payway.common.exception.BusinessException;
import com.example.payway.common.exception.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountUseCase {

    private final AccountRepository accountRepository;

    AccountUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountVO createAccount(AccountVO accountVO) {

        if (this.isDocumentNumberExists(accountVO.documentNumber())) {
          throw new BusinessException("Document number already exists");
        }

        var account = accountRepository.create(accountVO.documentNumber());

        return new AccountVO(
            account.id(),
            account.documentNumber()
        );
    }

    public AccountVO getAccount(String documentNumber) {

        var account = accountRepository.getByDocumentNumber(documentNumber);

        if (account.isEmpty()) {
            throw new NotFoundException("Document number not found!");
        }

        return new AccountVO(
            account.get().id(),
            account.get().documentNumber()
        );
    }

    private boolean isDocumentNumberExists(String documentNumber) {
        var account = accountRepository.getByDocumentNumber(documentNumber);
        return account != null;
    }
}