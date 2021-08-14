import React from "react";
import axios from "axios";
import { Link } from "react-router-dom";

class MemoryRecords extends React.Component {
  state = {
    memoryRecords: [{
      uuid: '123',
      description: '456'
    }]
  };

  componentWillMount() {
    this.getData();
  }

  getData() {
    axios.get('http://localhost:8080/memoryRecord/records')
      .then(response => {
        console.log(response);
        this.setState(() => ({
          memoryRecords: response.data
        }));
      })
      .catch(error => {
        console.log(error);
      });
  }

  onClickDelete = (event) => {
    const uuid = event.target.value;
    if(window.confirm('Do you want to delete it?')) {
      axios.delete('http://localhost:8080/memoryRecord/remove/' + uuid)
        .then(response => {
          alert(response.data);
          //window.location.reload(false);
          this.getData();
        })
        .catch(error => {
          console.log(error);
        });
    }
    else {
      //alert('No');
    }

  }

  render() {
    return (
    <div>
      <h3>Memory Record Details</h3>
      <table>
      <thead>
      <tr>
        <td>UUID</td>
        <td>Description</td>
        <td>Operation</td>
      </tr>
      </thead>
      <tbody>
      {this.state.memoryRecords.map((record) => (
        <tr>
          <td>{record.uuid}</td>
          <td>{record.description}</td>
          <td>
            <button onClick={this.onClickDelete} value={record.uuid}>DELETE</button> |
            <Link to={`/memoryRecordDetail?recordId=${record.uuid}`}>Details</Link>
          </td>
        </tr>
      ))}
      </tbody>
      </table>
    </div>
    );
  }
}

export default MemoryRecords;
