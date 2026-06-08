package com.preethan.banking_finance_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Create account
    @PostMapping("/create")
    public Account createAccount(@RequestBody Account account) {
        account.setAccountNumber("ACC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        account.setBalance(0.0);
        return accountRepository.save(account);
    }

    // Get account by ID
    @GetMapping("/{id}")
    public Account getAccount(@PathVariable Long id) {
        return accountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    // Deposit money
    @PostMapping("/{id}/deposit")
    public Account deposit(@PathVariable Long id, @RequestParam Double amount) {
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction tx = new Transaction();
        tx.setAccountNumber(account.getAccountNumber());
        tx.setType("DEPOSIT");
        tx.setAmount(amount);
        tx.setBalanceAfter(account.getBalance());
        tx.setTimestamp(LocalDateTime.now());
        transactionRepository.save(tx);

        return account;
    }

    // Withdraw money
    @PostMapping("/{id}/withdraw")
    public Account withdraw(@PathVariable Long id, @RequestParam Double amount) {
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Account not found"));
        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Transaction tx = new Transaction();
        tx.setAccountNumber(account.getAccountNumber());
        tx.setType("WITHDRAWAL");
        tx.setAmount(amount);
        tx.setBalanceAfter(account.getBalance());
        tx.setTimestamp(LocalDateTime.now());
        transactionRepository.save(tx);

        return account;
    }

    // Transaction history
    @GetMapping("/{id}/transactions")
    public List<Transaction> getTransactions(@PathVariable Long id) {
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Account not found"));
        return transactionRepository.findByAccountNumberOrderByTimestampDesc(account.getAccountNumber());
    }
}