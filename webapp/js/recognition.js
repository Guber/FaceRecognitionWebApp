window.addEventListener("DOMContentLoaded", function () {
  // Doing the recognition
  document.getElementById("recognition").addEventListener("click", function () {
  //get the part of the video that is placed inside the rectangle  
    context.drawImage(video,sliderValue,0.75*sliderValue,800-2*sliderValue,600-1.5*sliderValue,0,0,800*1.25,600*1.25);
    $("#result").html('Recognition started.');
	//picture is sent as a Base64
    
    $.ajax({
      type: "POST",
      url: "http://localhost/webapp/php/do_the_recognition.php",
      data: {
        imgBase64: canvas.toDataURL()
      }
    }).done(function (msg) {
       $("#result").html(msg);
      // Do Any thing you want
      //setTimeout(worker, 100);
    });
  });
}, false);

