import React from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Books from './book/Books.js';
import Caps from './cap/Caps.js';
import Countries from './country/Countries.js';
import AddBook from './book/Add.js';
import AddCap from './cap/Add.js';
import AddCountry from './country/Add.js';
import Home from './Home';
import {BrowserRouter as Router, Link, Route, Routes} from "react-router-dom";
import {Col, Container, Row} from 'react-bootstrap';

function App() {
    return (
        <Container fluid>
            <Router>
                <header>
                    <Row className="justify-content-center">
                        <Col xxl={2} xl={3} lg={4} md={6}><Link to="/">Home</Link></Col>
                        <Col xxl={2} xl={3} lg={4} md={6}><Link to="/caps">Caps</Link></Col>
                        <Col xxl={2} xl={3} lg={4} md={6}><Link to="/books">Books</Link></Col>
                    </Row>
                </header>
                <Routes>
                    <Route path="/country/add" element={<AddCountry/>}/>
                    <Route path="/cap/add" element={<AddCap/>}/>
                    <Route path="/book/add" element={<AddBook/>}/>
                    <Route path="/books" element={<Books/>}/>
                    <Route path="/countries" element={<Countries/>}/>
                    <Route path="/caps" element={<Caps/>}/>
                    <Route path="/" element={<Home/>}/>
                </Routes>
            </Router>
        </Container>
    );
}

export default App;