 <?php 
 
require_once '../includes/DbOperations.php';

$response = array();

 if($_SERVER['REQUEST_METHOD']=='POST'){
	 if(isset($_POST['username'])){
		
		$db = new DbOperations();
		
		$user = $db->getUserByUsername($_POST['username']);
		$response['error'] = false;
		$response['id'] = $user['id'];
		// $response['email'] = $user['email'];
		// $response['username'] = $user['username'];
		
		if(isset($user['id']) and isset($_POST['username'])){
			$db = new DbOperations();
			$result = $db->saveLocation($_POST['lat'],
				$_POST['lon'],
				$_POST['date'],
				$user['id']);
			
			if($result == 5){
				$response['error'] = false;
				$response['message'] = "Table  Badges Updated";
				
				$db = new DbOperations;
				$userid = $db->getBadgeByUserId($user['id']);
				$response['id'] = $userid['id'];
				$response['lat'] = $userid['lat'];
				$response['lon'] = $userid['lon'];
				$response['date'] = $userid['date'];
				
			}elseif($result == 6){
					$response['error'] = true;
					$response['message'] = "Table Badges Not Updated";
			}
		}
 }else{
	 $response['error'] = true;
	 $responser['message'] = "Invalid Request";
	 
 }
 
 }
 echo json_encode($response);