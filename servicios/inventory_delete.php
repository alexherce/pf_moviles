<?php
include_once('config/config.php');
include_once('config/administradorBD.php');
$db = new administradorBD();

if($_SERVER['REQUEST_METHOD'] == 'POST') {

	$id_inventory = $_POST['id'];

  if (!empty($id_inventory)) {

    $query = "DELETE FROM PF_Inventory WHERE id = '$id_inventory'";
    $qresult = $db->executeQuery($query);

    if($qresult){
      $response['code'] = "01";
      $response['message'] = "Inventory item delete successful";
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
