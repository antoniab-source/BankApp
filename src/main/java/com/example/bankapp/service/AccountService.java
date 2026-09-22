package com.example.bankapp.service;

import com.example.bankapp.model.Account;
import com.example.bankapp.model.Deposit;
import com.example.bankapp.model.Transaction;
import com.example.bankapp.model.Transfer;
import com.example.bankapp.model.User;
import com.example.bankapp.model.Withdrawal;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.repository.TransactionRepository;
import com.example.bankapp.security.AuthorizationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final AuthorizationService authorizationService;

    public AccountService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            UserService userService,
            AuthorizationService authorizationService) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
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

        return accountRepository.findByUser_UserID(
                user.getUserID()
        );
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

    String accountType = account.getAccountType();

    if (accountType == null
            || accountType.isBlank()) {

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Account type is required"
        );
    }

    if (!accountType.equals("Checking")
            && !accountType.equals("Savings")) {

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid account type"
        );
    }

    if (accountRepository.existsByUser_UserIDAndAccountType(
            user.getUserID(),
            accountType)) {

        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "You already have a " + accountType + " account"
        );
    }

    BigDecimal initialDeposit = account.getBalance();

    if (initialDeposit == null) {
        initialDeposit = BigDecimal.ZERO;
    }

    if (initialDeposit.compareTo(BigDecimal.ZERO) < 0) {

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Initial deposit cannot be negative"
        );
    }

    account.setAccountID(null);
    account.setAccountNumber(
            generateUniqueAccountNumber()
    );
    account.setBalance(initialDeposit);
    account.setUser(user);

    Account savedAccount = accountRepository.save(account);

    if (initialDeposit.compareTo(BigDecimal.ZERO) > 0) {

        Transaction transaction = new Transaction(
                "DEPOSIT",
                initialDeposit,
                LocalDateTime.now(),
                savedAccount
        );

        transactionRepository.save(transaction);
    }

    return savedAccount;
}

    @Transactional
public Account createAccountForRegistration(
        User user,
        String accountType,
        BigDecimal initialDeposit) {

    if (accountType == null
            || accountType.isBlank()) {

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Account type is required"
        );
    }

    if (!accountType.equals("Checking")
            && !accountType.equals("Savings")) {

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid account type"
        );
    }

    if (accountRepository.existsByUser_UserIDAndAccountType(
            user.getUserID(),
            accountType)) {

        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "You already have a " + accountType + " account"
        );
    }

    if (initialDeposit == null) {
        initialDeposit = BigDecimal.ZERO;
    }

    if (initialDeposit.compareTo(BigDecimal.ZERO) < 0) {

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Initial deposit cannot be negative"
        );
    }

    Account account = new Account();

    account.setAccountID(null);
    account.setAccountNumber(generateUniqueAccountNumber());
    account.setBalance(initialDeposit);
    account.setAccountType(accountType);
    account.setUser(user);

    Account savedAccount = accountRepository.save(account);

    if (initialDeposit.compareTo(BigDecimal.ZERO) > 0) {

        Transaction transaction = new Transaction(
                "DEPOSIT",
                initialDeposit,
                LocalDateTime.now(),
                savedAccount
        );

        transactionRepository.save(transaction);
    }

    return savedAccount;
}

    private String generateUniqueAccountNumber() {

        String accountNumber;

        do {
            accountNumber = String.valueOf(
                    ThreadLocalRandom.current()
                            .nextLong(100000000L, 1000000000L)
            );
        } while (
                accountRepository.existsByAccountNumber(accountNumber)
        );

        return accountNumber;
    }

    public Account updateAccount(
            Integer accountID,
            Account updatedAccount) {

        Account account = getAccountById(accountID);

        account.setAccountNumber(
                updatedAccount.getAccountNumber()
        );

        account.setBalance(
                updatedAccount.getBalance()
        );

        account.setAccountType(
                updatedAccount.getAccountType()
        );

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

    authorizationService.requireAccountAccess(account);

    if (account.getBalance() == null
            || account.getBalance().compareTo(BigDecimal.ZERO) != 0) {

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Account must have a $0.00 balance before it can be closed"
        );
    }

    List<Transaction> transactions =
            transactionRepository
                    .findByAccount_AccountIDOrderByTransactionDateDesc(
                            accountID
                    );

    transactionRepository.deleteAll(transactions);

    accountRepository.delete(account);
}

    @Transactional
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

        Account savedAccount = accountRepository.save(account);

        Transaction transaction = new Transaction(
                "DEPOSIT",
                amount,
                LocalDateTime.now(),
                account
        );

        transactionRepository.save(transaction);

        return savedAccount;
    }

    @Transactional
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

        Account savedAccount = accountRepository.save(account);

        Transaction transaction = new Transaction(
                "WITHDRAWAL",
                amount.negate(),
                LocalDateTime.now(),
                account
        );

        transactionRepository.save(transaction);

        return savedAccount;
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

        LocalDateTime transactionDate = LocalDateTime.now();

        Transaction outgoingTransaction = new Transaction(
                "TRANSFER",
                amount.negate(),
                transactionDate,
                fromAccount
        );

        Transaction incomingTransaction = new Transaction(
                "TRANSFER",
                amount,
                transactionDate,
                toAccount
        );

        transactionRepository.save(outgoingTransaction);
        transactionRepository.save(incomingTransaction);

        return fromAccount;
    }
}