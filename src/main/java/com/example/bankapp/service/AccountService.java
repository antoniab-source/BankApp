package com.example.bankapp.service;

import com.example.bankapp.model.Account;
import com.example.bankapp.model.Deposit;
import com.example.bankapp.model.Transfer;
import com.example.bankapp.model.User;
import com.example.bankapp.model.Withdrawal;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.security.AuthorizationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserService userService;
    private final AuthorizationService authorizationService;

    public AccountService(
            AccountRepository accountRepository,
            UserService userService,
            AuthorizationService authorizationService) {

        this.accountRepository = accountRepository;
        this.userService = userService;
        this.authorizationService = authorizationService;
    }

    public List<Account> getAllAccounts() {
        if (authorizationService.isAdmin()) {
            return accountRepository.findAll();
        }

        User user = userService.getUserByUsername(
                authorizationService.getCurrentUsername()
        );

        return accountRepository.findByUser_UserID(user.getUserID());
    }

    public Account getAccountById(Integer accountID) {
        Account account = accountRepository.findById(accountID)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account not found"
                ));

        authorizationService.requireAccountAccess(account);

        return account;
    }

    public List<Account> getAccountsByUserId(Integer userID) {
        User user = userService.getUserById(userID);

        return accountRepository.findByUser_UserID(
                user.getUserID()
        );
    }

    public Account createAccount(Account account) {
        if (account.getUser() == null
                || account.getUser().getUserID() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "User is required"
            );
        }

        User user = userService.getUserById(
                account.getUser().getUserID()
        );

        account.setAccountID(null);
        account.setUser(user);

        return accountRepository.save(account);
    }

    public Account updateAccount(
            Integer accountID,
            Account updatedAccount) {

        Account account = getAccountById(accountID);

        account.setAccountNumber(
                updatedAccount.getAccountNumber()
        );

        account.setBalance(updatedAccount.getBalance());
        account.setAccountType(updatedAccount.getAccountType());

        if (updatedAccount.getUser() != null
                && updatedAccount.getUser().getUserID() != null) {

            User user = userService.getUserById(
                    updatedAccount.getUser().getUserID()
            );

            account.setUser(user);
        }

        return accountRepository.save(account);
    }

    public void deleteAccount(Integer accountID) {
        Account account = getAccountById(accountID);
        accountRepository.delete(account);
    }

    public Account deposit(
            Integer accountID,
            Deposit deposit) {

        Account account = getAccountById(accountID);

        BigDecimal amount = deposit.getAmount();

        if (amount == null
                || amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Deposit amount must be greater than zero"
            );
        }

        account.setBalance(
                account.getBalance().add(amount)
        );

        return accountRepository.save(account);
    }

    public Account withdraw(
            Integer accountID,
            Withdrawal withdrawal) {

        Account account = getAccountById(accountID);

        BigDecimal amount = withdrawal.getAmount();

        if (amount == null
                || amount.compareTo(BigDecimal.ZERO) <= 0) {

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

        account.setBalance(
                account.getBalance().subtract(amount)
        );

        return accountRepository.save(account);
    }

    @Transactional
    public Account transfer(Transfer transfer) {

        Account fromAccount =
                getAccountById(transfer.getFromAccountID());

        Account toAccount =
                getAccountById(transfer.getToAccountID());

        BigDecimal amount = transfer.getAmount();

        if (amount == null
                || amount.compareTo(BigDecimal.ZERO) <= 0) {

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

        fromAccount.setBalance(
                fromAccount.getBalance().subtract(amount)
        );

        toAccount.setBalance(
                toAccount.getBalance().add(amount)
        );

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return fromAccount;
    }
}