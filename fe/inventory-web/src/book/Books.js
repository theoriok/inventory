import React from 'react';
import {getApi} from '../util.js';
import Book from './Book.js';
import * as _ from 'lodash';
import {Link} from "react-router-dom";

class Books extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            books: []
        };
    }

    render() {
        return (
            <div>
                <h1>All of my Books</h1>
                {
                    _.sortBy(this.state.books, "author", "title")
                        .map((book, i) => <Book key={i} book={book}/>)
                }
                <p><Link to="/book/add">Add</Link></p>
            </div>
        );
    }

    componentDidMount() {
        getApi('books')
            .then(res => this.setState({books: res}))
            .catch(err => console.log(err));
    }
}

export default Books;