package com.example.payway.account.infrastructure.repository;

import com.example.payway.account.domain.vo.AccountVO;
import com.example.payway.account.infrastructure.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountEntityRepository extends JpaRepository<AccountEntity, Long> {
  Optional<AccountVO> getByDocumentNumber(String documentNumber);
  Boolean existsByDocumentNumber(String documentNumber);
}
