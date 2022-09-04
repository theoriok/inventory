import React from 'react';
import {getApi} from '../util.js';
import Country from './Country.js';
import * as _ from "lodash";

class Countries extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            countries: []
        };
    }

    render() {
        return (
            <div>
                <h1>All My Countries</h1>
                {
                    _.sortBy(this.state.countries, "name")
                        .map((country, i) => <Country key={i} country={country}/>)
                }
            </div>
        );
    }

    componentDidMount() {
        getApi('countries')
            .then(res => this.setState({countries: res}))
            .catch(err => console.log(err));
    }
}

export default Countries;