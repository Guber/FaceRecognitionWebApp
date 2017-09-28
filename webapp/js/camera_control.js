window.addEventListener("DOMContentLoaded", function () {
  var c = document.getElementById("rect");
    
  //drawing a rectangle
  var ctx = c.getContext("2d");
  ctx.save();
  ctx.fillStyle = "#FF0000";
  ctx.rect(0, 0, 800, 600);
  ctx.stroke();

  //
  $("#slider").slider({
      id: "",min: 0,max: 150,step: 5,range: false,handle: 'round',orientation: 'horizontal',formatter: function formatter(val) {
        if (Array.isArray(val)) {
          return val[0] + " : " + val[1];
        } else {
          return val;
        }
      }
    }
  );

//drawing a rectangle and set global variable(sliderValue) on slider value change
  $("#slider").change(function () {
    sliderValue = $("#slider").val();
    ctx.restore();
    ctx.clearRect(0, 0, 800, 600);
    ctx.beginPath();
    ctx.moveTo(0, 0);
    ctx.rect(sliderValue, 0.75*sliderValue, 800 - 2 * sliderValue, 600 - 1.5 * sliderValue);
    ctx.stroke();
    ctx.closePath();
  });

  // Grab elements, create settings, etc.
    var canvas = document.getElementById("canvas"), 
    video = document.getElementById("video"),
    videoObj = {"video": true},
    errBack = function (error) {
      console.log("Video capture error: ", error.code);
    };
	context = canvas.getContext("2d");
  // Put video listeners into place
  if (navigator.getUserMedia) { // Standard
    navigator.getUserMedia(videoObj, function (stream) {
      video.src = stream;
      video.play();
    }, errBack);
  } else if (navigator.webkitGetUserMedia) { // WebKit-prefixed
    navigator.webkitGetUserMedia(videoObj, function (stream) {
      video.src = window.webkitURL.createObjectURL(stream);
      video.play();
    }, errBack);
  } else if (navigator.mozGetUserMedia) { // WebKit-prefixed
    navigator.mozGetUserMedia(videoObj, function (stream) {
      video.src = window.URL.createObjectURL(stream);
      video.play();
    }, errBack);
  }


}, false);

