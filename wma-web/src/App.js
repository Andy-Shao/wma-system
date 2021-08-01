import React, { Component } from 'react'
import logo from './logo.svg';
import './App.css';
import { BrowserRouter as Router, Route, Switch, Link, Redirect } from "react-router-dom";

import MainPage from "./pages/mainPage.js"
import PageDetail from "./pages/pageDetail.js"
import NotFoundPage from "./pages/notFoundPage.js"
import CreateMemoryRecord from "./pages/createMemoryRecord.js"

class App extends React.Component {

  render() {
    return (
      <Router>
        <Switch>
          <Route exact path="/" component={MainPage}/>
          <Route exact path="/pageDetail" component={PageDetail} />
          <Route exact path="/createMemoryRecord" component={CreateMemoryRecord} />
          <Route exact path="/404" component={NotFoundPage}/>
          <Redirect to="/404"/>
        </Switch>
      </Router>
    );
  }
}

export default App;
