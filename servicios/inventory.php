<?php
include_once('config/config.php');
include_once('config/administradorBD.php');
$db = new administradorBD();

date_default_timezone_set('America/Mexico_City');

$response = array();

if($_SERVER['REQUEST_METHOD'] == 'GET') {

  $id_inventory = $_GET['id'];

  if (isset($id_inventory)) {

    $query = "SELECT i.id, i.id_product, i.id_store, i.quantity, i.size, p.name, p.brand, p.price, p.image_url, s.name as store FROM PF_Inventory i INNER JOIN PF_Products p ON i.id_product = p.id INNER JOIN PF_Store s ON i.id_store = s.id WHERE i.id = '$id_inventory'";
    $result = $db->executeQuery($query);

    if ($result) {
      $response['code'] = "01";
      $response['inventory_data'] = mysql_fetch_assoc($result);
    } else {
      $response['code'] = "03";
      $response['message'] = "Not existing products";
    }

  } else {
    $query = "SELECT i.id, i.id_product, i.id_store, i.quantity, i.size, p.name, p.brand, p.price, p.image_url, s.name as store FROM PF_Inventory i INNER JOIN PF_Products p ON i.id_product = p.id INNER JOIN PF_Store s ON i.id_store = s.id";
    $result = $db->executeQuery($query);

    if ($result) {
      $rows = array();

      while($r = mysql_fetch_assoc($result)) {
        $rows[] = $r;
      }
      $response['code'] = "01";
      $response['inventory_data'] = $rows;
    } else {
      $response['code'] = "03";
      $response['message'] = "Not existing products";
    }
  }

  echo json_encode($response);
}

if($_SERVER['REQUEST_METHOD'] == 'POST') {

  $id_product = $_POST['id_product'];
  $id_store = $_POST['id_store'];
  $quantity = $_POST['quantity'];
  $size = $_POST['size'];

  if (!empty($id_product) && !empty($id_store) && !empty($quantity) && !empty($size)) {

    $query = "INSERT INTO PF_Inventory (id_product, id_store, quantity, size) VALUES ('$id_product','$id_store','$quantity','$size')";
    $qresult = $db->executeQuery($query);

    if($qresult){
      $response['code'] = "01";
      $response['message'] = "Inventory item insert successful";
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

if($_SERVER['REQUEST_METHOD'] == 'PUT') {

  parse_str(file_get_contents('php://input'), $_PUT);

  $id_inventory = $_PUT['id'];
  $id_product = $_PUT['id_product'];
  $id_store = $_PUT['id_store'];
  $quantity = $_PUT['quantity'];
  $size = $_PUT['size'];

  if (!empty($id_inventory) && !empty($id_product) && !empty($id_store) && !empty($quantity) && !empty($size)) {

    $query = "UPDATE PF_Inventory SET id_product = '$id_product', id_store = '$id_store', quantity = '$quantity', size = '$size' WHERE id = '$id_inventory'";
    $qresult = $db->executeQuery($query);

    if($qresult){
      $response['code'] = "01";
      $response['message'] = "Inventory update successful";
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
