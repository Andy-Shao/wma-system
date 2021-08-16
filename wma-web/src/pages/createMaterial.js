import React from "react";
import { Link } from "react-router-dom";
import axios from "axios";


import AppTitle from "../components/appTitle.js";
import "../css/table.css";

class CreateMaterialForm extends React.Component {

  render() {
    return (
    <div>
      <table>
      <tbody>
        <tr>
          <td> Words: </td>
          <td><button onClick={this.props.onClickAddWord}>Add</button></td>
        </tr>
        { 
        this.props.material.wordList.map( (it, index) => (
        <tr>
          <td></td>
          <td>Word: <input type="text" name={index} onChange={this.props.onWordChange} value={it} /></td> 
          <td><button value={index} onClick={this.props.onClickDelWord}>Del</button></td>
        </tr>
        ))}
        <tr>
          <td>Means: </td>
          <td><button onClick={this.props.onClickAddMean}>Add</button></td>
        </tr>
        { this.props.material.meansList.map( (it, index) => (
        <tr>
          <td></td>
          <td>Mean: <input type="text" name={index} value={it.interpretation} onChange={this.props.onInterpretationChange} /></td>
          <td> 
          Type: <select name={index} value={it.type} onChange={this.props.onTypeChange} >
            <option value="adj">adj</option>
            <option value="v">verb</option>
            <option value="n">non</option>
            <option value="adv">adv</option>
            <option value="prep">prep</option>
          </select>
          </td>
          <td><button value={index} onClick={this.props.onClickDelMean}>Del</button></td>
        </tr>
        ))}
        <tr>
          <td></td>
          <td><input type="submit" value="Submit" onClick={this.props.onSubmit}/></td>
        </tr>
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
      <td>Word Match: <input type="text" name="word" onChange={this.props.onMatchParamChange} /></td>
      <td><button onClick={this.props.onMatchMaterial}>Search</button></td>
    </tr>
    </tbody>
    </table>
    <table class="bolderTable">
    <thead>
    <tr>
      <td>UUID</td>
      <td>WordList</td>
      <td>Means</td>
      <td>Operation</td>
    </tr>
    </thead>
    <tbody>
    { this.props.materials.map((material) => (
    <tr>
      <td>{material.uuid}</td>
      <td>{material.wordList.map((word) => (
        <span>{word}; </span>
        ))}
      </td>
      <td>{material.meansList.map((mean) => (
        <div>
          <span>{mean.interpretation},{mean.type}; </span>
        </div>
        ))}
      </td>
      <td><Link to={'/falsifyMaterial?materialId=' + material.uuid}>Amend</Link> | <button>Del</button></td>
    </tr>
    ))}
    </tbody>
    </table>
    </div>
    );
  }
}

class CreateMaterial extends React.Component {
  state = { 
    newMaterial: {
      wordList: [ '' ],
      meansList: [{ 
        interpretation: '',
        type: 'v'
      }]
    },
    materials: [{ 
      uuid: 'N/A',
      wordList: ['N/A'],
      meansList: [{ 
        interpretation: 'N/A',
        type: 'N/A'
      }]
    }],
    matchParam: { 
      word: ''
    }
  }

  onClickAddWord = (event) => {
    const material = this.state.newMaterial;
    material.wordList.push('');
    this.setState({ newMaterial: material });
  }

  onClickAddMean = (event) => {
    const material = this.state.newMaterial;
    material.meansList.push({ interpretation: '', type: 'v' });
    this.setState({ newMaterial: material });
  }

  onClickDelWord = (event) => {
    const material = this.state.newMaterial;
    const index = event.target.value;

    material.wordList.splice(index, 1);
    this.setState({ newMaterial: material });
  }

  onClickDelMean = (event) => {
    const material = this.state.newMaterial;
    const index = event.target.value;

    material.meansList.splice(index, 1);
    this.setState({ newMaterial: material });
  }

  onWordChange = (event) => {
    const value = event.target.value;
    const index = event.target.name;
    const material = this.state.newMaterial;

    material.wordList[index] = value;
    this.setState({ newMaterial: material });
  }

  onInterpretationChange = (event) => {
    const value = event.target.value;
    const index = event.target.name;
    const material = this.state.newMaterial;

    material.meansList[index].interpretation = value;
    this.setState({ newMaterial: material });
  }

  onTypeChange = (event) => {
    const value = event.target.value;
    const index = event.target.name;
    const material = this.state.newMaterial;

    material.meansList[index].type = value;
    this.setState({ newMaterial: material });
  }

  onSubmit = (event) => {
    const material = this.state.newMaterial;
    axios.post('http://localhost:8080/material/addOrUpdate',
      material,
      {
        headers: {
          'Content-Type': 'application/json',
          'Access-Control-Allow-Headers': 'Content-Type'
        }
      })
      .then(response => { 
        console.log(response);
        const material = { 
          wordList: [ '' ],
          meansList: [{
          interpretation: '',
          type: 'v'
          }]
        };
        this.setState({ newMaterial: material });
      })
      .catch(error => { 
        console.log(error);
      });
    alert('Adding Success!');
  }

  onMatchParamChange = (event) => { 
    const value = event.target.value;
    const name = event.target.name;
    const matchPm = this.state.matchParam;
    var hasChanged = false;

    if(name === 'word') {
      matchPm.word = value;  
      hasChanged = true;
    }

    if(hasChanged) {
      this.setState({ matchParam: matchPm });
    }
  }

  onMatchMaterial = (event) => {
    axios.get('http://localhost:8080/material/getByWord/' + this.state.matchParam.word)
      .then(response => { 
        console.log(response);
        this.setState({ materials: response.data });
      })
      .catch(error => { 
        console.log(error);
      });
  }

  render() {
    return (
      <div>
        <AppTitle />
        <h3>Creating Material Page</h3>
        <CreateMaterialForm material={this.state.newMaterial} onClickAddWord={this.onClickAddWord} onClickAddMean={this.onClickAddMean} onClickDelWord={this.onClickDelWord} onClickDelMean={this.onClickDelMean} onWordChange={this.onWordChange} onInterpretationChange={this.onInterpretationChange} onTypeChange={this.onTypeChange} onSubmit={this.onSubmit} />
        <hr/>
        <MaterialSearch materials={this.state.materials} onMatchParamChange={this.onMatchParamChange} onMatchMaterial={this.onMatchMaterial}/>
        <Link to="/">Main Page</Link> | <Link to="/materialManagement">Material Management</Link>
      </div>
    );
  }
}

export default CreateMaterial;
