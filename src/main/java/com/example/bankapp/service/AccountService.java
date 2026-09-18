package com.example.bankapp.service;

import com.example.bankapp.model.Account;
import com.example.bankapp.model.Deposit;
import com.example.bankapp.model.Transfer;
import com.example.bankapp.model.User;
import com.example.bankapp.model.Withdrawal;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    private final UserService userService;

    private List<Account> accounts = new ArrayList<>(List.of(
            createAccountWithUser(1, "123456789", new BigDecimal("1000.00"), "Checking", 1),
            createAccountWithUser(4, "789123456", new BigDecimal("1500.00"), "Savings", 2)
    ));

    public AccountService(UserService userService) {
        this.userService = userService;
    }

    private Account createAccountWithUser(
            Integer accountID,
            String accountNumber,
            BigDecimal balance,
            String accountType,
            Integer userID) {

        Account account = new Account(accountID, accountNumber, balance, accountType);
        account.setUserID(userID);
        return account;
    }

    public List<Account> getAllAccounts() {
        return accounts;
    }

    public Account getAccountById(Integer accountID) {
        for (Account account : accounts) {
            if (account.getAccountID().equals(accountID)) {
                return account;
            }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Account not found"
        );
    }

    public List<Account> getAccountsByUserId(Integer userID) {
        User user = userService.getUserById(userID);

        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found"
            );
        }

        List<Account> userAccounts = new ArrayList<>();

        for (Account account : accounts) {
            if (account.getUserID().equals(userID)) {
                userAccounts.add(account);
            }
        }

        return userAccounts;
    }

    public Account createAccount(Account account) {
        User user = userService.getUserById(account.getUserID());

        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found"
            );
        }

        Integer newAccountID = accounts.stream()
                .map(Account::getAccountID)
                .max(Integer::compareTo)
                .orElse(0) + 1;

        account.setAccountID(newAccountID);
        accounts.add(account);

        return account;
    }

    public Account updateAccount(Integer accountID, Account updatedAccount) {
        for (Account account : accounts) {
            if (account.getAccountID().equals(accountID)) {
                account.setAccountNumber(updatedAccount.getAccountNumber());
                account.setBalance(updatedAccount.getBalance());
                account.setAccountType(updatedAccount.getAccountType());

                if (updatedAccount.getUserID() != null) {
                    User user = userService.getUserById(updatedAccount.getUserID());

                    if (user == null) {
                        throw new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User not found"
                        );
                    }

                    account.setUserID(updatedAccount.getUserID());
                }

                return account;
            }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Account not found"
        );
    }

    public void deleteAccount(Integer accountID) {
        for (Account account : accounts) {
            if (account.getAccountID().equals(accountID)) {
                accounts.remove(account);
                return;
            }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Account not found"
        );
    }

    public Account deposit(Integer accountID, Deposit deposit) {
        Account account = getAccountById(accountID);

        BigDecimal amount = deposit.getAmount();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Deposit amount must be greater than zero"
            );
        }

        account.setBalance(account.getBalance().add(amount));

        return account;
    }

    public Account withdraw(Integer accountID, Withdrawal withdrawal) {
        Account account = getAccountById(accountID);

        BigDecimal amount = withdrawal.getAmount();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Withdrawal amount must be greater than zero"
            );
        }

        if (amount.compareTo(account.getBalance()) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Insufficient funds"
            );
        }

        account.setBalance(account.getBalance().subtract(amount));

        return account;
    }

    public Account transfer(Transfer transfer) {
        Account fromAccount = getAccountById(transfer.getFromAccountID());
        Account toAccount = getAccountById(transfer.getToAccountID());

        BigDecimal amount = transfer.getAmount();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Transfer amount must be greater than zero"
            );
        }

        if (amount.compareTo(fromAccount.getBalance()) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Insufficient funds"
            );
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        return fromAccount;
    }
}
