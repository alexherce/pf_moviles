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

if($_SERVER['REQUEST_METHOD'] == 'PUT') {

	parse_str(file_get_contents('php://input'), $_PUT);

	$product_id = $_PUT['id'];
  $name = $_PUT['name'];
  $description = $_PUT['description'];
  $image_url = $_PUT['image_url'];
  $price = $_PUT['price'];
  $brand = $_PUT['brand'];
  $id_category = $_PUT['id_category'];

  if (!empty($product_id) && !empty($name) && !empty($description) && !empty($image_url) && !empty($price) && !empty($brand) && !empty($id_category)) {

    $query = "UPDATE PF_Products SET name = '$name', description = '$description', image_url = '$image_url', price = '$price', brand = '$brand', id_category = '$id_category' WHERE id = '$product_id'";
    $qresult = $db->executeQuery($query);

    if($qresult){
      $response['code'] = "01";
      $response['message'] = "Product update successful";
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
