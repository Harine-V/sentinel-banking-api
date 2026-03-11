package com.sentinel.banking_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sentinel.banking_api.model.Transaction;
import java.util.List;
public interface TransactionRepository extends JpaRepository<Transaction,Long> { 
    List<Transaction> findByAccountId(Long accountId);
}
