import { useEffect, useState } from "react";

function AccountDetails({ account, setCurrentPage }) {
  const [currentAccount, setCurrentAccount] = useState(account);
  const [accounts, setAccounts] = useState([]);
  const [transactions, setTransactions] = useState([]);

  const [showDeposit, setShowDeposit] = useState(false);
  const [showWithdraw, setShowWithdraw] = useState(false);
  const [showTransfer, setShowTransfer] = useState(false);
  const [showAllTransactions, setShowAllTransactions] = useState(false);

  const [depositAmount, setDepositAmount] = useState("");
  const [withdrawAmount, setWithdrawAmount] = useState("");
  const [transferAmount, setTransferAmount] = useState("");
  const [destinationAccountID, setDestinationAccountID] = useState("");

  const [transactionLoading, setTransactionLoading] = useState(true);
  const [transactionError, setTransactionError] = useState("");

  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    const getAccounts = async () => {
      try {
        const token = localStorage.getItem("token");

        if (!token) {
          return;
        }

        const response = await fetch(
          "http://localhost:8080/api/accounts",
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (response.ok) {
          const data = await response.json();
          setAccounts(data);
        }
      } catch (error) {
        console.error("Failed to load accounts.", error);
      }
    };

    getAccounts();
  }, []);

  const getTransactions = async () => {
    try {
      setTransactionLoading(true);
      setTransactionError("");

      const token = localStorage.getItem("token");

      if (!token) {
        throw new Error("Please log in first.");
      }

      const response = await fetch(
        `http://localhost:8080/api/accounts/${currentAccount.accountID}/transactions`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!response.ok) {
        if (response.status === 401) {
          throw new Error(
            "Your login has expired. Please log in again."
          );
        }

        if (response.status === 403) {
          throw new Error(
            "You are not authorized to view these transactions."
          );
        }

        throw new Error("Failed to load transactions.");
      }

      const data = await response.json();
      setTransactions(data);
    } catch (error) {
      setTransactionError(error.message);
    } finally {
      setTransactionLoading(false);
    }
  };

  useEffect(() => {
    if (currentAccount) {
      getTransactions();
    }
  }, [currentAccount?.accountID]);

  if (!currentAccount) {
    return (
      <main>
        <h2>Account Not Found</h2>

        <p>
          We could not find the account you selected.
        </p>

        <button onClick={() => setCurrentPage("Accounts")}>
          Back to My Accounts
        </button>
      </main>
    );
  }

  const clearMessages = () => {
    setMessage("");
    setError("");
  };

  const handleDeposit = async (event) => {
    event.preventDefault();

    try {
      clearMessages();

      const token = localStorage.getItem("token");

      if (!token) {
        throw new Error("Please log in first.");
      }

      const response = await fetch(
        `http://localhost:8080/api/accounts/${currentAccount.accountID}/deposit`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            amount: Number(depositAmount),
          }),
        }
      );

      if (!response.ok) {
        if (response.status === 401) {
          throw new Error(
            "Your login has expired. Please log in again."
          );
        }

        if (response.status === 403) {
          throw new Error(
            "You are not authorized to deposit into this account."
          );
        }

        throw new Error("Deposit failed.");
      }

      const updatedAccount = await response.json();

      setCurrentAccount(updatedAccount);
      setDepositAmount("");
      setShowDeposit(false);

      setMessage(
        `Deposit successful. New balance: $${Number(
          updatedAccount.balance
        ).toFixed(2)}`
      );

      await getTransactions();
    } catch (error) {
      setError(error.message);
    }
  };

  const handleWithdraw = async (event) => {
    event.preventDefault();

    try {
      clearMessages();

      const token = localStorage.getItem("token");

      if (!token) {
        throw new Error("Please log in first.");
      }

      const response = await fetch(
        `http://localhost:8080/api/accounts/${currentAccount.accountID}/withdraw`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            amount: Number(withdrawAmount),
          }),
        }
      );

      if (!response.ok) {
        if (response.status === 401) {
          throw new Error(
            "Your login has expired. Please log in again."
          );
        }

        if (response.status === 403) {
          throw new Error(
            "You are not authorized to withdraw from this account."
          );
        }

        throw new Error("Withdrawal failed.");
      }

      const updatedAccount = await response.json();

      setCurrentAccount(updatedAccount);
      setWithdrawAmount("");
      setShowWithdraw(false);

      setMessage(
        `Withdrawal successful. New balance: $${Number(
          updatedAccount.balance
        ).toFixed(2)}`
      );

      await getTransactions();
    } catch (error) {
      setError(error.message);
    }
  };

  const handleTransfer = async (event) => {
    event.preventDefault();

    try {
      clearMessages();

      const token = localStorage.getItem("token");

      if (!token) {
        throw new Error("Please log in first.");
      }

      if (!destinationAccountID) {
        throw new Error("Please select a destination account.");
      }

      if (
        Number(destinationAccountID) ===
        Number(currentAccount.accountID)
      ) {
        throw new Error(
          "You cannot transfer money to the same account."
        );
      }

      const response = await fetch(
        "http://localhost:8080/api/accounts/transfer",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            fromAccountID: currentAccount.accountID,
            toAccountID: Number(destinationAccountID),
            amount: Number(transferAmount),
          }),
        }
      );

      if (!response.ok) {
        if (response.status === 401) {
          throw new Error(
            "Your login has expired. Please log in again."
          );
        }

        if (response.status === 403) {
          throw new Error(
            "You are not authorized to transfer from this account."
          );
        }

        throw new Error("Transfer failed.");
      }

      const updatedAccount = await response.json();

      setCurrentAccount(updatedAccount);
      setTransferAmount("");
      setDestinationAccountID("");
      setShowTransfer(false);

      setMessage(
        `Transfer successful. New balance: $${Number(
          updatedAccount.balance
        ).toFixed(2)}`
      );

      await getTransactions();
    } catch (error) {
      setError(error.message);
    }
  };

  const otherAccounts = accounts.filter(
    (otherAccount) =>
      Number(otherAccount.accountID) !==
      Number(currentAccount.accountID)
  );

  const displayedTransactions = showAllTransactions
    ? transactions
    : transactions.slice(0, 3);

  return (
    <main>
      <button onClick={() => setCurrentPage("Accounts")}>
        ← Back to My Accounts
      </button>

      <h2>{currentAccount.accountType} Account</h2>

      <p>
        Account Number: ****
        {currentAccount.accountNumber.slice(-4)}
      </p>

      <section>
        <h3>Available Balance</h3>

        <p>
          ${Number(currentAccount.balance).toFixed(2)}
        </p>
      </section>

      {message && <p>{message}</p>}

      {error && <p>{error}</p>}

      <section>
        <h3>Account Actions</h3>

        <button
          onClick={() => {
            clearMessages();
            setShowDeposit(!showDeposit);
            setShowWithdraw(false);
            setShowTransfer(false);
          }}
        >
          Deposit
        </button>

        {showDeposit && (
          <form onSubmit={handleDeposit}>
            <div>
              <label htmlFor="depositAmount">
                Deposit Amount
              </label>

              <br />

              <input
                id="depositAmount"
                type="number"
                step="0.01"
                min="0.01"
                value={depositAmount}
                onChange={(event) =>
                  setDepositAmount(event.target.value)
                }
                required
              />
            </div>

            <br />

            <button type="submit">
              Submit Deposit
            </button>
          </form>
        )}

        <button
          onClick={() => {
            clearMessages();
            setShowWithdraw(!showWithdraw);
            setShowDeposit(false);
            setShowTransfer(false);
          }}
        >
          Withdraw
        </button>

        {showWithdraw && (
          <form onSubmit={handleWithdraw}>
            <div>
              <label htmlFor="withdrawAmount">
                Withdrawal Amount
              </label>

              <br />

              <input
                id="withdrawAmount"
                type="number"
                step="0.01"
                min="0.01"
                value={withdrawAmount}
                onChange={(event) =>
                  setWithdrawAmount(event.target.value)
                }
                required
              />
            </div>

            <br />

            <button type="submit">
              Submit Withdrawal
            </button>
          </form>
        )}

        <button
          onClick={() => {
            clearMessages();
            setShowTransfer(!showTransfer);
            setShowDeposit(false);
            setShowWithdraw(false);
          }}
        >
          Transfer
        </button>

        {showTransfer && (
          <form onSubmit={handleTransfer}>
            <div>
              <label htmlFor="destinationAccount">
                Transfer To
              </label>

              <br />

              <select
                id="destinationAccount"
                value={destinationAccountID}
                onChange={(event) =>
                  setDestinationAccountID(event.target.value)
                }
                required
              >
                <option value="">
                  Select an account
                </option>

                {otherAccounts.map((otherAccount) => (
                  <option
                    key={otherAccount.accountID}
                    value={otherAccount.accountID}
                  >
                    {otherAccount.accountType} Account ending in{" "}
                    {otherAccount.accountNumber.slice(-4)}
                  </option>
                ))}
              </select>
            </div>

            <br />

            <div>
              <label htmlFor="transferAmount">
                Transfer Amount
              </label>

              <br />

              <input
                id="transferAmount"
                type="number"
                step="0.01"
                min="0.01"
                value={transferAmount}
                onChange={(event) =>
                  setTransferAmount(event.target.value)
                }
                required
              />
            </div>

            <br />

            <button type="submit">
              Submit Transfer
            </button>
          </form>
        )}

        {showTransfer && otherAccounts.length === 0 && (
          <p>
            You do not have another account available for transfers.
          </p>
        )}
      </section>

      <section>
        <h3>
          {showAllTransactions
            ? "Transaction History"
            : "Recent Transactions"}
        </h3>

        {transactionLoading && (
          <p>Loading transactions...</p>
        )}

        {transactionError && (
          <p>{transactionError}</p>
        )}

        {!transactionLoading &&
          !transactionError &&
          transactions.length === 0 && (
            <p>No transactions found for this account.</p>
          )}

        {!transactionLoading &&
          !transactionError &&
          displayedTransactions.length > 0 && (
            <div>
              {displayedTransactions.map((transaction) => (
                <div key={transaction.transactionID}>
                  <p>
                    <strong>
                      {transaction.transactionType}
                    </strong>
                  </p>

                  <p>
                    Amount: $
                    {Number(transaction.amount).toFixed(2)}
                  </p>

                  <p>
                    Date:{" "}
                    {new Date(
                      transaction.transactionDate
                    ).toLocaleString()}
                  </p>

                  <hr />
                </div>
              ))}
            </div>
          )}

        {transactions.length > 3 && (
          <button
            onClick={() =>
              setShowAllTransactions(!showAllTransactions)
            }
          >
            {showAllTransactions
              ? "Show Recent Transactions"
              : "View All Transactions"}
          </button>
        )}
      </section>

      <section>
        <h3>Account Information</h3>

        <p>
          Account Type: {currentAccount.accountType}
        </p>

        <p>
          Account Number: ****
          {currentAccount.accountNumber.slice(-4)}
        </p>

        <p>
          Account ID: {currentAccount.accountID}
        </p>
      </section>
    </main>
  );
}

export default AccountDetails;