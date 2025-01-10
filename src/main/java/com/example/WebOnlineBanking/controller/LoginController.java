package com.example.WebOnlineBanking.controller;

import com.example.WebOnlineBanking.model.User;
import com.example.WebOnlineBanking.service.AccountService;
import com.example.WebOnlineBanking.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;

    public LoginController() {
    }

    @GetMapping({"/login"})
    public String showLoginPage() {
        return "login";
    }

    @PostMapping({"/login"})
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        try {
            User user = this.userService.authenticate(username, password);
            session.setAttribute("currentUser", user);
            return this.accountService.getAccountsByOwner(user.getUsername()).isEmpty() ? "redirect:/accounts/create" : "redirect:/account";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "login";
        }
    }

    @GetMapping({"/logout"})
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
