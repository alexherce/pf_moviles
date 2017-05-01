<?php

include_once('config/config.php');
include_once('config/administradorBD.php');
date_default_timezone_set('America/Mexico_City');

$db = new administradorBD();
$response = array();

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$email = $_POST['email'];
	$pass = $_POST['password'];

	if (!empty($email) && !empty($pass)) {
		$query = "SELECT id, email, first_name, last_name, address, zip_code, state, rol FROM PF_Users WHERE email = '$email' AND password = '$pass'";
		$qresult = $db->executeQuery($query);

    if(mysql_num_rows($qresult) == 1) {
      $row = mysql_fetch_assoc($qresult);
      $id = $row['id'];
      $login_date = date('m/d/Y h:i:s a', time());
      $lastLoginQuery = "UPDATE PF_Users SET last_login = '$login_date' WHERE id = '$id'";
  		$db->executeQuery($lastLoginQuery);
  		$response['code'] = "01";
  		$response['message'] = "Login successful";
      $response['user_data'] = $row;
  	} else if(mysql_num_rows($qresult) == 0) {
  		$response['code'] = "03";
  		$response['message'] = "Incorrect credentials";
  	} else {
      $response['code'] = "04";
  		$response['message'] = "Unknown error. Possible duplicate users or query failed.";
    }
	} else {
    $response['code'] = "02";
    $response['message'] = "Missing mandatory parameters";
  }
	echo json_encode($response);
}
?>
