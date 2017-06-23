import React from 'react';
import './App.css';
import Metronome from './Metronome';
import Tuner from './Tuner';

class App extends React.Component {
  constructor(props) {
    super(props);
    try {
      // Fix up for prefixing
      window.AudioContext = window.AudioContext||window.webkitAudioContext;
      window.AudioContext = new AudioContext();
    }
    catch(e) {
      alert('Web Audio API is not supported in this browser');
    }
  }

  render() {
    return (
      <div>
        <Metronome />
        <Tuner />
      </div>
    );
  }
}

export default App;
