import React from "react";
import { Link } from "react-router-dom";
import axios from "axios";


import AppTitle from "../components/appTitle.js";

class GroupList extends React.Component {
  render() {
    return (
      <div>
      <table>
        <thead>
        <tr>
          <td>UUID</td>
          <td>Operation</td>
        </tr>
        </thead>
        <tbody>
        { this.props.page.groups.map((group) => (
        <tr>
          <td>{group.uuid}</td>
          <td>
            <button value={group.uuid} onClick={this.props.onDeleteGroup}>DELETE</button> |
          </td>
        </tr>
        ))}
        </tbody>
      </table>
      </div>
    );
  }
}

class PageDetail extends React.Component {
  state = { 
    recordId: '',
    pageId: '',
    page: { 
      groups: [ ]
    }
  }

  componentWillMount() { 
    this.getData();
  }

  getData() {
    const queryParams = new URLSearchParams(window.location.search);
    const id = queryParams.get('pageId');
    const recId = queryParams.get('recordId');
    this.setState({ 
      pageId: id,
      recordId: recId
    });

    axios.get('http://localhost:8080/page/getPage/'+id)
      .then(response => { 
        console.log(response);
        this.setState((prveState, pros) => ({ 
          page: response.data
        }));
      })
      .catch(error => { 
        console.log(error);
      });
  }

  onClickSearch = (event) => {
    alert('Yes!');
  }

  onAddGroup = (event) => {
    axios.put('http://localhost:8080/page/addGroup?pageId=' + this.state.page.uuid)
      .then(response => {
        alert('Add Group Success!');
        this.getData();
      })
      .catch(error => {
        console.log(error);
      });
  }

  onDeleteGroup = (event) => {
    if(window.confirm('Do you want to delete the group?')) {
      axios.delete('http://localhost:8080/page/removeGroup?pageId=' + this.state.page.uuid + '&groupId=' + event.target.value)
        .then(response => { 
          alert('DELETE SUCCESS!');
          console.log(response);
          this.getData();
        })
        .catch(error => {
          console.log(error);
        });
    }
    else {
      //Do Nothing!!
    }
  }

  render(){
    return (
      <div>
        <AppTitle />
        <h3>Page Id: <Link to={'/memoryRecordDetail?recordId=' + this.state.recordId}>{this.state.page.uuid}</Link></h3>
        <button onClick={this.onAddGroup}>Add Group</button>
        <GroupList page={this.state.page} onDeleteGroup={this.onDeleteGroup}/>
        <Link to="/">Main Page</Link>
      </div>
    );
  }
}

export default PageDetail;
