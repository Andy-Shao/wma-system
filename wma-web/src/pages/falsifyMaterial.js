import React from "react";
import axios from "axios";
import { Link } from "react-router-dom";


import AppTitle from "../components/appTitle.js"

class FalsifyMaterialForm extends React.Component {
  render() {
    return ( 
      <div>
      <table class="bolderTable">
      <tbody>
        <tr>
          <td>UUID:</td>
          <td>{this.props.material.uuid}</td>
        </tr>
        <tr>
          <td>wordList:</td>
          <td>
          { this.props.material.wordList.map( (word, index) => (
          <div>
            <input type="text" name={index} value={word} onChange={this.props.onWordListChange}/> | <button name={index} value="wordList" onClick={this.props.onDel}>Del</button>
          </div>
          ))}
          </td>
          <td>
            <button value="wordList" onClick={this.props.onAdd}>Add</button>
          </td>
        </tr>
        <tr>
          <td>meansList:</td>
          <td>
          { this.props.material.meansList.map( (mean, index) => (
          <div>
            mean: <input type="text" name={index} value={mean.interpretation} onChange={this.props.onMeanChange}/>
            type: <select name={index} value={mean.type} onChange={this.props.onMeanTypeChange}>
              <option value="adj">adj</option>
              <option value="v">verb</option>
              <option value="n">non</option>
              <option value="adv">adv</option>
              <option value="prep">prep</option>
            </select> | <button name={index} value="meansList" onClick={this.props.onDel}>Del</button>
          </div>
          ))}
          </td>
          <td>
            <button value="meansList" onClick={this.props.onAdd}>Add</button>
          </td>
        </tr>
        <tr>
          <td></td>
          <td><button onClick={this.props.onSubmit}>Modify</button></td>
        </tr>
      </tbody>
      </table>
      </div>
    );
  }
}

class FalsifyMaterial extends React.Component {
  state = { 
    materialId: '',
    material: {
      uuid: '',
      wordList: [ ],
      meansList: [{ 
        interpretation: '',
        type: ''
      }]
    }
  }

  componentWillMount() {
    this.getData();
  }

  getData() {
    const queryParams = new URLSearchParams(window.location.search);
    const id = queryParams.get('materialId');

    this.setState({ materialId: id });

    axios.get('http://localhost:8080/material/getById/' + id)
      .then(response => { 
        console.log(response);
        this.setState({ material: response.data });
      })
      .catch(error => { 
        console.log(error);
      });
  }

  onWordListChange = (event) => {
    const value = event.target.value;
    const index = event.target.name;
    const newMaterial = this.state.material;

    newMaterial.wordList[index] = value;
    this.setState({ material: newMaterial });
  }

  onMeanChange = (event) => {
    const value = event.target.value;
    const index = event.target.name;
    const newMaterial = this.state.material;

    newMaterial.meansList[index].interpretation = value;
    this.setState({ material: newMaterial });
  }

  onMeanTypeChange = (event) => {
    const value = event.target.value;
    const index = event.target.name;
    const newMaterial = this.state.material;

    newMaterial.meansList[index].type = value;
    this.setState({ material: newMaterial });
  }

  onSubmit = (event) => {
    const newMaterial = this.state.material;

    if(window.confirm('Do you want to modify it?')) {
      axios.post('http://localhost:8080/material/addOrUpdate',
        newMaterial,
        { 
          headers: {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Headers': 'Content-Type'
          }
        })
        .then(response => { 
          console.log(response);
          this.getData();
          alert('Modify Success!');
        })
        .catch(error => { 
          console.log(error);
        });
    }
    else { 
      //Do nothing
    }
  }

  onDel = (event) => {
    const index = event.target.name;
    const delType = event.target.value;
    const newMaterial = this.state.material;
    var hasUpdated = false;

    if(delType === 'wordList') {
      newMaterial.wordList.splice(index, 1);
      hasUpdated = true;
    }
    else if(delType === 'meansList') {
      newMaterial.meansList.splice(index, 1);
      hasUpdated = true;
    }

    if(hasUpdated) {
      this.setState({ material: newMaterial });
    }
  }

  onAdd = (event) => {
    const addType = event.target.value;
    const newMaterial = this.state.material;
    var hasUpdated = false;

    if(addType === 'wordList') {
      newMaterial.wordList.push('');
      hasUpdated = true;
    }
    else if(addType === 'meansList') {
      newMaterial.meansList.push({ interpretation: '', type: 'v' });
      hasUpdated = true;
    }

    if(hasUpdated) {
      this.setState({ material: newMaterial });
    }
  }

  render() {
    return (
      <div>
        <AppTitle />
        <h2>Material Id: {this.state.materialId}</h2>
        <FalsifyMaterialForm material={this.state.material} onWordListChange={this.onWordListChange} onMeanChange={this.onMeanChange} onMeanTypeChange={this.onMeanTypeChange} onDel={this.onDel} onAdd={this.onAdd} onSubmit={this.onSubmit} />
        <Link to="/">Main Page</Link> | <Link to="/materialManagement">Material Management</Link>
      </div>
    );
  }
}

export default FalsifyMaterial;
