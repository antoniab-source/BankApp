import { useState } from "react";
import "./App.css";

import Header from "./components/Header";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";

import Home from "./pages/Home";
import About from "./pages/About";
import Contact from "./pages/Contact";
import Data from "./pages/Data";
import Login from "./pages/Login";
import AccountDetails from "./pages/AccountDetails";

function App() {
  const [currentPage, setCurrentPage] = useState("Home");

  const [isLoggedIn, setIsLoggedIn] = useState(
    Boolean(localStorage.getItem("token"))
  );

  const [selectedAccount, setSelectedAccount] = useState(null);

  const handleLogin = () => {
    setIsLoggedIn(true);
    setCurrentPage("Accounts");
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    setIsLoggedIn(false);
    setSelectedAccount(null);
    setCurrentPage("Home");
  };

  const renderPage = () => {
    if (currentPage === "Accounts" && isLoggedIn) {
      return (
        <Data
          setCurrentPage={setCurrentPage}
          setSelectedAccount={setSelectedAccount}
        />
      );
    }

    if (currentPage === "AccountDetails" && isLoggedIn) {
      return (
        <AccountDetails
          account={selectedAccount}
          setCurrentPage={setCurrentPage}
        />
      );
    }

    if (currentPage === "Login") {
      return <Login onLogin={handleLogin} />;
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

      <Navbar
        setCurrentPage={setCurrentPage}
        isLoggedIn={isLoggedIn}
        handleLogout={handleLogout}
      />

      {renderPage()}

      <Footer />
    </div>
  );
}

export default App;