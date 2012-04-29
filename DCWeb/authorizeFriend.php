<?
	require("include/main/DcWebManager.inc.php");
	require("include/main/DcLoginManager.inc.php");
	require("include/main/DcFriendManager.inc.php");
	
	$lm = new DcLoginManager();
	$lm->checkLogin();
	$userId = $lm->getLoggedUserId();	
	
	$manager = new DcWebManager();
	$fm = new DcFriendManager();
		
	$form = "friendRequests.php";
	
	$friendId = $_GET['u'];
	$auth = $_GET['a'];
	
	if (!($friendId && $auth))
	{
		header("Location: ".$form);
	}
	else 
	{
		if ($auth == 1)
		{
			$fm->authorizeFriendRequest($userId, $friendId);
		}
		if ($auth == -1)
		{
			$fm->declineFriendRequest($userId, $friendId);
		}
		header("Location: friends.php");		
	}


?>