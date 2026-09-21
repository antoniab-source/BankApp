function Profile() {
  const user = JSON.parse(localStorage.getItem("user"));

  return (
    <main>
      <h2>My Profile</h2>

      <p>View your BankApp account information.</p>

      <section>
        <h3>Profile Information</h3>

        <p>
          <strong>Username:</strong>{" "}
          {user?.username || "Not available"}
        </p>

        <p>
          <strong>User ID:</strong>{" "}
          {user?.userID || "Not available"}
        </p>

        <p>
          <strong>Role:</strong>{" "}
          {user?.role || "Not available"}
        </p>
      </section>
    </main>
  );
}

export default Profile;