<?php

require_once '../includes/DbOperations.php';

$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
	if(isset($_POST['username'])){
		$db = new DbOperations();
		
		$user = $db->getUserByUsername($_POST['username']);
		$response['error'] = false;
		$response['id'] = $user['id'];
		
		
		if(isset($user['id'])){
			$db = new DbOperations;
			
			$user = $db->getBadgeByUserId($user['id']);
			$response['id'] = $user['id'];
			$response['lat'] = $user['lat'];
			$response['lon'] = $user['lon'];
			$response['date'] = $user['date'];
			$response['message'] = "fetched cordinates";
			
		}else{
		$response['error'] = true;
		$response['message'] = "Could Not get values";	
		}
	}else{
		$response['error'] = true;
		$response['message'] = "Something Went Wrong Showing updated location";	
	}
}

echo json_encode($response);