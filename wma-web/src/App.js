import React, { Component } from 'react'
import logo from './logo.svg';
import { BrowserRouter as Router, Route, Switch, Link, Redirect } from "react-router-dom";

import MainPage from "./pages/mainPage.js";
import PageDetail from "./pages/pageDetail.js";
import NotFoundPage from "./pages/notFoundPage.js";
import CreateMemoryRecord from "./pages/createMemoryRecord.js";
import MemoryRecordDetail from "./pages/memoryRecordDetail.js";
import MaterialManagement from "./pages/materialManagement.js";
import CreateMaterial from "./pages/createMaterial.js";
import FalsifyMaterial from "./pages/falsifyMaterial.js";
import ModifyGroup from "./pages/modifyGroup.js";
import Study from "./pages/study.js";
import LaunchStudy from "./pages/launchStudy.js";

class App extends React.Component {

  render() {
    return (
      <Router>
        <Switch>
          <Route exact path="/" component={MainPage}/>
          <Route exact path="/pageDetail" component={PageDetail} />
          <Route exact path="/memoryRecordDetail" component={MemoryRecordDetail} />
          <Route exact path="/createMemoryRecord" component={CreateMemoryRecord} />
          <Route exact path="/createMaterial" component={CreateMaterial} />
          <Route exact path="/materialManagement" component={MaterialManagement} />
          <Route exact path="/falsifyMaterial" component={FalsifyMaterial} />
          <Route exact path="/modifyGroup" component={ModifyGroup} />
          <Route exact path="/study" component={Study} />
          <Route exact path="/launchStudy" component={LaunchStudy} />
          <Route exact path="/404" component={NotFoundPage}/>
          <Redirect to="/404"/>
        </Switch>
      </Router>
    );
  }
}

export default App;
