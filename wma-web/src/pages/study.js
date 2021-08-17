import React from "react";
import { Link } from "react-router-dom";
import axios from "axios";


import AppTitle from "../components/appTitle.js"

class PageSearchForm extends React.Component {
  render() {
    return (
    <div>
    <table>
    <tbody>
    <tr>
      <td>RecordId: </td>
      <td>
        <select onChange={this.props.onRecordIdChange}>
        { this.props.records.map( (record, index) => ( 
          <option value={index}>{record.description}</option>
        ))}
        </select>
      </td>
    </tr>
    <tr>
      <td>Page number: </td>
      <td>
        <select onChange={this.props.onPageNumberChange}>
          <option value="1">1</option>
          <option value="2">2</option>
          <option value="3">3</option>
          <option value="4">4</option>
          <option value="5" selected="selected">5</option>
          <option value="6">6</option>
          <option value="7">7</option>
          <option value="8">8</option>
          <option value="9">9</option>
        </select>
      </td>
    </tr>
    <tr>
      <td></td>
      <td><button onClick={this.props.onSetting}>setting</button></td>
    </tr>
    </tbody>
    </table>
    </div>
    );
  }
}

class Study extends React.Component {
  state = {
    currentRecordIndex: 0,
    currentRecordId: '',
    pageNumber: 5,
    records: [{
      uuid: '',
      pageSequence: [ ],
      description: '',
      studyNumber: 0
    }]
  }

  componentWillMount() {
    this.getData();
  }

  getData() {
    axios.get('http://localhost:8080/memoryRecord/records')
      .then(response => { 
        console.log(response);
        const recds = response.data;
        const curRecordId = recds[0].uuid;
        this.setState({ records: recds, currentRecordId: curRecordId });
      })
      .catch(error => { 
        console.log(error);
      });
  }
  
  onSetting = (event) => {
    const recordIndex = this.state.currentRecordIndex;
    const pageNumber = this.state.pageNumber;
    const record = this.state.records[recordIndex];

    record.studyNumber = pageNumber;
    axios.post('http://localhost:8080/memoryRecord/saveOrUpdate',
      record,
      { 
        headers: { 
          'Content-Type': 'application/json', 
          'Access-Control-Allow-Headers': 'Content-Type'
        } 
      })
      .then(response => { 
        console.log(response);
      })
      .catch(error => { 
        console.log(error);
      });
  }

  onRecordIdChange = (event) => {
    const recordIndex = event.target.value;
    const recordId = this.state.records[recordIndex].uuid;
    this.setState({ currentRecordIndex: recordIndex, currentRecordId: recordId });
  }
  
  onPageNumberChange = (event) => {
    const pgNumber = event.target.value;
    this.setState({ pageNumber: pgNumber });
  }
  
  render() {
    return (
      <div>
        <AppTitle />
        <h2>Study Page</h2>
        <PageSearchForm records={this.state.records} onSetting={this.onSetting} onRecordIdChange={this.onRecordIdChange} onPageNumberChange={this.onPageNumberChange} />
        <Link to="/">Main Page</Link> | <Link to={'/launchStudy?recordId=' + this.state.currentRecordId}>Launch Study</Link>
      </div>
    );
  }
}

export default Study;
