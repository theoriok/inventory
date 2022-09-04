import React from 'react';
import PropTypes from 'prop-types';

class Country extends React.Component {
    static get propTypes() {
        return {
            country: PropTypes.object
        };
    }

    render() {
        return (
            <div>{this.props.country.name} ({this.props.country.code})</div>
        )
    }
}

export default Country;