import React from "react";
import { Link } from "react-router-dom";
import axios from "axios"


import PageList from "../components/pageList.js"
import AppTitle from "../components/appTitle.js"

class MemoryRecordDetail extends React.Component {
  state = { 
    pages: [{ 
      //uuid: '12345'
    }],
    recordId: '',
    record: { }
  }

  componentWillMount() { 
    this.getData();
  }

  getData() {
    const queryParams = new URLSearchParams(window.location.search)
    const id = queryParams.get('recordId');
    this.setState({ recordId: id });

    axios.get('http://localhost:8080/memoryRecord/record/'+id)
      .then(response => { 
        console.log(response);
        this.setState((prveState, pros) => ({ 
          record: response.data
        }));

        const record = response.data;
        axios.post('http://localhost:8080/page/getPageByIds', 
          { pageIds: record.pageSequence },
          { 
            headers: { 
              'Content-Type': 'application/json',
              'Access-Control-Allow-Headers': 'Content-Type'
            }
          })
          .then(response => { 
            console.log(response);
            this.setState((prveState, props) => ({ 
              pages: response.data
            }));
          })
          .catch(error => { 
            console.log(error);
          });
      })
      .catch(error => { 
        console.log(error);
      });
  }

  onClickSearch = (event) => {
    alert('Yes!');
  }

  onAddPage = (event) => {
    axios.put('http://localhost:8080/memoryRecord/addPage?recordId=' + this.state.record.uuid)
      .then(response => {
        alert('Add Page Success!');
        this.getData();
      })
      .catch(error => {
        console.log(error);
      });
  }

  onDeletePage = (event) => {
    if(window.confirm('Do you want to delete the page?')) {
      axios.delete('http://localhost:8080/memoryRecord/removePage?recordId=' + this.state.record.uuid + '&pageId=' + event.target.value)
        .then(response => { 
          console.log(response);
          this.getData();
        })
        .catch(error => { 
          console.log(error);
        });
    }
    else {
      //Do nothing!
    }
  }

  render(){
    return (
      <div>
        <AppTitle />
        <h3>Memory Record Id: {this.state.record.uuid} | Description: {this.state.record.description}</h3>
        <button onClick={this.onAddPage}>Add Page</button>
        <PageList pages={this.state.pages} onDelete={this.onDeletePage}/>
        <Link to="/">Main Page</Link>
      </div>
    );
  }
}

export default MemoryRecordDetail;
