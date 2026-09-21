import { useState } from "react";

function Register({ setCurrentPage }) {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const [checking, setChecking] = useState(false);
  const [savings, setSavings] = useState(false);

  const [checkingInitialDeposit, setCheckingInitialDeposit] =
    useState("");

  const [savingsInitialDeposit, setSavingsInitialDeposit] =
    useState("");

  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const handleRegister = async (event) => {
    event.preventDefault();

    setMessage("");
    setError("");

    if (password !== confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    if (!checking && !savings) {
      setError(
        "Please select at least one account type."
      );
      return;
    }

    if (
      checking &&
      (checkingInitialDeposit === "" ||
        Number(checkingInitialDeposit) < 0)
    ) {
      setError(
        "Please enter a valid Checking initial deposit."
      );
      return;
    }

    if (
      savings &&
      (savingsInitialDeposit === "" ||
        Number(savingsInitialDeposit) < 0)
    ) {
      setError(
        "Please enter a valid Savings initial deposit."
      );
      return;
    }

    try {
      const response = await fetch(
        "http://localhost:8080/api/auth/register",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            username,
            email,
            password,
            checking,
            savings,
            checkingInitialDeposit: checking
              ? Number(checkingInitialDeposit)
              : 0,
            savingsInitialDeposit: savings
              ? Number(savingsInitialDeposit)
              : 0,
          }),
        }
      );

      if (!response.ok) {
        const errorText = await response.text();

        if (response.status === 409) {
          throw new Error(
            errorText ||
              "Username or email is already in use."
          );
        }

        if (response.status === 400) {
          throw new Error(
            errorText ||
              "Please check your information."
          );
        }

        throw new Error("Registration failed.");
      }

      await response.json();

      setMessage(
        "Account created successfully. You can now log in."
      );

      setUsername("");
      setEmail("");
      setPassword("");
      setConfirmPassword("");
      setChecking(false);
      setSavings(false);
      setCheckingInitialDeposit("");
      setSavingsInitialDeposit("");
    } catch (error) {
      setError(error.message);
    }
  };

  return (
    <main>
      <h2>Create an Account</h2>

      <p>
        Create your BankApp profile and choose your bank
        account(s).
      </p>

      <form onSubmit={handleRegister}>
        <section>
          <h3>Personal Information</h3>

          <div>
            <label htmlFor="registerUsername">
              Username
            </label>

            <br />

            <input
              id="registerUsername"
              type="text"
              value={username}
              onChange={(event) =>
                setUsername(event.target.value)
              }
              required
            />
          </div>

          <br />

          <div>
            <label htmlFor="registerEmail">
              Email
            </label>

            <br />

            <input
              id="registerEmail"
              type="email"
              value={email}
              onChange={(event) =>
                setEmail(event.target.value)
              }
              required
            />
          </div>

          <br />

          <div>
            <label htmlFor="registerPassword">
              Password
            </label>

            <br />

            <input
              id="registerPassword"
              type="password"
              value={password}
              onChange={(event) =>
                setPassword(event.target.value)
              }
              required
            />
          </div>

          <br />

          <div>
            <label htmlFor="confirmPassword">
              Confirm Password
            </label>

            <br />

            <input
              id="confirmPassword"
              type="password"
              value={confirmPassword}
              onChange={(event) =>
                setConfirmPassword(event.target.value)
              }
              required
            />
          </div>
        </section>

        <br />

        <section>
          <h3>Choose Your Bank Account(s)</h3>

          <div>
            <label>
              <input
                type="checkbox"
                checked={checking}
                onChange={(event) =>
                  setChecking(event.target.checked)
                }
              />

              {" "}Checking Account
            </label>
          </div>

          {checking && (
            <div>
              <br />

              <label htmlFor="checkingDeposit">
                Checking Initial Deposit
              </label>

              <br />

              <input
                id="checkingDeposit"
                type="number"
                step="0.01"
                min="0"
                value={checkingInitialDeposit}
                onChange={(event) =>
                  setCheckingInitialDeposit(
                    event.target.value
                  )
                }
                required
              />
            </div>
          )}

          <br />

          <div>
            <label>
              <input
                type="checkbox"
                checked={savings}
                onChange={(event) =>
                  setSavings(event.target.checked)
                }
              />

              {" "}Savings Account
            </label>
          </div>

          {savings && (
            <div>
              <br />

              <label htmlFor="savingsDeposit">
                Savings Initial Deposit
              </label>

              <br />

              <input
                id="savingsDeposit"
                type="number"
                step="0.01"
                min="0"
                value={savingsInitialDeposit}
                onChange={(event) =>
                  setSavingsInitialDeposit(
                    event.target.value
                  )
                }
                required
              />
            </div>
          )}
        </section>

        <br />

        <button type="submit">
          Create Account
        </button>
      </form>

      {message && <p>{message}</p>}

      {error && <p>{error}</p>}

      <br />

      <button onClick={() => setCurrentPage("Login")}>
        Back to Login
      </button>
    </main>
  );
}

export default Register;