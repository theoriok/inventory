import React from 'react';
import {Button, Col, Row} from 'react-bootstrap';
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
                    <Row>
                        <Col xxl={2} xl={3} lg={4} md={6}><label htmlFor="business_id">Id: </label></Col>
                        <Col xxl={2} xl={3} lg={4} md={6}><input type="text" name="business_id" id="business_id"/></Col>
                    </Row>
                    <Row>
                        <Col xxl={2} xl={3} lg={4} md={6}><label htmlFor="name">Name: </label></Col>
                        <Col xxl={2} xl={3} lg={4} md={6}>
                            <input type="text" name="name" id="name" required={true}/>
                        </Col>
                    </Row>
                    <Row>
                        <Col xxl={2} xl={3} lg={4} md={6}><label htmlFor="description">Description: </label></Col>
                        <Col xxl={2} xl={3} lg={4} md={6}><textarea name="description" id="description"/></Col>
                    </Row>
                    <Row>
                        <Col xxl={2} xl={3} lg={4} md={6}><label htmlFor="country">Country</label></Col>
                        <Col xxl={2} xl={3} lg={4} md={6}>
                            <select name="country" id="country">
                                {
                                    this.state.countries.map(country =>
                                        <option value={country.code} key={country.code}>{country.name}</option>
                                    )
                                }
                            </select>
                        </Col>
                    </Row>
                    <Row>
                        <Col xxl={2} xl={3} lg={4} md={6}><Button variant="dark" type="submit">Submit</Button></Col>
                    </Row>
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
        })
             .then(res => alert(res))
             .catch(err => alert(err));
    }

    componentDidMount() {
        getApi('countries')
            .then(res => this.setState({countries: res}))
            .catch(err => console.log(err));
    }
}

export default Add;