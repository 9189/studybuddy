import React, { useState } from "react";
import { Link } from "react-router-dom";

const Nav = () => {
  const [homeActive, setHomeActive] = useState(true);
  const [searchActive, setSearchActive] = useState(false);
  const [addActive, setAddActive] = useState(false);
  const [chatActive, setChatActive] = useState(false);
  const [personActive, setPersonActive] = useState(false);

  return (
    <nav className="navbar fixed-bottom">
      <div className="btn-group mx-auto">
        <Link className="nav-item nav-link" to="/">
          <span className="icons">home</span>
        </Link>

        <Link className="nav-item nav-link" to="/suche">
          <span className="icons">search</span>
        </Link>

        <Link className="nav-item nav-link" to="/add">
          <span className="icons">add_circle</span>
        </Link>

        <Link className="nav-item nav-link" to="/chat">
          <span className="icons">chat_bubble</span>
        </Link>

        <Link className="nav-item nav-link" to="/profil">
          <span className="icons">person</span>
        </Link>
      </div>
    </nav>
  );
};

export default Nav;
