<?
	require("include/main/DcWebManager.inc.php");
	require("include/main/smartyInit.inc.php");
	require("include/main/DcUnitConverter.inc.php");
	require("include/main/DcLoginManager.inc.php");
	require("include/main/DcFriendManager.inc.php");
	
	$lm = new DcLoginManager();
	$lm->checkLogin();
	$userId = $lm->getLoggedUserId();	
	$smarty = smartyInit();
	$manager = new DcWebManager();
	
	$fm = new DcFriendManager();
	$requests = $fm->getFriendRequests($userId);
	
	$users = array();
	for ($i=0; $i<count($requests); $i++)
	{
		$usr = $manager->loadUser($requests[$i]);
		array_push($users, $usr);
	}
	
	$smarty->assign('users', $users);	
	$smarty->display('friendRequests.tpl');
?>