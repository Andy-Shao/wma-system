import React from "react";
import { Link } from "react-router-dom";


import AppTitle from "../components/appTitle.js"

const PageDetail = () => {
  return (
    <div>
      <AppTitle />
      <h3>Welcome to the page detail page!</h3>
      <Link to="/">Main Page</Link>
    </div>
  );
}

export default PageDetail;
