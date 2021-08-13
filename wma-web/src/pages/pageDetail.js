import React from "react";
import { Link } from "react-router-dom";
import axios from "axios"


import PageList from "../components/pageList.js"
import AppTitle from "../components/appTitle.js"

class PageDetail extends React.Component {
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
    axios.get('http://localhost:8080/page/getPages')
      .then(response => { 
        console.log(response);
        this.setState((prveState,props) => ({ 
          pages: response.data
        }));
      })
      .catch(error => { 
        console.log(error);
      });

    const queryParams = new URLSearchParams(window.location.search)
    const id = queryParams.get('recordId');
    this.setState({ recordId: id });

    axios.get('http://localhost:8080/memoryRecord/record/'+id)
      .then(response => { 
        console.log(response);
        this.setState((prveState, pros) => ({ 
          record: response.data
        }));
      })
      .catch(error => { 
        console.log(error);
      });
  }

  onClickSearch = (event) => {
    alert('Yes!');
  }

  onAddPage = (event) => {
    axios.put('http://localhost:8080/page/addPage')
      .then(response => {
        alert('Add Page Success!');
        this.getData();
      })
      .catch(error => {
        console.log(error);
      });
  }

  render(){
    return (
      <div>
        <AppTitle />
        <h3>Memory Record Id: {this.state.record.uuid} | Description: {this.state.record.description}</h3>
        <button onClick={this.onAddPage}>Add Page</button>
        <PageList pages={this.state.pages}/>
        <Link to="/">Main Page</Link>
      </div>
    );
  }
}

export default PageDetail;
