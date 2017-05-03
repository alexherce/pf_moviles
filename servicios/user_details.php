<?php
include_once('config/config.php');
include_once('config/administradorBD.php');
$db = new administradorBD();
$response = array();

if($_SERVER['REQUEST_METHOD'] == 'GET') {

	$user_id = $_GET['id'];

	if (!empty($user_id)) {

		$query = "SELECT id, email, first_name, last_name, address, zip_code, state, rol FROM PF_Users WHERE id = '$user_id'";
		$result = $db->executeQuery($query);

    if(!empty($result)) {
  		$response['code'] = "01";
  		$response['user_data'] = mysql_fetch_assoc($result);
  	} else {
  		$response['code'] = "04";
  		$response['message'] = "Possible error executing the query.";
  	}
	} else {
    $response['code'] = "02";
    $response['message'] = "Missing mandatory parameters";
  }
	echo json_encode($response);
}

if($_SERVER['REQUEST_METHOD'] == 'POST') {

	$user_id = $_POST['user_id'];
  $email = $_POST['email'];
  $first_name = $_POST['first_name'];
  $last_name = $_POST['last_name'];
  $address = $_POST['address'];
  $zip_code = $_POST['zip_code'];
  $state = $_POST['state'];

	if (!empty($user_id) && !empty($email) && !empty($first_name) && !empty($last_name) && !empty($address) && !empty($zip_code) && !empty($state)) {

		$query = "UPDATE PF_Users SET email = '$email', first_name = '$first_name', last_name = '$last_name', address = '$address', zip_code = '$zip_code', state = '$state' WHERE id = '$user_id'";
		$result = $db->executeQuery($query);

    if(!empty($result)) {
  		$response['code'] = "01";
  	} else {
  		$response['code'] = "04";
  		$response['message'] = "Possible error executing the query.";
  	}
	} else {
    $response['code'] = "02";
    $response['message'] = "Missing mandatory parameters";
  }
	echo json_encode($response);
}
?>
