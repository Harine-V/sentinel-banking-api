package com.sentinel.banking_api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

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

    @NotBlank(message = "Account holder name is required")
    private String accountHolderName;

    @JsonProperty("balance")
    @NotNull(message = "Balance cannot be null")
    @Min(value =0, message="Balance cannot be negative")
    private double balance;

    @Column(columnDefinition = "boolean default true")
    private boolean active=true;

    @Column(nullable = false, columnDefinition = "DOUBLE PRECISION DEFAULT 0.07")
    private double interestRate=0.07;

    public Account() {}

    public Account(Long id, String accountHolderName, Double balance) {
    this.id = id;
    this.accountHolderName = accountHolderName;
    this.balance = balance;
    }

    public Long getId() {return id;}
    public String getAccountHolderName() { return accountHolderName; }
    public double getBalance() { return balance;}
    public boolean isActive() { return active; }

    public void setId(Long id) {this.id=id;}
    public void setAccountHolderName(String accountHolderName) {this.accountHolderName=accountHolderName;}
    public void setBalance(double balance) {this.balance=balance;}
    public void setActive(boolean active) { this.active = active; }
}
