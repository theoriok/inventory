import React from 'react';
import {putApi} from "../util";

class Add extends React.Component {

    render() {
        return (
            <div>
                <h1>Add Country</h1>
                <form onSubmit={this.handleSubmit}>
                    <div>
                        <label htmlFor="name">Name: </label>
                        <input type="text" name="name" id="name"/>
                    </div>
                    <div>
                        <label htmlFor="code">Code: </label>
                        <textarea name="code" id="code"/>
                    </div>
                    <div><input type="submit" value="Submit"/></div>
                </form>
            </div>
        );
    }

    handleSubmit(e) {
        e.preventDefault();
        const {name, code} = e.target.elements;

        putApi('countries', {
            name: name.value,
            code: code.value
        });
    }
}

export default Add;