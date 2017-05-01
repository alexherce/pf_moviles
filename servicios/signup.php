<?php

include_once('config/config.php');
include_once('config/administradorBD.php');
date_default_timezone_set('America/Mexico_City');

$db = new administradorBD();
$response = array();

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$email = $_POST['email'];
	$password = $_POST['password'];
  $first_name = $_POST['first_name'];
  $last_name = $_POST['last_name'];
  $address = $_POST['address'];
  $zip_code = $_POST['zip_code'];
  $state = $_POST['state'];
  $rol = 3;

	if (!empty($email) && !empty($password) && !empty($first_name) && !empty($last_name) && !empty($address) && !empty($zip_code) && !empty($state)) {

    $verifyQuery = "SELECT id FROM PF_Users WHERE email = '$email'";
    $resultVerify = $db->executeQuery($verifyQuery);

    if(mysql_num_rows($resultVerify) > 0) {
      $response['code'] = "03";
  		$response['message'] = "Email is already registered";
		} else {
      $date_created = date('m/d/Y h:i:s a', time());
      $hashed_password = hash('sha256', $password);
      $query = "INSERT INTO PF_Users (email, first_name, last_name, zip_code, state, address, password, rol, created_date) VALUES ('$email','$first_name','$last_name','$zip_code','$state','$address','$hashed_password','$rol', '$date_created')";
  		$qresult = $db->executeQuery($query);

      if($qresult){
    		$response['code'] = "01";
    		$response['message'] = "Signup successful";
    	} else {
    		$response['code'] = "04";
    		$response['message'] = "Seems that there was an error!";
    	}
    }
	} else {
    $response['code'] = "02";
    $response['message'] = "Missing mandatory parameters";
  }
	echo json_encode($response);
}
?>
