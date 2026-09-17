import { useState } from "react";
import "./App.css";

import Header from "./components/Header";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";

import Home from "./pages/Home";
import About from "./pages/About";
import Contact from "./pages/Contact";
import Data from "./pages/Data";

function App() {
  const [currentPage, setCurrentPage] = useState("Home");

  const renderPage = () => {
    if (currentPage === "About") {
      return <About />;
    }

    if (currentPage === "Contact") {
      return <Contact />;
    }

    if (currentPage === "Data") {
      return <Data />;
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