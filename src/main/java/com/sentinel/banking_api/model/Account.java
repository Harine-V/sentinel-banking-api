package com.sentinel.banking_api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "accounts")
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(name = "account_holder_name")
    @JsonProperty("accountHolderName")
    private String accountHolderName;

    @JsonProperty("balance")
    private double balance;

    public Account() {}

    public Long getId() {return id;}
    public String getAccountHolderName() { return accountHolderName; }
    public double getBalance() { return balance;}

    public void setId(Long id) {this.id=id;}
    public void setAccountHolderName(String accountHolderName) {this.accountHolderName=accountHolderName;}
    public void setBalance(double balance) {this.balance=balance;}
}
