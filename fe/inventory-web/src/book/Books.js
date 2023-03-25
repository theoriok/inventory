import React from 'react';
import {getApi} from '../util.js';
import Book from './Book.js';
import * as _ from 'lodash';

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
                <h1>Hello Books Lovers</h1>
                {
                    _.sortBy(this.state.books, "country.name", "name")
                        .map((book, i) => <Book key={i} book={book}/>)
                }
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