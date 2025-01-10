package com.example.WebOnlineBanking.service;

import com.example.WebOnlineBanking.model.Account;
import com.example.WebOnlineBanking.repository.AccountRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public AccountService() {
    }

    public Map<String, Object> prepareAccountView(String currentUser, Long accountId) {
        List<Account> accounts = this.getAccountsByOwner(currentUser);
        Map<String, Object> modelData = new HashMap<>();
        modelData.put("accounts", accounts);

        if (accountId == null && !accounts.isEmpty()) {
            modelData.put("account", accounts.getFirst());
        }
        else if (accountId != null) {
            accounts.stream().filter((account) -> account.getId().equals(accountId)).
                    findFirst().ifPresent((account) -> modelData.put("account", account));
        }

        return modelData;
    }

    public List<Account> getAccountsByOwner(String owner) {
        return this.accountRepository.findByOwner(owner);
    }

    public void createNewAccount(String owner, String currency, int initialBalance) {
        Account account = new Account();
        account.setOwner(owner.toLowerCase());
        account.setCurrency(currency);
        account.setBalance(initialBalance);
        this.accountRepository.save(account);
    }


    @Transactional
    public void deleteAccount(Long accountId, String owner) {
        Account account = this.accountRepository.findById(accountId).orElseThrow(() ->
                new IllegalArgumentException("Account not found"));
        if (!account.getOwner().equals(owner)) {
            throw new SecurityException("You are not allowed to delete this account");
        } else {
            this.accountRepository.delete(account);
        }
    }
}
