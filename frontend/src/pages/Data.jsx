import { useState } from "react";

function Data() {
  const [accounts, setAccounts] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const showAllCustomers = async () => {
    try {
      setLoading(true);
      setError("");

      const response = await fetch("http://localhost:8080/api/accounts");

      if (!response.ok) {
        throw new Error("Failed to retrieve customer data.");
      }

      const data = await response.json();

      setAccounts(data);
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <main>
      <h2>Customer Data</h2>

      <p>
        View account information retrieved from the BankApp REST API.
      </p>

      <button onClick={showAllCustomers}>
        ShowAllCustomers
      </button>

      {loading && <p>Loading account data...</p>}

      {error && <p>{error}</p>}

      {accounts.length > 0 && (
        <div>
          <h3>Accounts</h3>

          <table>
            <thead>
              <tr>
                <th>Account ID</th>
                <th>Account Number</th>
                <th>Balance</th>
                <th>Account Type</th>
              </tr>
            </thead>

            <tbody>
              {accounts.map((account) => (
                <tr key={account.accountID}>
                  <td>{account.accountID}</td>
                  <td>{account.accountNumber}</td>
                  <td>${Number(account.balance).toFixed(2)}</td>
                  <td>{account.accountType}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {accounts.length === 0 && !loading && !error && (
        <p>Click "ShowAllCustomers" to retrieve account data.</p>
      )}
    </main>
  );
}

export default Data;

