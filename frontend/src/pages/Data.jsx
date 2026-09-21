import { useEffect, useState } from "react";

function Data({ setCurrentPage, setSelectedAccount }) {
  const [accounts, setAccounts] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  const user = JSON.parse(localStorage.getItem("user"));

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

          if (response.status === 403) {
            throw new Error(
              "You are not authorized to view these accounts."
            );
          }

          throw new Error("Failed to retrieve account data.");
        }

        const data = await response.json();

        setAccounts(data);
      } catch (error) {
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };

    getAccounts();
  }, []);

  const viewAccount = (account) => {
    setSelectedAccount(account);
    setCurrentPage("AccountDetails");
  };

  return (
    <main>
      <h2>Welcome, {user?.username}</h2>

      <p>
        Here are your BankApp accounts and current balances.
      </p>

      {loading && <p>Loading your accounts...</p>}

      {error && <p>{error}</p>}

      {!loading && !error && accounts.length === 0 && (
        <p>You don't have any bank accounts yet.</p>
      )}

      {accounts.length > 0 && (
        <section>
          <h3>Your Accounts</h3>

          <div>
            {accounts.map((account) => (
              <div
                key={account.accountID}
                onClick={() => viewAccount(account)}
                role="button"
                tabIndex="0"
              >
                <h3>{account.accountType} Account</h3>

                <p>
                  Account Number: ****
                  {account.accountNumber.slice(-4)}
                </p>

                <p>
                  Current Balance: $
                  {Number(account.balance).toFixed(2)}
                </p>

                <p>View Details →</p>
              </div>
            ))}
          </div>
        </section>
      )}
    </main>
  );
}

export default Data;