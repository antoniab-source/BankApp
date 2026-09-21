import { useState } from "react";
import "./App.css";

import Header from "./components/Header";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";

import Home from "./pages/Home";
import About from "./pages/About";
import Contact from "./pages/Contact";
import Data from "./pages/Data";
import Deposit from "./pages/Deposit";

function App() {
  const [currentPage, setCurrentPage] = useState("Home");

  const renderPage = () => {
    if (currentPage === "Accounts") {
      return <Data />;
    }
    if (currentPage === "Deposit") {
  return <Deposit />;
}
    if (currentPage === "About") {
      return <About />;
    }

    if (currentPage === "Contact") {
      return <Contact />;
    }

    return <Home />;
  };

  return (
    <div className="app">
      <Header />

      <Navbar setCurrentPage={setCurrentPage} />

      {renderPage()}

      <Footer />
    </div>
  );
}

export default App;