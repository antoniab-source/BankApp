function Navbar({ setCurrentPage }) {
  return (
    <nav>
      <button onClick={() => setCurrentPage("Home")}>Home</button>
      <button onClick={() => setCurrentPage("Accounts")}>Accounts</button>
      <button onClick={() => setCurrentPage("Deposit")}>Deposit</button>
      <button onClick={() => setCurrentPage("About")}>About</button>
      <button onClick={() => setCurrentPage("Contact")}>Contact</button>
    </nav>
  );
}

export default Navbar;