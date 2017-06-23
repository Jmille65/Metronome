import React from 'react';

class Metronome extends React.Component {
  constructor(props) {
    super(props);
    var cookieBpm = this.getCookie("bpm");
    var intBpm;
    if (cookieBpm !== "NaN" && cookieBpm !== "") {
      intBpm = parseInt(cookieBpm, 10);
    } else {
      intBpm = 90;
    }

    this.state = {
      bpm: intBpm,
      scheduleAheadTime: 0.1,
      schedulerTime: 0.051,
      nextClickTime: 0,
      playing: false
    };

    // loading audio
    var request = new XMLHttpRequest();
    request.open('GET', 'click.wav', true);
    request.responseType = 'arraybuffer';

    // Decode asynchronously
    request.onload = () => {
      console.log('request response type: ' + request.response + ', size: ' + request.response.byteLength);
      window.AudioContext.decodeAudioData(request.response, buf => {
        this.setState({decodedData: buf});
        console.log('decodedData: ' + this.state.decodedData);}, function () {
        console.error('The decode attempt failed.');
      });
    }
    request.onerror = () => {console.error('Request failed');}
    request.send();

    this.setTempo = this.setTempo.bind(this);
    this.toggleMetronome = this.toggleMetronome.bind(this);
    this.scheduler = this.scheduler.bind(this);
  }

  getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
      var c = ca[i];
      while (c.charAt(0) === ' ') {
        c = c.substring(1);
      }
      if (c.indexOf(name) === 0) {
        return c.substring(name.length, c.length);
      }
    }
    return "";
  }

  toggleMetronome() {
    var tempState = this.state;
    if (!this.state.playing) {
      tempState.playing = true;
      tempState.nextClickTime = window.AudioContext.currentTime;
      tempState.interval = setInterval(this.scheduler, Math.round(tempState.schedulerTime*1000));
    } else {
      tempState.playing = false;
      clearInterval(tempState.interval);
    }
    this.setState(tempState);
  }

  setTempo(newTempo) {
    this.setState({bpm: newTempo});
    document.cookie = 'bpm=' + newTempo + ';';
  }

  scheduler() {
    while (this.state.nextClickTime < window.AudioContext.currentTime + this.state.scheduleAheadTime) {
      this.schedulePlayback(this.state.decodedData, this.state.nextClickTime);
      this.setState({nextClickTime: this.state.nextClickTime + 60.0/this.state.bpm});
    }
  }

  schedulePlayback(stream, time = 0) {
    var buffer = window.AudioContext.createBufferSource();
    buffer.buffer = stream;
    buffer.connect(window.AudioContext.destination);
    buffer.start(time);
  }

  render () {
    return (
      <div>
      <h1>A Simple React Metronome</h1>
      <RangeInput currentTempo={this.state.bpm} setTempo={this.setTempo} />
      <NumberInput currentTempo={this.state.bpm} setTempo={this.setTempo} />
      <ClickButton newTempo={this.setTempo} currentTempo={this.state.bpm}/>
      <p><ToggleButton click={this.toggleMetronome} playing={this.state.playing} /></p>
      <p>Time between first load and next click: {this.state.nextClickTime}</p>
      </div>
    );
  }
}

class RangeInput extends React.Component {
  handleInput = e => {
    this.props.setTempo(e.target.value);
  }

  render () {
    return <input type="range" min="30" max="300" value={this.props.currentTempo} onInput={this.handleInput}></input>
  }
}

class NumberInput extends React.Component {
  handleInput = e => this.props.setTempo(e.target.value);

  render() {
    return <input type="number" min="30" max="300" value={this.props.currentTempo} onChange={this.handleInput}></input>
  }
}

// Props: playing = parent working or not, click = handler/toggle method in parent
class ToggleButton extends React.Component {
  render() {
    var text = this.props.playing ? "Stop" : "Start";
    return <button onClick={this.props.click}>{text}</button>
  }
}

class ClickButton extends React.Component {
  handleClick = () => {
    var newBpm;
    if (this.timeSelector) {
      this.timeA = Date.now();
      this.timeSelector = false;
      if (this.timeB !== 0) {
        newBpm = 60.0 / (this.timeA - this.timeB) * 1000;
      }
    } else {
      this.timeB = Date.now();
      this.timeSelector = true;
      newBpm = 60.0 / (this.timeB - this.timeA) * 1000;
    }
    if (newBpm < 300 && newBpm > 30) {
      newBpm = Math.round(newBpm);

      if (Math.abs(newBpm - this.props.currentTempo) < 15) { // if close to previous guess, smooths guesses
        newBpm = Math.round((newBpm + this.props.currentTempo) / 2);
      }

      this.props.newTempo(newBpm);
    }
  }

  render() {
    return <button onClick={this.handleClick}>Click</button>
  }
}

export default Metronome;
