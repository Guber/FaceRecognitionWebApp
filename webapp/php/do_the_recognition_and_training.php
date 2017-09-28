<?php
header('Access-Control-Allow-Origin: *');
ini_set('max_execution_time', 300);

try {
	//get image data and decode it
	$rawData = $_POST['imgBase64'];
	$filteredData = explode(',', $rawData);
	$unencoded = base64_decode($filteredData[1]);
	//Create the image
	$fp = fopen('../test/test.jpg', 'w');
	fwrite($fp, $unencoded);
	fclose($fp);

	//call the jar with the training and recognition and echo the result
	$ret = shell_exec("java -jar ../jar/recognition_and_training.jar  ../img ../test/test.jpg 2>&1");

	echo $ret;
}

catch(Exception $e) {
	echo 'Things went wrong. Error: ' .$e->getMessage();
}
?>
