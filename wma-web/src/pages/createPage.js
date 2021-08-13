import React from "react";
import { Link } from "react-router-dom";


import AppTitle from "../components/appTitle.js"

class CreatePage extends React.Component {
  render() {
    return (
      <div>
        <AppTitle />
        <form>
          <label>Create Page Form</label>
        </form>
        <Link to="/pageDetail">Page Detail</Link>
      </div>
    );
  }
}

export default CreatePage;
