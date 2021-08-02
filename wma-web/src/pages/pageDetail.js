import React from "react";
import { Link } from "react-router-dom";
import axios from "axios"


import PageList from "../components/pageList.js"
import AppTitle from "../components/appTitle.js"

class PageSearch extends React.Component {
  render() {
    return (
      <form onSubmit={this.props.onClickSearch}>
      <label>
      MemoryRecord:
      <input type="text"/>
      </label>
      <input type="submit" value="Search"/>
      </form>
    );
  }
}

class PageDetail extends React.Component {
  state = { 
    pages: [{ 
      uuid: '12345'
    }]
  }

  onClickSearch = (event) => {
    alert('Yes!');
    this.setState((prveState,props) => ({
      pages: [{ 
        uuid: '7890'
      }]
    }));
  }

  render(){
    return (
      <div>
        <AppTitle />
        <h3>Welcome to the page detail page!</h3>
        <PageSearch onClickSearch={this.onClickSearch} />
        <PageList pages={this.state.pages}/>
        <Link to="/">Main Page</Link>
      </div>
    );
  }
}

export default PageDetail;
