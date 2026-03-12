package com.sentinel.banking_api.model;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private Long accountId;
    private double amount;
    public String description;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public Transaction() {}

    public Transaction(Long accountId, double amount, TransactionType type, String description, java.time.LocalDateTime timestamp) {
    this.accountId = accountId;
    this.amount = amount;
    this.type = type;
    this.description = description;
    this.timestamp = timestamp;
}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    } 
}