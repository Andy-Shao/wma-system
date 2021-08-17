import React from "react";
import { Link } from "react-router-dom";


import AppTitle from "../components/appTitle.js";
import MemoryRecords from "../components/memoryRecords.js";

class MainPage extends React.Component {
  render() {
    return (
      <div>
        <AppTitle />
        <MemoryRecords />
        <Link to="/createMemoryRecord"> Create Memory Record </Link> | <Link to="/materialManagement">Material Management</Link> | <Link to="/study">Study</Link>
      </div>
    );
  }
}

export default MainPage;
