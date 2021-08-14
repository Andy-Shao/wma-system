import React from "react";
import { Link } from "react-router-dom";

class PageList extends React.Component {
  render(){
    return (
      <table>
      <thead>
        <tr>
        <td>UUID</td>
        <td>Operation</td>
        </tr>
      </thead>
      <tbody>
      { this.props.pages.map((page) => (
        <tr>
        <td>{page.uuid}</td>
        <td>
          <button onClick={this.props.onDelete} value={page.uuid}>DELETE</button> |
          <Link to={ '/pageDetail?pageId=' + page.uuid}>Detail</Link>
        </td>
        </tr>
      )) }
      </tbody>
      </table>
    );
  }
}

export default PageList;
