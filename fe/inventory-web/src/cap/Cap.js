import React from 'react';
import PropTypes from "prop-types";

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
                <p>{this.props.cap.description}</p>
            </div>
        )
    }
}

export default Cap;