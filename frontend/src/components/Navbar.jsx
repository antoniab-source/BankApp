function Navbar({
  setCurrentPage,
  isLoggedIn,
  handleLogout,
}) {
  return (
    <nav>
      <button onClick={() => setCurrentPage("Home")}>
        Home
      </button>

      {isLoggedIn ? (
        <>
          <button onClick={() => setCurrentPage("Accounts")}>
            My Accounts
          </button>
          <button onClick={() => setCurrentPage("Profile")}>
  Profile
</button>

          <button onClick={() => setCurrentPage("About")}>
            About
          </button>

          <button onClick={() => setCurrentPage("Contact")}>
            Contact
          </button>

          <button onClick={handleLogout}>
            Logout
          </button>
        </>
      ) : (
        <>
          <button onClick={() => setCurrentPage("Login")}>
            Login
          </button>

          <button onClick={() => setCurrentPage("About")}>
            About
          </button>

          <button onClick={() => setCurrentPage("Contact")}>
            Contact
          </button>
        </>
      )}
    </nav>
  );
}

export default Navbar;