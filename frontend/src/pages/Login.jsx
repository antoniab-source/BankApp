import { useState } from "react";

function Login({ onLogin, setCurrentPage }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleLogin = async (event) => {
    event.preventDefault();

    try {
      setError("");

      const response = await fetch(
        "http://localhost:8080/api/auth/login",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            username,
            password,
          }),
        }
      );

      if (!response.ok) {
        throw new Error("Invalid username or password.");
      }

      const data = await response.json();

      localStorage.setItem("token", data.token);
      localStorage.setItem("user", JSON.stringify(data));

      onLogin();
    } catch (error) {
      setError(error.message);
    }
  };

  return (
    <main>
      <h2>Login</h2>

      <p>Log in to access your BankApp account.</p>

      <form onSubmit={handleLogin}>
        <div>
          <label htmlFor="username">Username</label>

          <br />

          <input
            id="username"
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
          <label htmlFor="password">Password</label>

          <br />

          <input
            id="password"
            type="password"
            value={password}
            onChange={(event) =>
              setPassword(event.target.value)
            }
            required
          />
        </div>

        <br />

        <button type="submit">
          Login
        </button>
      </form>

      {error && <p>{error}</p>}

      <br />

      <section>
        <h3>New to BankApp?</h3>

        <p>Create an account to get started.</p>

        <button
          onClick={() => setCurrentPage("Register")}
        >
          Create an Account
        </button>
      </section>
    </main>
  );
}

export default Login;