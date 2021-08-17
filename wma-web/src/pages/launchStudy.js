import React from "react";
import axios from "axios";
import { Link } from "react-router-dom";


import AppTitle from "../components/appTitle.js";

class PrintPage extends React.Component {
  render() {
    return (
    <div>
    { this.props.page.groups.map( (group, gIndex) => (
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
    ))}
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

    this.setState({ recordId: id });
    axios.get('http://localhost:8080/memoryRecord/study/' + id)
      .then(response => { 
        console.log(response);
        this.setState({ page: response.data });
      })
      .catch(error => { 
        console.log(error);
      });
  }

  onClickShow = (event) => {
    const key = event.target.value;
    const displayMp = this.state.displayMap;

    displayMp.set(key, 1);
    this.setState({ displayMap: displayMp });
  }

  render() {
    return (
      <div>
        <AppTitle />
        <h2>Launch Study Page | Page Id: {this.state.page.uuid}</h2>
        <PrintPage page={this.state.page} onClickShow={this.onClickShow} displayMap={this.state.displayMap}/>
        <Link to="/">Main Page</Link>
      </div>
    );
  }
}

export default LaunchStudy;
