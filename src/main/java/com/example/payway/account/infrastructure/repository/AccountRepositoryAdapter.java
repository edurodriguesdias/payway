package com.example.payway.account.infrastructure.repository;

import com.example.payway.account.domain.ports.AccountRepository;
import com.example.payway.account.domain.vo.AccountVO;
import com.example.payway.account.infrastructure.entity.AccountEntity;
import com.example.payway.account.infrastructure.mapper.AccountMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AccountRepositoryAdapter implements AccountRepository {

    private final AccountEntityRepository entityRepository;

    public AccountRepositoryAdapter(AccountEntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public AccountVO create(String documentNumber) {
        var accountEntity = new AccountEntity(documentNumber);
        var savedEntity = entityRepository.save(accountEntity);
        return AccountMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<AccountVO> getById(Long id) {
        return entityRepository.findById(id)
                .map(AccountMapper::toDomain);
    }

    @Override
    public Optional<AccountVO> getByDocumentNumber(String documentNumber) {
        return entityRepository.getByDocumentNumber(documentNumber);
    }

    @Override
    public Boolean isDocumentAlreadyExists(String documentNumber) {
        return entityRepository.existsByDocumentNumber(documentNumber);
    }
}
