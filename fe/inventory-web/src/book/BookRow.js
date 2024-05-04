import React from 'react';
import PropTypes from "prop-types";

class BookRow extends React.Component {
    static get propTypes() {
        return {
            book: PropTypes.object
        };
    }

    render() {
        return (
            <tr>
                <td>{this.props.book.author}</td>
                <td>{this.props.book.title}</td>
                <td>Edit | Delete</td>
            </tr>
        )
    }
}

export default BookRow;