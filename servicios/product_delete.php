<?php
include_once('config/config.php');
include_once('config/administradorBD.php');
$db = new administradorBD();

if($_SERVER['REQUEST_METHOD'] == 'POST') {

	$product_id = $_POST['id'];

  if (!empty($product_id)) {

    $query = "DELETE FROM PF_Products WHERE id = '$product_id'";
    $qresult = $db->executeQuery($query);

    if($qresult){
      $response['code'] = "01";
      $response['message'] = "Product delete successful";
    } else {
      $response['code'] = "04";
      $response['message'] = "Seems that there was an error!";
    }
  } else {
    $response['code'] = "02";
    $response['message'] = "Missing mandatory parameters";
  }
  echo json_encode($response);
}

?>
