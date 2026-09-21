import { useState } from "react";

function Deposit() {
  const [accountID, setAccountID] = useState("");
  const [amount, setAmount] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const handleDeposit = async (event) => {
    event.preventDefault();

    try {
      setMessage("");
      setError("");

      const response = await fetch(
        `http://localhost:8080/api/accounts/${accountID}/deposit`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            amount: Number(amount),
          }),
        }
      );

      if (!response.ok) {
        throw new Error("Deposit failed.");
      }

      const updatedAccount = await response.json();

      setMessage(
        `Deposit successful. New balance: $${Number(
          updatedAccount.balance
        ).toFixed(2)}`
      );

      setAmount("");
    } catch (error) {
      setError(error.message);
    }
  };

  return (
    <main>
      <h2>Deposit Funds</h2>

      <p>Add money to one of your BankApp accounts.</p>

      <form onSubmit={handleDeposit}>
        <div>
          <label htmlFor="accountID">Account ID</label>
          <br />
          <input
            id="accountID"
            type="number"
            value={accountID}
            onChange={(event) => setAccountID(event.target.value)}
            required
          />
        </div>

        <br />

        <div>
          <label htmlFor="amount">Amount</label>
          <br />
          <input
            id="amount"
            type="number"
            step="0.01"
            min="0.01"
            value={amount}
            onChange={(event) => setAmount(event.target.value)}
            required
          />
        </div>

        <br />

        <button type="submit">Deposit</button>
      </form>

      {message && <p>{message}</p>}

      {error && <p>{error}</p>}
    </main>
  );
}

export default Deposit;