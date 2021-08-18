import React from "react";
import axios from "axios";
import { Link } from "react-router-dom";


import AppTitle from "../components/appTitle.js"

class GroupSearchForm extends React.Component {
  render() {
    return (
      <div>
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
          <td><input type="submit" value="Search" onClick={this.props.onSubmit}/></td>
        </tr>
        </tbody>
      </table>
      </div>
    );
  }
}

class MaterialList extends React.Component {
  render() {
    return (
    <div>
    <h3>Page Details</h3> <button onClick={this.props.onAddGroup}>Add Group</button>
    { this.props.page.groups.map( (group, index) => (
    <div>
    <label>Group uuid: {group.uuid}</label> | <Link to={'/modifyGroup?groupId=' + group.uuid}>Amend</Link> | <button value={group.uuid} onClick={this.props.onDeleteGroup}>Delete</button>
    <table class="bolderTable">
    <thead>
    <tr>
      <td>UUID</td>
      <td>wordList</td>
      <td>meansList</td>
      <td>Operation</td>
    </tr>
    </thead>
    <tbody>
    { group.materials.map( (material) => (
    <tr>
      <td>{material.uuid}</td>
      <td>
      { material.wordList.map( (word) => (
        <span>{word};</span>
      ))}
      </td>
      <td>
      { material.meansList.map( (mean) => (
        <div>
          <span>{mean.interpretation},{mean.type};</span>
        </div>
      ))}
      </td>
      <td> 
      <button name={group.uuid} value={material.uuid} onClick={this.props.onEliminateMaterial}>eliminate</button> 
      | <Link to={ '/falsifyMaterial?materialId=' + material.uuid }>Modify</Link>
      </td>
    </tr>
    ))}
    </tbody>
    </table>
    </div>
    ))}
    </div>
    );
  }
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
      currentPageId: 'NULL',
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
    const currentRecordId = this.state.currentRecordId;
    const currentPageId = this.state.currentPageId;

    if(currentPageId === 'NULL') {
      alert('record id: ' + this.state.currentRecordId + '; page id: ' + this.state.currentPageId);
    }
    else {
      this.getPageInfo(currentPageId);
    }
  }
  
  getPageInfo(pageId) {
      axios.get('http://localhost:8080/page/getPage/' + pageId)
        .then(response => { 
          console.log(response);
          this.setState({ page: response.data });
        })
        .catch(error => { 
          console.log(error);
        });
  }

  onAddGroup = (event) => {
    const currentRecordId = this.state.currentRecordId;
    const currentPageId = this.state.currentPageId;

    if(currentRecordId === 'NULL' || currentPageId === 'NULL') {
      alert('recordId and pageId is NULL');
    }
    else {
      axios.put('http://localhost:8080/page/addGroup?pageId=' + currentPageId)
        .then(response => { 
          console.log(response);
          alert('Add success');
          this.getPageInfo(currentPageId);
        })
        .catch(error => { 
          console.log(error);
        });
    }
  }

  onDeleteGroup = (event) => { 
    const pageId = this.state.currentPageId;
    const groupId = event.target.value;

    if(window.confirm('Do you want to delete this group?')) {
      axios.delete('http://localhost:8080/page/removeGroup?pageId=' + pageId + '&groupId=' + groupId)
        .then(response => { 
          console.log(response);
          alert(response.data);
          this.getPageInfo(pageId);
        })
        .catch(error => { 
          console.log(error);
        });
    }
    else {
      //Do Nothing
    }
  }

  onEliminateMaterial = (event) => {
    const groupId = event.target.name;
    const materialId = event.target.value;

    if(window.confirm('Do you want to eliminate this material?')) {
      axios.delete('http://localhost:8080/group/removeMaterial?groupId=' + groupId + '&materialId=' + materialId)
        .then(response => { 
          console.log(response);
          this.getPageInfo(this.state.currentPageId);
        })
        .catch(error => { 
          console.log(error);
        });
    }
    else {
      //Do nothing
    }
  }

  render() {
    return (
      <div>
        <AppTitle />
        <h3>Material Management Page</h3>
        <GroupSearchForm onRecordIdChange={this.onRecordIdChange} onPageIdChange={this.onPageIdChange} onSubmit={this.onSearch} records={this.state.records} pageIds={this.state.pageIds}/>
        <MaterialList page={this.state.page} onAddGroup={this.onAddGroup} onDeleteGroup={this.onDeleteGroup} onEliminateMaterial={this.onEliminateMaterial}/>
        <Link to="/">Main Page</Link> | <Link to="/createMaterial">Create Material</Link>
      </div>
    );
  }
}

export default MaterialManagement;
