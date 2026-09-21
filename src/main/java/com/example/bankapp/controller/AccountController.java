package com.example.bankapp.controller;

import com.example.bankapp.model.Account;
import com.example.bankapp.model.Deposit;
import com.example.bankapp.model.Transfer;
import com.example.bankapp.model.Withdrawal;
import com.example.bankapp.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{accountID}")
    public Account getAccountById(@PathVariable Integer accountID) {
        try {
            return accountService.getAccountById(accountID);
        } catch (ResponseStatusException exception) {
            if (exception.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Access denied"
                );
            }

            throw exception;
        }
    }

    @GetMapping("/user/{userID}")
    public List<Account> getAccountsByUserId(@PathVariable Integer userID) {
        return accountService.getAccountsByUserId(userID);
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @PutMapping("/{accountID}")
    public Account updateAccount(
            @PathVariable Integer accountID,
            @RequestBody Account updatedAccount) {

        return accountService.updateAccount(accountID, updatedAccount);
    }

    @DeleteMapping("/{accountID}")
    public String deleteAccount(@PathVariable Integer accountID) {
        accountService.deleteAccount(accountID);
        return "Account deleted successfully";
    }

    @PostMapping("/{accountID}/deposit")
    public Account deposit(
            @PathVariable Integer accountID,
            @RequestBody Deposit deposit) {

        return accountService.deposit(accountID, deposit);
    }

    @PostMapping("/{accountID}/withdraw")
    public Account withdraw(
            @PathVariable Integer accountID,
            @RequestBody Withdrawal withdrawal) {

        return accountService.withdraw(accountID, withdrawal);
    }

    @PostMapping("/transfer")
    public Account transfer(@RequestBody Transfer transfer) {
        return accountService.transfer(transfer);
    }
}