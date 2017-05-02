<?php
include_once('config/config.php');
include_once('config/administradorBD.php');
$db = new administradorBD();

date_default_timezone_set('America/Mexico_City');

$response = array();

if($_SERVER['REQUEST_METHOD'] == 'POST') {

  $id_customer = $_POST['id_customer'];
  $total = 0.0;
  $online_purchase = $_POST['online_purchase'];
  $date_created = date('m/d/Y h:i:s a', time());
  $status = 1;

  if (!empty($id_customer) && !empty($online_purchase)) {

    if ($online_purchase == 1) {
      $id_seller = 5;
      $shipping_address = $_POST['shipping_address'];
      $zip_code = $_POST['zip_code'];
      $state = $_POST['state'];
    } else {
      $id_seller = $_POST['id_seller'];
      $shipping_address = "";
      $zip_code = "";
      $state = "";
    }

    if (!empty($id_seller)) {
      $query = "INSERT INTO PF_Orders (id_customer, id_seller, shipping_address, zip_code, state, total, online_purchase, date_created, status) VALUES ('$id_customer','$id_seller','$shipping_address','$zip_code','$state','$total','$online_purchase','$date_created','$status')";
      $conecta = mysql_connect(config::obtieneServidorBD(),config::obtieneUsuarioBD(),config::obtienePasswordBD());
  		if(!$conecta){
  			die('No puedo conectarme:' .mysql_error());
  		}
  		mysql_select_db(config::obtieneNombreBD(),$conecta);
  		$result = mysql_query($query);
      $inserted_id = mysql_insert_id();
      mysql_close($conecta);

      if($result){
        $response['code'] = "01";
        $response['message'] = "Order created successfully";
        $response['order_id'] = $inserted_id;
      } else {
        $response['code'] = "04";
        $response['message'] = "Seems that there was an error!";
      }
    } else {
      $response['code'] = "02";
      $response['message'] = "Missing mandatory parameters";
    }
  } else {
    $response['code'] = "02";
    $response['message'] = "Missing mandatory parameters";
  }
  echo json_encode($response);
}
?>
