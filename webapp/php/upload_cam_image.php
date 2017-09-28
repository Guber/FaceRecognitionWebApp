<?php

header('Access-Control-Allow-Origin: *');

try {
	//get image data
	$rawData = $_POST['imgBase64'];
	$filteredData = explode(',', $rawData);
	$unencoded = base64_decode($filteredData[1]);

	//Create the image
	$fp = fopen('../img/' . $_POST['label'] . '.jpg', 'w');
	fwrite($fp, $unencoded);
	fclose($fp);

	echo "Test image ". $_POST['label']. " succesfully added.";
}

catch(Exception $e) {
	echo 'Test image not added. Error: ' .$e->getMessage();
}

?>
