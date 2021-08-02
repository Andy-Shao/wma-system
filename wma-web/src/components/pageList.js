import React from "react";

class PageList extends React.Component {
  render(){
    return (
      <table>
      <thead>
        <tr>
        <td>UUID</td>
        </tr>
      </thead>
      <tbody>
      { this.props.pages.map((page) => (
        <tr>
        <td>{page.uuid}</td>
        </tr>
      )) }
      </tbody>
      </table>
    );
  }
}

export default PageList;
