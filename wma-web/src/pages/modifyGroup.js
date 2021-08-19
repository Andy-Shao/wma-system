import React from "react";
import { Link } from "react-router-dom";
import axios from "axios";


import AppTitle from "../components/appTitle.js";

class MaterialList extends React.Component {
  render() {
    return (
    <div>
    <h3>Group id: {this.props.group.uuid}</h3>
    <table>
    <thead>
    <tr>
      <td>UUID</td>
      <td>WordList</td>
      <td>MeansList</td>
      <td>Operation</td>
    </tr>
    </thead>
    <tbody>
    { this.props.group.materials.map( (material, index) => (
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
      <td><button value={material.uuid} onClick={this.props.onOmitMaterial}>Del</button></td>
    </tr>
    ))}
    </tbody>
    </table>
    </div>
    );
  }
}

class MaterialSearch extends React.Component {
  render() {
    return (
    <div>
    <table>
    <tbody>
    <tr>
      <td>Word: <input type="text" name="word" onChange={this.props.onSearchParamChange} /></td>
      <td><button onClick={this.props.onSearchMaterial}>Search</button></td>
      <td><Link to="/createMaterial">Add Word</Link></td>
    </tr>
    </tbody>
    </table>

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
    { this.props.materials.map( (material, index) => (
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
        <button value={material.uuid} onClick={this.props.onAddMaterial}>Add to Group</button> | <Link to={'/falsifyMaterial?materialId=' + material.uuid}>Modify</Link> | <button onClick={this.props.onDeleteMaterial} value={material.uuid}>Del</button>
      </td>
    </tr>
    ))}
    </tbody>
    </table>
    </div>
    );
  }
}

class ModifyGroup extends React.Component {
  state = {
    groupId: '',
    group: {
      uuid: '',
      materials: [{ 
        uuid: 'N/A',
        wordList: [ ],
        meansList: [ ]
      }]
    },
    searchMaterials: [{ 
      uuid: 'N/A',
      wordList: [ ],
      meansList: [ ]
    }],
    searchParam: {
      word: ''
    }
  }

  componentWillMount() {
    this.getData();
  }

  getData() {
    const queryParams = new URLSearchParams(window.location.search);
    const id = queryParams.get('groupId');

    this.setState({ groupId: id });

    axios.get('http://localhost:8080/group/getById/' + id)
      .then(response => { 
        console.log(response);
        this.setState({ group: response.data });
      })
      .catch(error => { 
        console.log(error);
      });
  }

  onSearchParamChange = (event) => {
    const paramType = event.target.name;
    const value = event.target.value;
    const searchPm = this.state.searchParam;
    var hasAmended = false;

    if(paramType === 'word') {
      searchPm.word = value;
      hasAmended = true;
    }

    if(hasAmended) {
      this.setState({ searchParam: searchPm });
    }
  }

  onSearchMaterial = (event) => {
    this.searchMaterial();
  }

  searchMaterial() {
    const searchParam = this.state.searchParam;
    
    axios.get('http://localhost:8080/material/getByWord/' + searchParam.word)
      .then(response => { 
        console.log(response);
        this.setState({ searchMaterials: response.data });
      })
      .catch(error => { 
        console.log(error);
      });
  }

  onAddMaterial = (event) => {
    const groupId = this.state.groupId;
    const materialId = event.target.value;

    axios.put('http://localhost:8080/group/addMaterial?groupId=' + groupId + '&materialId=' + materialId)
      .then(response => { 
        console.log(response);
        this.getData();
      })
      .catch(error => { 
        console.log(error);
      });
  }

  onOmitMaterial = (event) => {
    const groupId = this.state.groupId;
    const materialId = event.target.value;

    axios.delete('http://localhost:8080/group/removeMaterial?groupId=' + groupId + '&materialId=' + materialId)
      .then(response => { 
        console.log(response);
        this.getData();
      })
      .catch(error => { 
        console.log(error);
      });
  }

  onDeleteMaterial = (event) => {
    const materialId = event.target.value;

    if(window.confirm('Do you want to delete this material?')) {
      axios.delete('http://localhost:8080/material/delete/' + materialId)
        .then(response => { 
          console.log(response);
          alert(response.data);
          this.searchMaterial();
        })
        .catch(error => { 
          console.log(error);
        });
    }
    else { 
      // Do Nothing...
    }
  }
  
  render() {
    return (
      <div>
        <AppTitle />
        <h2>Modify Group Page </h2>
        <MaterialList group={this.state.group} onOmitMaterial={this.onOmitMaterial}/>
        <hr />
        <MaterialSearch materials={this.state.searchMaterials} onAddMaterial={this.onAddMaterial} searchParam={this.state.searchParam} onSearchParamChange={this.onSearchParamChange} onSearchMaterial={this.onSearchMaterial} onDeleteMaterial={this.onDeleteMaterial} />
        <Link to="/">Main Page</Link> | <Link to="/MaterialManagement">Material Management</Link>
      </div>
    );
  }
}

export default ModifyGroup;
