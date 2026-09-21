package com.example.bankapp.controller;

import com.example.bankapp.dto.TransactionResponse;
import com.example.bankapp.model.Account;
import com.example.bankapp.model.Transaction;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.repository.TransactionRepository;
import com.example.bankapp.security.AuthorizationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AuthorizationService authorizationService;

    public TransactionController(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository,
            AuthorizationService authorizationService) {

        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.authorizationService = authorizationService;
    }

    @GetMapping("/{accountID}/transactions")
    public List<TransactionResponse> getTransactions(
            @PathVariable Integer accountID) {

        Account account = accountRepository.findById(accountID)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        authorizationService.requireAccountAccess(account);

        List<Transaction> transactions =
                transactionRepository
                        .findByAccount_AccountIDOrderByTransactionDateDesc(
                                accountID
                        );

        return transactions.stream()
                .map(transaction ->
                        new TransactionResponse(
                                transaction.getTransactionID(),
                                transaction.getTransactionType(),
                                transaction.getAmount(),
                                transaction.getTransactionDate()
                        )
                )
                .toList();
    }
}