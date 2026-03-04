package com.sentinel.banking_api.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sentinel.banking_api.model.Account;
import com.sentinel.banking_api.service.AccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
    

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @DeleteMapping("/{id}")
    public String deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return "Account with ID "+id+" Account deleted successfully";
    }
    
    @PutMapping("/{id}/deposit")
    public Account deposit(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        Double amount=request.get("amount");
        return accountService.deposit(id, amount);
    }

    @PutMapping("/{id}/withdraw")
    public Account withdraw(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        Double amount=request.get("amount");
        return accountService.withdraw(id, amount);
    }

    @PostMapping("/transfer")
    public String transfer(@RequestBody Map<String, Object> request) {
        Long fromId=((Number) request.get("fromId")).longValue();
        Long toId=((Number) request.get("toId")).longValue();
        double amount=((Number) request.get("amount")).doubleValue();
        accountService.transfer(fromId,toId,amount);
        return "Transfer successful";
    }
    
}