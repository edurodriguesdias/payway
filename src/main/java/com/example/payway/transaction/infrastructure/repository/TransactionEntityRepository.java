package com.example.payway.transaction.infrastructure.repository;

import com.example.payway.transaction.infrastructure.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionEntityRepository extends JpaRepository<TransactionEntity, Long> {
  @Query("SELECT t FROM TransactionEntity t WHERE t.accountId = ?1 AND t.balance < 0 ORDER BY t.eventDate ASC")
  List<TransactionEntity> findOpenedTransactions(Long accountId);
}
