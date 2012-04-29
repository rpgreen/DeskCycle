<?
	require("include/main/DcWebManager.inc.php");
	require("include/main/DcLoginManager.inc.php");
	require("include/main/DcFriendManager.inc.php");
	
	$lm = new DcLoginManager();
	$lm->checkLogin();
	$userId = $lm->getLoggedUserId();	
	
	$form = "addFriend.php";
	
	$email = $_POST['email'];
	$manager = new DcWebManager();
	$fm = new DcFriendManager();
		
	if (! ($email))
	{
		header("Location: ".$form."?e=1");
	}
	else if (! $manager->getUserIdByEmail($email))
	{
		header("Location: ".$form."?e=2");
	}
	else 
	{
		$friendId = $manager->getUserIdByEmail($email);
		
		//TODO: not checking if already friends
		$fm->addFriendRequest($userId, $friendId);
		header("Location: friends.php");		
	}


?>