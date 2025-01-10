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
public class RegistrationController {
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;

    public RegistrationController() {
    }

    @GetMapping({"/register"})
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping({"/register"})
    public String registerUser(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        try {
            User user = this.userService.register(username, password);
            session.setAttribute("currentUser", user);
            return "redirect:/accounts/create";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "register";
        }
    }
}
