import React from 'react';
import {Button} from 'react-bootstrap';
import {putApi} from "../util";

class Add extends React.Component {

    render() {
        return (
            <div>
                <h1>Add Book</h1>
                <form onSubmit={this.handleSubmit}>
                    <div>
                        <label htmlFor="business_id">Id: </label>
                        <input type="text" name="business_id" id="business_id"/>
                    </div>
                    <div>
                        <label htmlFor="title">Title: </label>
                        <input type="text" name="title" id="title"/>
                    </div>
                    <div>
                        <label htmlFor="author">Author: </label>
                        <input type="text" name="author" id="author"/>
                    </div>
                    <div>
                        <label htmlFor="description">Description: </label>
                        <textarea name="description" id="description"/>
                    </div>
                    <div><Button variant="dark" type="submit">Submit</Button></div>
                </form>
            </div>
        );
    }

    handleSubmit(e) {
        e.preventDefault();
        const {business_id, title, description, author} = e.target.elements;

        putApi('books', {
            business_id: business_id.value,
            title: title.value,
            description: description.value,
            author: author.value
        });


    }
}

export default Add;