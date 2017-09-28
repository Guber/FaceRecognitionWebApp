window.addEventListener("DOMContentLoaded", function () {
  

// Doing the recognition
  document.getElementById("recognition").addEventListener("click", function () {
  //get the part of the video that is placed inside the rectangle  
    context.drawImage(video,sliderValue,0.75*sliderValue,800-2*sliderValue,600-1.5*sliderValue,0,0,800*1.25,600*1.25);
    $("#result").html('Recognition started.');
	//picture is sent as a Base64
    
    $.ajax({
      type: "POST",
      url: "http://localhost/webapp/php/do_the_recognition_and_training.php",
      data: {
        imgBase64: canvas.toDataURL()
      }
    }).done(function (msg) {
       $("#result").html(msg);
      // Do Any thing you want
      //setTimeout(worker, 100);
    });
  });


//upload the image by ajax call to php script
  document.getElementById("upload").addEventListener("click", function () {
	//get the part of the video that is placed inside the rectangle  
    context.drawImage(video,sliderValue,0.75*sliderValue,800-2*sliderValue,600-1.5*sliderValue,0,0,800*1.25,600*1.25);

	//picture is sent as a Base64
    $.ajax({
      type: "POST",
      url: "http://localhost/webapp/php/upload_cam_image.php",
      data: {
        imgBase64: canvas.toDataURL(),
        "label": $("#label").val()
      }
    }).done(function (msg) {
	  $("#upload_result").html("<div class=\"alert alert-success fade in alert-dismissable\">"+
	  "<a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\" title=\"close\">×</a>"+
	  "<strong>Success!</strong>" + msg +"</div>");
    });
  });
}, false);

