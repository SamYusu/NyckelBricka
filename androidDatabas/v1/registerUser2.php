<?php

require_once '../includes/DbOperations.php';

$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
	
	if(
		isset($_POST['username']) and 
			isset($_POST['password']) and 
				isset($_POST['email'])
		){
			//går framåt	
			
			$db = new DbOperations();
			
			$result = $db->createUser($_POST['username'],
									$_POST['password'],
									$_POST['email']);
			
			if($result == 1){
					$response['error'] = false;
					$response['message'] = "User Registered";
					
					if(isset($_POST['username']) and isset($_POST['password'])){
		
						$db = new DbOperations();
		
						$user = $db->getUserByUsername($_POST['username']);
						$response['error'] = false;
						$response['id'] = $user['id'];
						$response['email'] = $user['email'];
						$response['username'] = $user['username'];
		
						if(isset($user['id'])){
							$result = $db->createBadge($user['id']);
			
							if($result == 3){
								$response['error'] = false;
								$response['message'] = "Table Badges Created";
							}elseif($result == 4){
								$response['error'] = true;
								$response['message'] = "Table Badges Not Created";
							}
						}			
					}	
						
				}
				elseif($result == 2){
					$response['error'] = true;
					$response['message'] = "Something Went Wrong";					
				}
				elseif($result == 0){
					$response['error'] = true;
					$response['message'] = "User or Email already exists";
				}
		
	}else{
		$response['error'] = true;
		$response['message'] = "Invalid Fields";
	}
	
 
	
}else{
	$response['error'] = true;
	$response['message'] = "Invalid Request";
}

echo json_encode($response);