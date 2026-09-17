function Navbar({ setCurrentPage }) {
  return (
    <nav>
      <button onClick={() => setCurrentPage("Home")}>Home</button>
      <button onClick={() => setCurrentPage("About")}>About</button>
      <button onClick={() => setCurrentPage("Contact")}>Contact</button>
      <button onClick={() => setCurrentPage("Data")}>Data</button>
    </nav>
  );
}

export default Navbar;