package com.example.bankapp.repository;

import com.example.bankapp.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    List<Account> findByUser_UserID(Integer userID);
}