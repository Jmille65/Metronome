import React from 'react';
import { VictoryBar } from 'victory';

class Tuner extends React.Component {
  constructor(props) {
    super(props);
    var arr = new Uint8Array(1024);
    arr.fill(0);
    this.state = {
      frequency: 440,
      playing: false,
      data: arr
    }

  }

  // TODO
  toggleState = () => {
    if (this.state.playing) {
      this.setState({playing: false});
      this.state.stream.getTracks()[0].stop();
      clearInterval(this.state.interval);
    } else {
      navigator.mediaDevices.getUserMedia({audio: true, video: false})
      .then((stream) => {
        var source = window.AudioContext.createMediaStreamSource(stream);
        var analyser = window.AudioContext.createAnalyser();
        analyser.fftsize = 2048;
        source.connect(analyser);
        this.setState({
          playing: true,
          stream: stream,
          source: source,
          interval: setInterval(this.updateFrequency, 50),
          analyser: analyser
        });
      });
    }
  }

  updateFrequency = () => {
    var dataArray = new Uint8Array(this.state.analyser.frequencyBinCount);
    this.state.analyser.getByteFrequencyData(dataArray);
    var max = 0;
    var maxIndex = 0;

    for (var i = 0; i < this.state.analyser.frequencyBinCount; i++) {
      if (dataArray[i] > max) {
        max = dataArray[i];
        maxIndex = i;
      }
    }
    console.log("max: " + max + " index: " + maxIndex);

    this.setState({
      frequency: maxIndex * window.AudioContext.sampleRate / this.state.analyser.fftsize,
      data: dataArray
    });
  }

  render () {
    if (!navigator.mediaDevices) return (<h3>Tuner not available in Safari or IE</h3>);
    return (
      <div>
        <TunerDisplay>
          Frequency: {this.state.frequency}
        </TunerDisplay>
        <ToggleButton playing={this.state.playing} click={this.toggleState} />
        <VictoryBar data={Array.from(this.state.data)} />


      </div>
    );
  }
}

class ToggleButton extends React.Component {
  render() {
    var text = this.props.playing ? "Stop" : "Start";
    return <button onClick={this.props.click}>{text}</button>
  }
}

class TunerDisplay extends React.Component {
  render () {
    return (
      <div>
        <h1>A Simple React Tuner</h1>
        {this.props.children}
      </div>
    )
  }
}

export default Tuner;
