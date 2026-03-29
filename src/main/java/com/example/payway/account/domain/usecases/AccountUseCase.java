package com.example.payway.account.domain.usecases;

import com.example.payway.account.domain.ports.AccountRepository;
import com.example.payway.account.domain.vo.AccountInputVO;
import com.example.payway.account.domain.vo.AccountVO;
import com.example.payway.common.exception.BusinessException;
import com.example.payway.common.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountUseCase {

    private final AccountRepository accountRepository;

    AccountUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountVO createAccount(AccountInputVO accountInputVO) {

        if (accountRepository.isDocumentAlreadyExists(accountInputVO.documentNumber())) {
          throw new BusinessException("Document number already exists");
        }

        return accountRepository.create(accountInputVO.documentNumber());
    }

    public AccountVO getAccount(String documentNumber) {

        Optional<AccountVO> account = accountRepository.getByDocumentNumber(documentNumber);

        if (account.isEmpty()) {
						throw new NotFoundException("Document number not found!");
        }

        return account.get();
    }
}