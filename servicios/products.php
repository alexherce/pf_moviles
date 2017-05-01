<?php
include_once('config/config.php');
include_once('config/administradorBD.php');
$db = new administradorBD();

if($_SERVER['REQUEST_METHOD'] == 'GET') {

	$id_producto = $_GET['id'];

	if (isset($id_producto)) {

		$query = "SELECT * FROM PF_Products WHERE id = $id_producto";
		$result = $db->executeQuery($query);

	} else {

		$query = "SELECT * FROM PF_Products";
		$result = $db->executeQuery($query);

	}

	$rows = array();
	while($r = mysql_fetch_assoc($result)) {
    	$rows[] = $r;
	}
	$response = array();

	if($result) {
		$response['code'] = "01";
		$response['product_data'] = $rows;
	} else {
		$response['code'] = "04";
		$response['message'] = "Seems that there was an error :(";
	}
	echo json_encode($response);
}
?>
