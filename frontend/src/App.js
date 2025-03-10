import React from 'react';
import './index.css'; // Importing Tailwind CSS
import { Route, Routes } from 'react-router';
import Home from './components/Home/Home';
import AddItem from './components/AddItem/AddItem';
import DisplayItem from './components/DisplayItems/DisplayItem';

function App() {
  return (
    <div className="App container mx-auto p-4">
      <React.Fragment>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/additem" element={<AddItem />} />
          <Route path="/allitem" element={<DisplayItem />} />
         

        </Routes>
      </React.Fragment>
    </div>
  );
}

export default App;
