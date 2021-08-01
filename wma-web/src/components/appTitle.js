import React from "react";
import axios from "axios";

class AppTitle extends React.Component {
  state = {
    application: {
      code: '',
      data: {
        applicationLevel: '',
        applicationName: ''
      },
      message: '',
      success: true
    }
  };

  componentWillMount() {
    this.getData();
  }

  getData() {
    axios.get('http://localhost:8080/application')
      .then(response => {
        console.log(response);
        this.setState(() => ({
          application: response.data
        }));
      })  
      .catch(error => {
        console.log(error);
      }); 
  }

  render() {
    return <h1>Word Memory Assistance System -- {this.state.application.data.applicationLevel}</h1>;
  }
}

export default AppTitle;
