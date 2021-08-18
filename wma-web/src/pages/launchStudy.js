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
    displayMap: new Map()
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

  render() {
    return (
      <div>
        <AppTitle />
        <h2>Launch Study Page | Page Id: {this.state.page.uuid}</h2>
        <PrintPage page={this.state.page} onClickShow={this.onClickShow} displayMap={this.state.displayMap} onFinishStudy={this.onFinishStudy} onStudyTomorrow={this.onStudyTomorrow} onRestudyToday={this.onRestudyToday} />
        <Link to="/">Main Page</Link> | <Link to="/study">Study</Link>
      </div>
    );
  }
}

export default LaunchStudy;
