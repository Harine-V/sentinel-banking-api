package com.sentinel.banking_api.service;

import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import com.sentinel.banking_api.model.Account;
import com.sentinel.banking_api.repository.AccountRepository;
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
        .orElseThrow(() -> new RuntimeException("Account not found"));
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
    public void transfer(Long fronId, Long toId, double amount) {
        withdraw(fronId,amount);
        deposit(toId,amount);
    }
    
}
