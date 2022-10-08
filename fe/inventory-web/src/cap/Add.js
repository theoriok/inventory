import React from 'react';
import {getApi, putApi} from "../util";

class Add extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            countries: []
        };
    }

    render() {
        return (
            <div>
                <h1>Add Cap</h1>
                <form onSubmit={this.handleSubmit}>
                    <div>
                        <label htmlFor="business_id">Id: </label>
                        <input type="text" name="business_id" id="business_id"/>
                    </div>
                    <div>
                        <label htmlFor="name">Name: </label>
                        <input type="text" name="name" id="name"/>
                    </div>
                    <div>
                        <label htmlFor="description">Description: </label>
                        <textarea name="description" id="description"/>
                    </div>
                    <div>
                        <label htmlFor="country">Country</label>
                        <select name="country" id="country">
                            {
                                this.state.countries.map(country => <option value={country.code} key={country.code}>{country.name}</option>)
                            }
                        </select>
                    </div>
                    <div><input type="submit" value="Submit"/></div>
                </form>
            </div>
        );
    }

    handleSubmit(e) {
        e.preventDefault();
        const {business_id, name, description, country} = e.target.elements;

        putApi('caps', {
            business_id: business_id.value,
            name: name.value,
            description: description.value,
            country: country.value
        });


    }

    componentDidMount() {
        getApi('countries')
            .then(res => this.setState({countries: res}))
            .catch(err => console.log(err));
    }
}

export default Add;