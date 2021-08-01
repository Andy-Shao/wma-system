import React from "react";
import "../css/form.css";
import { Link, Redirect } from "react-router-dom";
import axios from "axios";


import AppTitle from "../components/appTitle.js"

class CreateMemoryRecord extends React.Component {
  state = {
    description: '',
    isRedirect: false
  };

  onDescriptionChanged = (event) => {
    this.setState(() => ({ 
      description: event.target.value
    }));
  };

  isPassFormCheck() {
    return this.state.description != '';
  }

  onClickSubmit = (event) => {
    if(!this.isPassFormCheck()) {
      alert('Inputing form firstly!');
      return;
    }
    
    this.submitform();
  }

  async submitform() {
    const response = axios.post('http://localhost:8080/memoryRecord/saveOrUpdate', 
      { description: this.state.description },
      {
        headers: { 
          'Content-Type': 'application/json', 
          'Access-Control-Allow-Headers': 'Content-Type'
        }
      });
      //this.setState({ isRedirect: true });
      alert('Creating record success!');
  }

  render() {
    if(this.state.isRedirect) return <Redirect to="/" />
    return (
      <div>
        <AppTitle />
        <form onSubmit={this.onClickSubmit}>
          <label>Create Memory Record</label>
          <table>
          <tbody>
          <tr>
            <td>Description:</td>
            <td><input type="text" onChange={this.onDescriptionChanged}/></td>
          </tr>
          <tr>
            <td></td>
            <td><input type="submit" value="Submit"/></td>
          </tr>
          </tbody>
          </table>
        </form>
        <Link to="/">Main Page</Link>
      </div>
    );
  }
}

export default CreateMemoryRecord;
