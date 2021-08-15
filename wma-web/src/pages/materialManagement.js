import React from "react";
import axios from "axios";
import { Link } from "react-router-dom";


import AppTitle from "../components/appTitle.js"

class GroupSearchForm extends React.Component {
  render() {
    return (
      <div>
      <form onSubmit={this.props.onSubmit}>
      <table>
        <tbody>
        <tr>
          <td>RecordId: </td>
          <td>
          <select name="records" onChange={this.props.onRecordIdChange}>
            <option value="NULL"></option>
          { this.props.records.map((record) => ( 
            <option value={record.uuid}>{record.description}</option>
          ))}
          </select>
          </td>
        </tr>
        <tr>
          <td>PageId: </td>
          <td>
          <select name="pages" onChange={this.props.onPageIdChange}>
            <option value="NULL"></option>
          { this.props.pageIds.map(pageId => (
            <option value={pageId}>{pageId}</option>
          ))}
          </select>
          </td>
        </tr>
        <tr>
          <td></td>
          <td><input type="submit" value="Search" /></td>
        </tr>
        </tbody>
      </table>
      </form>
      </div>
    );
  }
}

class MaterialList extends React.Component {
}

class MaterialManagement extends React.Component {
  state = {
    records: [{ 
      uuid: '',
      description: '',
      pageSequence: [ ]
    }],
    pageIds: [ ],
    page: { 
      uuid: '',
      groups: [ ]
    },
    currentRecordId: '',
    currentPageId: ''
  }
  
  componentWillMount() {
    this.getData();
  }

  getData() {
    axios.get('http://localhost:8080/memoryRecord/records')
      .then(response => { 
        console.log(response);
        this.setState({ 
          records: response.data,
          currentRecordId: "NULL",
          currentPageId: "NULL"
        });
      })
      .catch(error => { 
        console.log(error);
      });
  }

  onRecordIdChange = (event) => {
    const recordId = event.target.value;
    var pIds = [ ];
    const records = this.state.records;
    records.map(record => { 
      if(record.uuid === recordId) {
        pIds = record.pageSequence;
      }
    });
    this.setState({ 
      currentRecordId: recordId,
      pageIds: pIds
    });
  }

  onPageIdChange = (event) => {
    const pageId = event.target.value;
    this.setState({ 
      currentPageId: pageId
    });
  }

  onSearch = (event) => { 
    alert('record id: ' + this.state.currentRecordId + '; page id: ' + this.state.currentPageId);
  }

  render() {
    return (
      <div>
        <AppTitle />
        <h3>Material Management Page</h3>
        <GroupSearchForm onRecordIdChange={this.onRecordIdChange} onPageIdChange={this.onPageIdChange} onSubmit={this.onSearch} records={this.state.records} pageIds={this.state.pageIds}/>
        <Link to="/">Main Page</Link> | <Link to="/createMaterial">Create Material</Link>
      </div>
    );
  }
}

export default MaterialManagement;
