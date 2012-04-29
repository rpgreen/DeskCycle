<?
	include("include/main/DcWebManager.inc.php");

	//test loadUser
	$manager = new DcWebManager();
	$usr = $manager->loadUser(1);
	//print_r($usr);
	
	//test getUserId
	$id = $manager->getUserId("demo");
	//print $id;
	
	//test saveUser
	$usr->weight = 200;
	//$manager->saveUser($usr);
	
	//test getSessions & velocity points
	$sessions =	$manager->getSessions($id);
	$sess = $sessions[0];
	$pts = $sess->velPoints;
	//print_r($pts);
	
	//test getSessions with datetime limits
	$earlyDate = date("Y-m-d H:i:s");
	$lateDate =  date("Y-m-d H:i:s");
	$sessions =	$manager->getSessions($id, "", "");
	dump($sessions);
?>