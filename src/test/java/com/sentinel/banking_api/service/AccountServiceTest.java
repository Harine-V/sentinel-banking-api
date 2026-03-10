package com.sentinel.banking_api.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sentinel.banking_api.model.Account;
import com.sentinel.banking_api.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void testDeposit() {
        Account initialAccount=new Account(1L, "Henry", 1000.0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(initialAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(initialAccount);
        Account result=accountService.deposit(1L, 500.0);
        assertEquals(1500.0, result.getBalance());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testWithdraw() {
        Account initialAcccount=new Account(1L, "Henry", 1000.0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(initialAcccount));
        when(accountRepository.save(any(Account.class))).thenReturn(initialAcccount);
        Account result=accountService.withdraw(1L,400.0);
        assertNotNull(result);
        assertEquals(600.0,result.getBalance());
        verify(accountRepository,times(1)).save(any(Account.class));
    }

    @Test
    public void testTransfer() {
        Account sender=new Account(1L, "Henry", 1000.0);
        Account receiver =new Account(2L, "Sarah",500.0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(accountRepository.existsById(2L)).thenReturn(true);
        when(accountRepository.save(any(Account.class))).thenReturn(sender);
        accountService.transfer(1L,2L,300.0);
        assertEquals(700,sender.getBalance());
        assertEquals(800,receiver.getBalance());
        verify(accountRepository,times(2)).save(any(Account.class));
    }
}
