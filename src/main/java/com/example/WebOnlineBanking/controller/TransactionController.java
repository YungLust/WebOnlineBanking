package com.example.WebOnlineBanking.controller;

import com.example.WebOnlineBanking.model.User;
import com.example.WebOnlineBanking.service.TransactionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    public TransactionController() {
    }

    @PostMapping({"/account/deposit"})
    public String deposit(@RequestParam Long accountId, @RequestParam Double amount, HttpSession session) {
        try {
            this.transactionService.deposit(accountId, amount);
            session.setAttribute("message", "Deposit successful");
        } catch (Exception e) {
            session.setAttribute("message", "Deposit failed: " + e.getMessage());
        }

        return "redirect:/account";
    }

    @PostMapping({"/accounts/transfer"})
    public String transferMoney(HttpSession session, @RequestParam Long fromAccountId, @RequestParam Long toAccountId, @RequestParam Double amount) {
        User currentUser = (User)session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        else {
            try {
                this.transactionService.transfer(fromAccountId, toAccountId, amount, currentUser.getUsername());
                session.setAttribute("message", "Transfer successful");
            } catch (IllegalArgumentException e) {
                session.setAttribute("message", e.getMessage());
            } catch (Exception e) {
                session.setAttribute("message", "An unexpected error occurred: " + e.getMessage());
            }

            return "redirect:/account?accountId=" + fromAccountId;
        }
    }
}
