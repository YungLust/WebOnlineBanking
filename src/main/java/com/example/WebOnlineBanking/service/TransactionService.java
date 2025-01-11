package com.example.WebOnlineBanking.service;

import com.example.WebOnlineBanking.model.Account;
import com.example.WebOnlineBanking.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Objects;

@Service
public class TransactionService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CurrencyExchangeService currencyExchangeService;

    @Transactional
    public void deposit(Long accountId, int amount) {
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

    //TODO: change int to double
    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, int amount, String owner) {
        //check if we trolled
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        else if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("You cannot transfer money to the same account");
        }

        else { //if we didnt get trolled find from/to accounts
            Account fromAccount = this.accountRepository.findById(fromAccountId).orElseThrow(() ->
                    new IllegalArgumentException("Source account not found"));
            Account toAccount = this.accountRepository.findById(toAccountId).orElseThrow(() ->
                    new IllegalArgumentException("Target account not found"));

            //try to exchange currencies if needed
            int currencyAmount = amount;
            String fromCurrency = fromAccount.getCurrency();
            String toCurrency = toAccount.getCurrency();
            if (!fromCurrency.equals(toCurrency)){
                try{
                    currencyAmount = currencyExchangeService.exchange(fromCurrency, toCurrency, amount);
                }
                catch (IOException e){
                    throw new SecurityException("Currency exchange service not working");
                }
            }

            //another verifications if smth fucks up
            if (!fromAccount.getOwner().equals(owner)) {
                throw new SecurityException("You are not authorized to transfer from this account");
            }
            else if (fromAccount.getBalance() < amount) {
                throw new IllegalArgumentException("Not enough money on your balance");
            }

            else {
                // subtract amount from origin card w/o currency exchange
                fromAccount.setBalance(fromAccount.getBalance() - amount);

                //add amount to another card with their currency
                toAccount.setBalance(toAccount.getBalance() + currencyAmount);

                this.accountRepository.save(fromAccount);
                this.accountRepository.save(toAccount);
            }
        }
    }
}
