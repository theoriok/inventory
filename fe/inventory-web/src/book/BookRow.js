import React from 'react';
import PropTypes from "prop-types";
import {Link} from "react-router-dom";

class BookRow extends React.Component {
    static get propTypes() {
        return {
            book: PropTypes.object
        };
    }

    render() {
        let id;
        return (
            <tr>
                <td>{this.props.book.author}</td>
                <td>{this.props.book.title}</td>
                <td>Edit | <Link to="/book/delete/" state={{id: this.props.book.business_id}}>Delete</Link></td>
            </tr>
        )
    }
}

export default BookRow;