<?php
include_once('config/config.php');
include_once('config/administradorBD.php');
$db = new administradorBD();

date_default_timezone_set('America/Mexico_City');

$response = array();

if($_SERVER['REQUEST_METHOD'] == 'POST') {

  $id_order = $_POST['id_order'];
  $id_product = $_POST['id_product'];
  $quantity = $_POST['quantity'];
  $size = $_POST['size'];
  $status = 1;

  if (!empty($id_order) && !empty($id_product) && !empty($quantity) && !empty($size)) {

    $queryStock = "SELECT i.id, i.id_store, i.quantity, p.price FROM PF_Inventory i INNER JOIN PF_Products p ON i.id_product = p.id WHERE i.id_product = $id_product AND i.size = $size AND i.quantity > 0 LIMIT 1";
    $checkStock = $db->executeQuery($queryStock);
    $stockRow = mysql_fetch_assoc($checkStock);

    if ($stockRow['quantity'] > 0) {
      $store_id = $stockRow['id_store'];
      $subtotal = $stockRow['price'];
      $id_stock = $stockRow['id'];

      $date_added = date('m/d/Y h:i:s a', time());
      $query = "INSERT INTO PF_OrderProducts (id_order, id_product, quantity, subtotal, from_store, status, size) VALUES ('$id_order','$id_product','$quantity','$subtotal','$store_id','$status','$size')";
      $qresult = $db->executeQuery($query);
      if($qresult) {

        $updateOrderQuery = "UPDATE PF_Orders SET total = total + $subtotal WHERE id = $id_order";
        $updateOrderTotal = $db->executeQuery($updateOrderQuery);

        $queryRemoveStock = "UPDATE PF_Inventory SET quantity = quantity - 1 WHERE id = $id_stock";
        $removeStock = $db->executeQuery($queryRemoveStock);

        if($removeStock) {
          $response['code'] = "01";
          $response['message'] = "Product order successful";
        } else {
          $response['code'] = "05";
          $response['message'] = "Error while trying to reserve product from stock";
        }
      } else {
        $response['code'] = "04";
        $response['message'] = "Seems that there was an error!";
      }
    } else {
      $response['code'] = "03";
      $response['message'] = "Product out of stock";
    }
  } else {
    $response['code'] = "02";
    $response['message'] = "Missing mandatory parameters";
  }
  echo json_encode($response);
}
?>
