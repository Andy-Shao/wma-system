import React from "react";
import axios from "axios";
import { Link } from "react-router-dom";


import AppTitle from "../components/appTitle.js";

class PrintPage extends React.Component {
  render() {
    return (
    <div>
    { 
    this.props.page.groups.map( (group, gIndex) => { 
    return (
    <div>
      <h3>GroupId: {group.uuid}</h3>
      <table class="bolderTable">
      <thead>
        <tr>
          <td>wordList</td>
          <td>meansList</td>
          <td>operation</td>
          <td>quantity of words</td>
          <td>Operation</td>
        </tr>
      </thead>
      { group.materials.map( (material, mIndex) => (
      <tbody>
        <tr>
          <td>
          { material.wordList.map( (word, wIndex) => { 
            const mapKey = group.uuid + '#' + material.uuid;
            if(this.props.displayMap.has(mapKey)) {
              return ( 
                <span>{word}; </span>
              )
            }
            else return (
              <span class="hidden">{word}; </span>
            );
          })}
          </td>
          <td>
          { material.meansList.map( (mean, meanIndex) => (
            <div>
              <span>{mean.interpretation},{mean.type}</span>
            </div>
          ))}
          </td>
          <td><button value={group.uuid + '#' + material.uuid} onClick={this.props.onClickShow}>Show</button></td>
          <td>{material.wordList.length}</td>
          <td><Link to={'/falsifyMaterial?materialId=' + material.uuid}>Modify</Link></td>
        </tr>
      </tbody>
      ))}
      </table>
    </div>
    );
    })}
    <hr/>
    <button onClick={this.props.onFinishStudy}>Finish Study</button> | <button onClick={this.props.onStudyTomorrow}>Study Tomorrow</button> | <button onClick={this.props.onRestudyToday}>Restudy Today</button>
    <p></p>
    </div>
    );
  }
}

class MovePageForm extends React.Component {
  render() {
    return (
    <div>
    <table>
    <tbody>
    <tr>
      <td>Target Recrod:</td>
      <td>
        <select onChange={this.props.onTargetRecordIdChange}>
          <option value="NULL"></option>
          { this.props.records.map( (record) => (
          <option value={record.uuid}>{record.description + '(' + record.studyNumber + ')'}</option>
          ))}
        </select> 
      </td>
    </tr>
    <tr>
      <td>Move Type:</td>
      <td>
        <select onChange={this.props.onMoveTypeChange}>
          <option value="head">Head</option>
          <option value="tail">Tail</option>
        </select>
      </td>
    </tr>
    <tr>
      <td></td>
      <td>
        <button onClick={this.props.onMovePage}>Move Page</button>
      </td>
    </tr>
    </tbody>
    </table>
    </div>
    );
  }
}

class LaunchStudy extends React.Component {
  state = { 
    page: { 
      uuid: '',
      groups: [{ 
        uuid: '',
        materials: [{ 
          uuid: '',
          wordList: [ ],
          meansList: [ ]
        }]
      }]
    },
    recordId: '',
    displayMap: new Map(),
    records: [ ],
    targetRecordId: 'NULL',
    moveType: 'head'
  }

  componentWillMount() {
    this.getData();
  }

  getData() {
    const queryParams = new URLSearchParams(window.location.search);
    const id = queryParams.get('recordId');

    this.setState({ recordId: id, displayMap: new Map() });
    axios.get('http://localhost:8080/memoryRecord/study/' + id)
      .then(response => { 
        console.log(response);
        const data = this.getDataOrEmpty(response);
        this.setState({ page: data });
      })
      .catch(error => { 
        console.log(error);
      });
    
    axios.get('http://localhost:8080/memoryRecord/records')
      .then(response => { 
        console.log(response);
        this.setState({ records: response.data });
      })
      .catch(error => { 
        console.log(error);
      });
  }

  getDataOrEmpty(response) {
    var data = response.data;
    if(data === '') {
      data = {
        uuid: '',
        groups: [ ]
      };
    }
    return data;
  }

  onClickShow = (event) => {
    const key = event.target.value;
    const displayMp = this.state.displayMap;

    displayMp.set(key, 1);
    this.setState({ displayMap: displayMp });
  }

  onFinishStudy = (event) => {
    if(window.confirm('Do you want to finish to study this page?')) {
      axios.put('http://localhost:8080/memoryRecord/finishStudy?recordId=' + this.state.recordId)
        .then(response => { 
          console.log(response);
          const data = this.getDataOrEmpty(response);
          this.setState({ 
            page: data,
            displayMap: new Map()
          });
        })
        .catch(error => { 
          console.log(error);
        });
    }
    else {
      //Do Nothing ...
    }
  }

  onStudyTomorrow = (event) => {
    if(window.confirm('Do you want to study this page tomorrow?')) {
      axios.put('http://localhost:8080/memoryRecord/restudyTomorrow?recordId=' + this.state.recordId)
        .then(response => { 
          console.log(response);
          const data = this.getDataOrEmpty(response);
          this.setState({ 
            page: data,
            displayMap: new Map()
          });
        })
        .catch(error => { 
          console.log(error);
        });
    }
    else {
      //Do Nothing ...
    }
  }

  onRestudyToday = (event) => {
    const displayMp = new Map();

    this.setState({ displayMap: displayMp });
  }

  onTargetRecordIdChange = (event) => {
    const recordId = event.target.value;
    this.setState({ targetRecordId: recordId });
  }

  onMoveTypeChange = (event) => {
    const type = event.target.value;
    this.setState({ moveType: type });
  }

  onMovePage = (event) => { 
    const originalRecordId = this.state.recordId;
    const targetRecordId = this.state.targetRecordId;
    const pageId = this.state.page.uuid;
    const moveType = this.state.moveType;

    if(targetRecordId === 'NULL') {
      alert('target record id:' + targetRecordId);
    }
    else {
      axios.post('http://localhost:8080/memoryRecord/moveStudyPage?originRecordId=' + originalRecordId + '&targetRecordId=' + targetRecordId + '&pageId=' + pageId + '&moveType=' + moveType)
        .then(response => { 
          console.log(response);
          const data = this.getDataOrEmpty(response);
          this.setState({ 
            page: data,
            displayMap: new Map()
          });
        })
        .catch(error => { 
          console.log(error);
        });
    }

  }

  render() {
    return (
      <div>
        <AppTitle />
        <h2>Launch Study Page | Page Id: {this.state.page.uuid}</h2>
        <PrintPage page={this.state.page} onClickShow={this.onClickShow} displayMap={this.state.displayMap} onFinishStudy={this.onFinishStudy} onStudyTomorrow={this.onStudyTomorrow} onRestudyToday={this.onRestudyToday} />
        <hr/>
        <MovePageForm records={this.state.records} onTargetRecordIdChange={this.onTargetRecordIdChange} onMoveTypeChange={this.onMoveTypeChange} onMovePage={this.onMovePage}/>
        <Link to="/">Main Page</Link> | <Link to="/study">Study</Link>
      </div>
    );
  }
}

export default LaunchStudy;
