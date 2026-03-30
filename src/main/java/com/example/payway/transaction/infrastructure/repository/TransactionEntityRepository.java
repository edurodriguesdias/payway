package com.example.payway.transaction.infrastructure.repository;

import com.example.payway.transaction.infrastructure.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionEntityRepository extends JpaRepository<TransactionEntity, Long> {
}
