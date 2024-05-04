import React from 'react';
import {getApi} from '../util.js';
import BookRow from './BookRow.js';
import * as _ from 'lodash';
import {Link} from "react-router-dom";
import {Table} from "react-bootstrap";

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
                <Table striped bordered hover>
                    <thead>
                    <tr>
                        <th>Author</th>
                        <th>Title</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        _.sortBy(this.state.books, "author", "title")
                            .map((book, i) => <BookRow key={i} book={book}/>)
                    }
                    </tbody>

                </Table>
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