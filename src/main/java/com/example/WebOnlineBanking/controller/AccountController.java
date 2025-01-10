package com.example.WebOnlineBanking.controller;

import com.example.WebOnlineBanking.model.User;
import com.example.WebOnlineBanking.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {
    @Autowired
    private AccountService accountService;

    public AccountController() {
    }

    @GetMapping({"/account"})
    public String viewAccount(HttpSession session, @RequestParam(required = false) Long accountId, Model model) {
        User currentUser = (User)session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        else {
            model.addAllAttributes(this.accountService.prepareAccountView(currentUser.getUsername(), accountId));
            String message = (String)session.getAttribute("message");
            if (message != null) {
                model.addAttribute("message", message);
                session.removeAttribute("message");
            }

            return "account";
        }
    }

    @GetMapping({"/accounts/create"})
    public String showCreateAccountPage() {
        return "create-account";
    }

    @PostMapping({"/accounts/create"})
    public String createAccount(HttpSession session, @RequestParam String currency, @RequestParam int initialBalance) {
        User currentUser = (User)session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        else {
            this.accountService.createNewAccount(currentUser.getUsername(), currency, initialBalance);
            return "redirect:/account";
        }
    }

    @PostMapping({"/accounts/delete"})
    public String deleteAccount(HttpSession session, @RequestParam Long accountId) {
        User currentUser = (User)session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        else {
            try {
                this.accountService.deleteAccount(accountId, currentUser.getUsername());
                session.setAttribute("message", "Account deleted successfully");
            } catch (Exception e) {
                session.setAttribute("message", "Failed to delete account: " + e.getMessage());
            }

            return "redirect:/account";
        }
    }
}
