<?php
include_once('config/config.php');
include_once('config/administradorBD.php');
$db = new administradorBD();

if($_SERVER['REQUEST_METHOD'] == 'GET') {

	$product_id = $_GET['id'];

	if (isset($product_id)) {

		$query = "SELECT * FROM PF_Products WHERE id = $product_id";
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
		$response['message'] = "Possible error executing the query.";
	}
	echo json_encode($response);
}
?>
