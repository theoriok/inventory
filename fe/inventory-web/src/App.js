import React from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Caps from './cap/Caps.js';
import Countries from './country/Countries.js';
import AddCap from './cap/Add.js';
import AddCountry from './country/Add.js';
import {BrowserRouter as Router, Link, Route, Routes} from "react-router-dom";
import {Container} from 'react-bootstrap';

function App() {
    return (
        <Container>
            <Router>
                <ul>
                    <li><Link to="/">Home</Link></li>
                    <li><Link to="/countries">Countries</Link></li>
                </ul>
                <Routes>
                    <Route path="/country/add" element={<AddCountry/>}/>
                    <Route path="/cap/add" element={<AddCap/>}/>
                    <Route path="/countries" element={<Countries/>}/>
                    <Route path="/" element={<Caps/>}/>
                </Routes>
            </Router>
        </Container>
    );
}

export default App;