import React from "react";
import Nav from "./Nav";

const NavPages = ({ children }) => {
  return (
    <div>
      <Nav />
      {children}
    </div>
  );
};

export default NavPages;
