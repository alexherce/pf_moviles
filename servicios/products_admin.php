<?php
include_once('config/config.php');
include_once('config/administradorBD.php');
$db = new administradorBD();

date_default_timezone_set('America/Mexico_City');

$response = array();

if($_SERVER['REQUEST_METHOD'] == 'POST') {

  $name = $_POST['name'];
  $description = $_POST['description'];
  $image_url = $_POST['image_url'];
  $price = $_POST['price'];
  $brand = $_POST['brand'];
  $id_category = $_POST['id_category'];

  if (!empty($name) && !empty($description) && !empty($image_url) && !empty($price) && !empty($brand) && !empty($id_category)) {

    $date_added = date('m/d/Y h:i:s a', time());
    $query = "INSERT INTO PF_Products (name, description, image_url, price, date_added, brand, id_category) VALUES ('$name','$description','$image_url','$price','$date_added','$brand','$id_category')";
    $qresult = $db->executeQuery($query);

    if($qresult){
      $response['code'] = "01";
      $response['message'] = "Product inserted successful";
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
