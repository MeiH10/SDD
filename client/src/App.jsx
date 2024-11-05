// src/App.jsx
// eslint-disable-next-line no-unused-vars
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';

import UseCase1 from './pages/UseCase1';
import UseCase2 from './pages/UseCase2';
import UseCase3 from './pages/UseCase3';
import UseCase4 from './pages/UseCase4';
import UseCase5 from './pages/UseCase5';
import UseCase6 from './pages/UseCase6';
import UseCase7 from './pages/UseCase7';
import UseCase8 from './pages/UseCase8';
import UseCase9 from './pages/UseCase9';
import UseCase10 from './pages/UseCase10';
import UseCase11 from './pages/UseCase11';

const App = () => {
  return (
    <Router>
      <div>
        <nav>
          <ul>
            <li><Link to="/uc1">Use Case 1</Link></li>
            <li><Link to="/uc2">Use Case 2</Link></li>
            <li><Link to="/uc3">Use Case 3</Link></li>
            <li><Link to="/uc4">Use Case 4</Link></li>
            <li><Link to="/uc5">Use Case 5</Link></li>
            <li><Link to="/uc6">Use Case 6</Link></li>
            <li><Link to="/uc7">Use Case 7</Link></li>
            <li><Link to="/uc8">Use Case 8</Link></li>
            <li><Link to="/uc9">Use Case 9</Link></li>
            <li><Link to="/uc10">Use Case 10</Link></li>
            <li><Link to="/uc11">Use Case 11</Link></li>
          </ul>
        </nav>

        <Routes>
          <Route path="/uc1" element={<UseCase1 />} />
          <Route path="/uc2" element={<UseCase2 />} />
          <Route path="/uc3" element={<UseCase3 />} />
          <Route path="/uc4" element={<UseCase4 />} />
          <Route path="/uc5" element={<UseCase5 />} />
          <Route path="/uc6" element={<UseCase6 />} />
          <Route path="/uc7" element={<UseCase7 />} />
          <Route path="/uc8" element={<UseCase8 />} />
          <Route path="/uc9" element={<UseCase9 />} />
          <Route path="/uc10" element={<UseCase10 />} />
          <Route path="/uc11" element={<UseCase11 />} />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
