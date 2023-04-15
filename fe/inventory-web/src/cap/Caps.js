import React from 'react';
import {getApi} from '../util.js';
import Cap from './Cap.js';
import * as _ from 'lodash';
import {Link} from "react-router-dom";

class Caps extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            caps: []
        };
    }

    render() {
        return (
            <div>
                <h1>All of my Caps</h1>
                {
                    _.sortBy(this.state.caps, "country.name", "name")
                        .map((cap, i) => <Cap key={i} cap={cap}/>)
                }
                <p><Link to="/cap/add">Add</Link></p>
            </div>
        );
    }

    componentDidMount() {
        getApi('caps')
            .then(res => this.setState({caps: res}))
            .catch(err => console.log(err));
    }
}

export default Caps;