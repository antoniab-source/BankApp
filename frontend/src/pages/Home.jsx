function Home() {
  return (
    <main className="dashboard">
      <section className="welcome-section">
        <h2>Welcome to BankApp</h2>
        <p>
          Manage your accounts, view your balances, and make transactions
          through your BankApp dashboard.
        </p>
      </section>

      <section className="dashboard-cards">
        <div className="dashboard-card">
          <h3>Accounts</h3>
          <p>View your checking and savings accounts.</p>
        </div>

        <div className="dashboard-card">
          <h3>Balances</h3>
          <p>Keep track of your current account balances.</p>
        </div>

        <div className="dashboard-card">
          <h3>Transactions</h3>
          <p>Deposit, withdraw, and transfer funds.</p>
        </div>
      </section>
    </main>
  );
}

export default Home;

