import { useEffect, useState } from "react";

function OpenAccount({ setCurrentPage }) {
  const [accounts, setAccounts] = useState([]);
  const [accountType, setAccountType] = useState("");
  const [initialDeposit, setInitialDeposit] = useState("");

  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    const getAccounts = async () => {
      try {
        setError("");

        const token = localStorage.getItem("token");

        if (!token) {
          throw new Error("Please log in first.");
        }

        const response = await fetch(
          "http://localhost:8080/api/accounts",
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

          throw new Error(
            "Failed to retrieve your accounts."
          );
        }

        const data = await response.json();

        setAccounts(data);

        const hasChecking = data.some(
          (account) =>
            account.accountType === "Checking"
        );

        const hasSavings = data.some(
          (account) =>
            account.accountType === "Savings"
        );

        if (!hasChecking) {
          setAccountType("Checking");
        } else if (!hasSavings) {
          setAccountType("Savings");
        } else {
          setAccountType("");
        }
      } catch (error) {
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };

    getAccounts();
  }, []);

  const hasChecking = accounts.some(
    (account) =>
      account.accountType === "Checking"
  );

  const hasSavings = accounts.some(
    (account) =>
      account.accountType === "Savings"
  );

  const handleOpenAccount = async (event) => {
    event.preventDefault();

    setMessage("");
    setError("");

    if (!accountType) {
      setError(
        "There are no additional account types available."
      );
      return;
    }

    if (
      initialDeposit === "" ||
      Number(initialDeposit) < 0
    ) {
      setError(
        "Please enter a valid initial deposit."
      );
      return;
    }

    try {
      const token = localStorage.getItem("token");

      const user = JSON.parse(
        localStorage.getItem("user")
      );

      if (!token || !user?.userID) {
        throw new Error("Please log in first.");
      }

      const response = await fetch(
        "http://localhost:8080/api/accounts",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            balance: Number(initialDeposit),
            accountType,
            user: {
              userID: user.userID,
            },
          }),
        }
      );

      if (!response.ok) {
        const errorText = await response.text();

        if (response.status === 401) {
          throw new Error(
            "Your login has expired. Please log in again."
          );
        }

        if (response.status === 403) {
          throw new Error(
            "You are not authorized to open this account."
          );
        }

        if (response.status === 409) {
          throw new Error(
            `You already have a ${accountType} account.`
          );
        }

        throw new Error(
          errorText || "Failed to open account."
        );
      }

      const newAccount = await response.json();

      setAccounts((currentAccounts) => [
        ...currentAccounts,
        newAccount,
      ]);

      setInitialDeposit("");

      setMessage(
        `${accountType} account created successfully.`
      );

      setAccountType("");
    } catch (error) {
      setError(error.message);
    }
  };

  if (loading) {
    return (
      <main>
        <h2>Open Another Account</h2>

        <p>Loading your accounts...</p>
      </main>
    );
  }

  /*
   * Show the success message before checking
   * whether the customer now has both accounts.
   */
  if (message) {
    return (
      <main>
        <h2>Open Another Account</h2>

        <p>{message}</p>

        <button
          onClick={() => setCurrentPage("Accounts")}
        >
          Go to My Accounts
        </button>
      </main>
    );
  }

  if (error && accounts.length === 0) {
    return (
      <main>
        <h2>Open Another Account</h2>

        <p>{error}</p>

        <button
          onClick={() => setCurrentPage("Accounts")}
        >
          Back to My Accounts
        </button>
      </main>
    );
  }

  if (hasChecking && hasSavings) {
    return (
      <main>
        <h2>Open Another Account</h2>

        <p>
          You already have both a Checking and
          Savings account.
        </p>

        <button
          onClick={() => setCurrentPage("Accounts")}
        >
          Back to My Accounts
        </button>
      </main>
    );
  }

  return (
    <main>
      <h2>Open Another Account</h2>

      <p>
        Open an additional BankApp account.
      </p>

      <section>
        <h3>Your Current Accounts</h3>

        {hasChecking && <p>Checking Account</p>}

        {hasSavings && <p>Savings Account</p>}
      </section>

      <form onSubmit={handleOpenAccount}>
        <section>
          <h3>Choose Account Type</h3>

          {!hasChecking && (
            <label>
              <input
                type="radio"
                name="accountType"
                value="Checking"
                checked={accountType === "Checking"}
                onChange={(event) =>
                  setAccountType(
                    event.target.value
                  )
                }
              />

              {" "}Checking Account
            </label>
          )}

          {!hasSavings && (
            <label>
              <br />

              <input
                type="radio"
                name="accountType"
                value="Savings"
                checked={accountType === "Savings"}
                onChange={(event) =>
                  setAccountType(
                    event.target.value
                  )
                }
              />

              {" "}Savings Account
            </label>
          )}
        </section>

        <br />

        <section>
          <label htmlFor="initialDeposit">
            Initial Deposit
          </label>

          <br />

          <input
            id="initialDeposit"
            type="number"
            step="0.01"
            min="0"
            value={initialDeposit}
            onChange={(event) =>
              setInitialDeposit(
                event.target.value
              )
            }
            required
          />
        </section>

        <br />

        <button type="submit">
          Open Account
        </button>
      </form>

      {error && <p>{error}</p>}

      <br />

      <button
        onClick={() => setCurrentPage("Accounts")}
      >
        Back to My Accounts
      </button>
    </main>
  );
}

export default OpenAccount;