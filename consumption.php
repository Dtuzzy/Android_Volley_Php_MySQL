<?php
include_once("config.php");
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");


$database = new Database();
$conn = $database->getConnection();


$data = json_decode(@file_get_contents("php://input"), true);

if($conn){


  $station_code =  trim(mysqli_real_escape_string($conn, !empty($data['station_code']) ? $data['station_code']: ""));
  $post_date = trim(mysqli_real_escape_string($conn, !empty($data['post_date']) ? $data['post_date']: ""));
  $product_type = trim(mysqli_real_escape_string($conn, !empty($data['product_type']) ? $data['product_type']: ""));
  $group = trim(mysqli_real_escape_string($conn, !empty($data['product_group']) ? $data['product_group']: ""));
  $sales_quantity = trim(mysqli_real_escape_string($conn, !empty($data['sales_quantity']) ? $data['sales_quantity']: ""));


	$database->report_log($station_code. "||" . $product_type. "||" . date("Y-m-d h:m:s"));
	 $reportsSql = "INSERT INTO `consumption`(`station_code`, `post_date`, `product_type`, `group`, `sales_quantity`) VALUES ('".$station_code."','".$post_date."','".$product_type."','".$group."','".$sales_quantity."')";
    $rptQry = mysqli_query($conn,  $reportsSql);

  
	echo json_encode(array("success" => true));
	
}else{
	
	echo json_encode(array("success" => false));
}
	
 
  
  




?>