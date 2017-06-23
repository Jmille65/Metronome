// necessary for audio playback
var decodedData;
var context;
var buffer;

// model
var bpm;
var playing = false;

// for click scheduling
var nextClickTime;
var scheduleAheadTime = 0.1;
var schedulerTime = 0.051;
var interval;

// for tap time button
var timeA = 0;
var timeB = 0;
var timeSelector = true;

window.onload = init;

function init() {

  // loading persistent bpm
  cookieBpm = getCookie("bpm");
  if (cookieBpm != "NaN" && cookieBpm != "") {
    bpm = parseInt(cookieBpm);
  } else {
    bpm = 90;
  }

  document.getElementById('inputText').value = bpm;
  document.getElementById('inputSlider').value = bpm;

  try {
    // Fix up for prefixing
    window.AudioContext = window.AudioContext||window.webkitAudioContext;
    context = new AudioContext();
  }
  catch(e) {
    alert('Web Audio API is not supported in this browser');
  }

  loadAudio('click.wav');
  document.getElementById('startButton').addEventListener('click', function (event) {
    if (!playing) {
      nextClickTime = context.currentTime;
      interval = setInterval(scheduler, Math.round(schedulerTime*1000));
      playing = true;
      this.innerHTML = "Stop";
      this.className = "button stop";
    } else {
      clearInterval(interval);
      playing = false;
      this.innerHTML = "Start";
      this.className = "button start";
    }
  });

  document.getElementById('inputText').addEventListener('keydown', function (event) {
    if (event.keyCode == 13 || event.keyCode == 9) {
      if (document.getElementById('inputText').value > 30 &&
      document.getElementById('inputText').value < 300) {
        bpm = document.getElementById('inputText').value;
        document.getElementById('inputSlider').value = bpm;
        setCookie("bpm", bpm);
      } else {
        document.getElementById('inputText').value = bpm;
        alert("Please enter a value between 30 and 300");
      }
    }
  });

  document.getElementById('tapButton').addEventListener('click', function(event) {
    var newBpm;
    if (timeSelector) {
      timeA = Date.now();
      timeSelector = false;
      if (timeB !== 0) {
        newBpm = 60.0 / (timeA - timeB) * 1000;
      }
    } else {
      timeB = Date.now();
      timeSelector = true;
      newBpm = 60.0 / (timeB - timeA) * 1000;
    }
    if (newBpm < 300 && newBpm > 30) {
      newBpm = Math.round(newBpm);
      if (Math.abs(newBpm - bpm) < 15) { // if close to previous guess, smooths guesses
        bpm = Math.round((newBpm + bpm) / 2);
      } else {
        bpm = newBpm;
      }
      document.getElementById('inputText').value = bpm;
      document.getElementById('inputSlider').value = bpm;
      setCookie("bpm", bpm);
    }
  });

  document.getElementById('inputSlider').addEventListener('input', function updateFromSlider (event) {
    bpm = document.getElementById('inputSlider').value;
    document.getElementById('inputText').value = bpm;
    setCookie("bpm", bpm);
  });
}



function loadAudio(url) {
  var request = new XMLHttpRequest();
  request.open('GET', url, true);
  request.responseType = 'arraybuffer';

  // Decode asynchronously
  request.onload = function() {
    context.decodeAudioData(request.response, function(buf) {decodedData = buf;}, function () { console.error('The request failed.');});
  }
  request.send();

}

function scheduler() {
  while (nextClickTime < context.currentTime + scheduleAheadTime) {
    schedulePlayback(decodedData, nextClickTime);
    nextClickTime += 60.0/bpm;
  }
}

function schedulePlayback(stream, time = 0) {
  buffer = context.createBufferSource();
  buffer.buffer = stream;
  buffer.connect(context.destination);
  buffer.start(time);
}

function setCookie(cname, cval) {
  document.cookie = cname + '=' + cval + ';';
}

function getCookie(cname) {
  var name = cname + "=";
  var decodedCookie = decodeURIComponent(document.cookie);
  var ca = decodedCookie.split(';');
  for(var i = 0; i <ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}

