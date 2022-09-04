import React from 'react';
import {getApi} from '../util.js';
import Cap from './Cap.js';
import * as _ from 'lodash';

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
                <h1>Hello Caps Lovers</h1>
                {
                    _.sortBy(this.state.caps, "country.name", "name")
                        .map((cap, i) => <Cap key={i} cap={cap}/>)
                }
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