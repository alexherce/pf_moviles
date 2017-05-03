<?php
include_once('config/config.php');
include_once('config/administradorBD.php');
$db = new administradorBD();
$response = array();

if($_SERVER['REQUEST_METHOD'] == 'GET') {

	$customer_id = $_GET['customer_id'];
  $order_id = $_GET['order_id'];

	if (isset($customer_id)) {

		$query = "SELECT id, id_customer, id_seller, total, date_created, status FROM PF_Orders WHERE id_customer = $customer_id";
		$result = $db->executeQuery($query);

    $rows = array();
  	while($r = mysql_fetch_assoc($result)) {
      	$rows[] = $r;
  	}

    if($result) {
  		$response['code'] = "01";
  		$response['order_data'] = $rows;
  	} else {
  		$response['code'] = "04";
  		$response['message'] = "Possible error executing the query.";
  	}

	} else if (isset($order_id)) {

    $query = "SELECT id, id_customer, id_seller, total, date_created, status, shipping_address, zip_code, state FROM PF_Orders WHERE id = $order_id";
		$result = $db->executeQuery($query);

    if($result) {
  		$response['code'] = "01";
  		$response['order_data'] = mysql_fetch_assoc($result);
  	} else {
  		$response['code'] = "04";
  		$response['message'] = "Possible error executing the query.";
  	}

	} else {
    $response['code'] = "02";
  }


	echo json_encode($response);
}
?>
