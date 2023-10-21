import React from 'react';
import PropTypes from "prop-types";
import {deleteApi} from '../util.js';

class Cap extends React.Component {
    static get propTypes() {
        return {
            cap: PropTypes.object
        };
    }

    render() {
        return (
            <div>
                <div>{this.props.cap.name} ({this.props.cap.country.code}): {this.props.cap.amount}</div>
                <div>{this.props.cap.description}</div>
                <p><button onClick={() => this.delete(this.props.cap.business_id)}>Delete</button></p>
            </div>
        )
    }

    delete(businessId) {
        deleteApi('caps/' + businessId)
            .catch(err => alert(err));
    }
}

export default Cap;