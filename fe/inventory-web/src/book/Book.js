import React from 'react';
import PropTypes from "prop-types";

class Book extends React.Component {
    static get propTypes() {
        return {
            book: PropTypes.object
        };
    }

    render() {
        return (
            <div>
                <h3>{this.props.book.author}</h3>
                <div>{this.props.book.title}</div>
                <p>{this.props.book.description}</p>
            </div>
        )
    }
}

export default Book;