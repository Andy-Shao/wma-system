import React from "react";
import { Link } from "react-router-dom";
import axios from "axios";


import AppTitle from "../components/appTitle.js";

class MaterialList extends React.Component {
  render() {
    return (
    <div>
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
      <td><button>Add to Group</button></td>
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
      materials: [{ }]
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
  
  render() {
    return (
      <div>
        <AppTitle />
        <h2>Modify Group Page | Group id: {this.state.groupId}</h2>
        <MaterialList />
        <hr />
        <MaterialSearch materials={this.state.searchMaterials} searchParam={this.state.searchParam} onSearchParamChange={this.onSearchParamChange} onSearchMaterial={this.onSearchMaterial}/>
        <Link to="/">Main Page</Link> | <Link to="/MaterialManagement">Material Management</Link>
      </div>
    );
  }
}

export default ModifyGroup;
