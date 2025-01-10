package com.example.WebOnlineBanking.service;

import com.example.WebOnlineBanking.model.Account;
import com.example.WebOnlineBanking.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {
    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public void deposit(Long accountId, Double amount) {
        if (amount <= 0.0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        else {
            Account account = this.accountRepository.findById(accountId).orElseThrow(() ->
                    new IllegalArgumentException("Account not found"));
            account.setBalance(account.getBalance() + amount);
            this.accountRepository.save(account);
        }
    }

    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, Double amount, String owner) {
        if (amount <= 0.0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        else if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("You cannot transfer money to the same account");
        }

        else {
            Account fromAccount = this.accountRepository.findById(fromAccountId).orElseThrow(() ->
                    new IllegalArgumentException("Source account not found"));

            Account toAccount = this.accountRepository.findById(toAccountId).orElseThrow(() ->
                    new IllegalArgumentException("Target account not found"));

            if (!fromAccount.getOwner().equals(owner)) {
                throw new SecurityException("You are not authorized to transfer from this account");
            }

            else if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
                throw new IllegalArgumentException("Transfer failed: Currencies of both accounts must match");
            }

            else if (fromAccount.getBalance() < amount) {
                throw new IllegalArgumentException("Not enough money on your balance");
            }

            else {
                fromAccount.setBalance(fromAccount.getBalance() - amount);
                toAccount.setBalance(toAccount.getBalance() + amount);
                this.accountRepository.save(fromAccount);
                this.accountRepository.save(toAccount);
            }
        }
    }
}
