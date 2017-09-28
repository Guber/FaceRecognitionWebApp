# FaceRecognitionWebApp

This is a part of my bachelor thesis in which I investigated the Fisherfaces, Eigenfaces, and LBPH methods for face recognition using the FaceRecognizer class in the OpenCV library and a custom database of images created for the purpose of this study. 

In addition, I created a web application which implements these methods on images captured from webcams. The web application consisted of a three-tiered implementation. The front-end was implemented in HTML5, CSS3, Bootstrap, jQuery, and the getUserMedia API. The intermediate layer was written in PHP to transfer the client image to the backend (third layer) which ran the face recognition algorithms in Java. The result was returned through PHP to the client.

# System tiers

  - frontend part (HTML/CSS + pure JS), currently located in webapp/
  - intermediate backend part(PHP), located in webapp/php
  - facerecognition backend(Java), located in javacore/

# Requirements

Frontend part uses getUserMedia API, to check it's upport in your browser see: http://caniuse.com/#feat=stream . I developed this using Mozilla and it worked like I charm. :)

Intermediate part should be run on a PHP server(Apache, Nginx,..). For quick start I recommend XAMPP: https://www.apachefriends.org/index.html .

In the facerecognition tier this project uses JavaCV as a layer beetwen Java and C++ library OpenCV. JavaCV requires an implementation of Java SE 7. For more see https://github.com/bytedeco/javacv#user-content-required-software .

Project was developed on JavaCV 1.1(JavaCPP 1.1, OpenCV 3.0.0-1.1). 

# Setup

You can use pom.xml file with the described dependency to download the required jar-s. Be sure to include them in the classpath of the project. 


If you have installed xampp for php server needs, put webapp folder inside it.

To open training and recognition part of the app run:
> localhost/webapp/training.html

To open just the recognition part run:
> localhosst/webapp/recognition.html
