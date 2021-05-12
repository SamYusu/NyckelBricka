<?php

require_once '../includes/DbOperations.php';

$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
	if(isset($_POST['username']) and isset($_POST['password'])){
		$db = new DbOperations();
		
		if($db->userLogin($_POST['username'], $_POST['password'])){
			$user = $db->getUserByUsername($_POST['username']);
			$response['error'] = false;
			$response['id'] = $user['id'];
			$response['email'] = $user['email'];
			$response['username'] = $user['username'];
		}else{
		$response['error'] = true;
		$response['message'] = "Invalid Username or Password";	
		}
	}else{
		$response['error'] = true;
		$response['message'] = "Something Went Wrong";	
	}
}

echo json_encode($response);