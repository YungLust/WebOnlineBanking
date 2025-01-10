package com.example.WebOnlineBanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebOnlineBanking {
    public WebOnlineBanking() {
    }

    public static void main(String[] args) {
        SpringApplication.run(WebOnlineBanking.class, args);
        System.out.println("working...");
    }
}
