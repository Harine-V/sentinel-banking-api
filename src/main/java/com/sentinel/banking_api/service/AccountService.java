package com.sentinel.banking_api.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.sentinel.banking_api.exception.AccountNotFoundException;
import com.sentinel.banking_api.model.*;
import com.sentinel.banking_api.repository.*;

import jakarta.transaction.Transactional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
        .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: "+id));
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findByActiveTrue();
    }

    public void deleteAccount(Long id) {
        Account account=accountRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Account not found"));
        if(account.getBalance()>0)
            throw new RuntimeException("Cannot delete account with a remaining balance");
        account.setActive(false);
        accountRepository.save(account);
        }

    public Account deposit(Long id, double amount) {

        Account account=accountRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Account not found"));
        
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        
        Transaction transaction=new Transaction(
            id,
            amount,
            TransactionType.DEPOSIT,
            "Manual Deposit",
            LocalDateTime.now()
        );
        transactionRepository.save(transaction);
        return accountRepository.save(account);
    }

    public Account withdraw(Long id, double amount) {
        
        Account account=accountRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Account not found"));
        
        if(account.getBalance() < amount)
            throw new RuntimeException("Insufficient funds! You only have "+account.getBalance());
        
        double newBalance = account.getBalance() - amount;
        account.setBalance(newBalance);
        
        Transaction transaction=new Transaction(
            id,
            -amount,
            TransactionType.WITHDRAWAL,
            "Manual Withdraw",
            LocalDateTime.now()
        );
        transactionRepository.save(transaction);
        return accountRepository.save(account);
    }

    @Transactional
    public void transfer(Long fromId, Long toId, double amount) {
        
        if(!accountRepository.existsById(toId)) {
            throw new RuntimeException("Receiver account not found!");
        }
        withdraw(fromId,amount);
        deposit(toId,amount);

        Transaction transactionFrom=new Transaction(
            fromId,
            -amount,
            TransactionType.TRANSFER,
            "Transfer to Account: "+toId,
            LocalDateTime.now()
        );

        Transaction transactionTo=new Transaction(
            toId,
            amount,
            TransactionType.TRANSFER,
            "Received from Account: "+fromId,
            LocalDateTime.now()
        );
        transactionRepository.save(transactionFrom);
        transactionRepository.save(transactionTo);
    } 

    public void calculateInterest() {
        List<Account> activeAccounts=accountRepository.findByActiveTrue();
        for(Account account: activeAccounts) {
            double currentBalance=account.getBalance();
            double rate=account.getInterestRate();
            double interestEarned=currentBalance*rate;
            account.setBalance(currentBalance+interestEarned);
            accountRepository.save(account);

            Transaction interestTx=new Transaction(
            account.getId(),
            interestEarned,
            TransactionType.INTEREST,
            "Monthly Interest Earned at "+(rate*100)+"%",
            LocalDateTime.now()
        );
        transactionRepository.save(interestTx);
        }
    }

    public List<Transaction> getAccountHistory(Long accountId) {
        return transactionRepository.findByAccountIdOrderByTimestampDesc(accountId);
    }
}