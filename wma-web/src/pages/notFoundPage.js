import React from "react";


import AppTitle from "../components/appTitle.js"

class NotFoundPage extends React.Component {
  render() {
    return (
      <div>
        <AppTitle />
        <h2>404 Not Found</h2>
      </div>
    );
  }
}

export default NotFoundPage;
