package com.example.bankapp.controller;

import com.example.bankapp.model.Account;
import com.example.bankapp.model.Deposit;
import com.example.bankapp.model.Transfer;
import com.example.bankapp.model.Withdrawal;
import com.example.bankapp.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return accountService.getAccountById(accountID);
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