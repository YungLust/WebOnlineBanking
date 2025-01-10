package com.example.WebOnlineBanking.repository;

import com.example.WebOnlineBanking.model.Account;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByOwner(String owner);
}
