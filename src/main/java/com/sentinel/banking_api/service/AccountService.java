package com.sentinel.banking_api.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sentinel.banking_api.model.Account;
import com.sentinel.banking_api.repository.AccountRepository;
import com.sentinel.banking_api.exception.AccountNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
        .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: "+id));
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public void deleteAccount(Long id) {
        Account account=accountRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Account not found"));
        if(account.getBalance()>0)
            throw new RuntimeException("Cannot delete account with a remaining balance");
        accountRepository.delete(account);
        }

    public Account deposit(Long id, double amount) {
        Account account=accountRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Account not found"));
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        return accountRepository.save(account);
    }

    public Account withdraw(Long id, double amount) {
        Account account=accountRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Account not found"));
        if(account.getBalance() < amount)
            throw new RuntimeException("Insufficient funds! You only have "+account.getBalance());
        double newBalance = account.getBalance() - amount;
        account.setBalance(newBalance);
        return accountRepository.save(account);
    }

    @Transactional
    public void transfer(Long fromId, Long toId, double amount) {
        if(!accountRepository.existsById(toId)) {
            throw new RuntimeException("Receiver account not found!");
        }
        withdraw(fromId,amount);
        deposit(toId,amount);
    } 
}