<?php
include_once('config/config.php');
include_once('config/administradorBD.php');
$db = new administradorBD();

date_default_timezone_set('America/Mexico_City');

$response = array();

if($_SERVER['REQUEST_METHOD'] == 'POST') {

  $id_order = $_POST['id_order'];

  if (!empty($id_order)) {

    $cancelProductsQuery = "DELETE FROM PF_OrderProducts WHERE id_order = '$id_order'";
    $cancelProducts = $db->executeQuery($cancelProductsQuery);

    if($cancelProductsQuery) {
      $cancelOrderQuery = "DELETE FROM PF_Orders WHERE id = '$id_order'";
      $cancelOrder = $db->executeQuery($cancelOrderQuery);

      if($cancelOrder) {
        $response['code'] = "01";
        $response['message'] = "Order and products canceled";
      } else {
        $response['code'] = "04";
        $response['message'] = "Error in order cancelation query";
      }
    } else {
      $response['code'] = "04";
      $response['message'] = "Error in product cancelation query";
    }
  } else {
    $response['code'] = "02";
    $response['message'] = "Missing mandatory parameters";
  }
  echo json_encode($response);
}
?>
