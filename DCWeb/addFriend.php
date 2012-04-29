<?
	require("include/main/DcWebManager.inc.php");
	require("include/main/smartyInit.inc.php");
	require("include/main/DcUnitConverter.inc.php");
	require("include/main/DcLoginManager.inc.php");
	
	$lm = new DcLoginManager();
	$lm->checkLogin();

	//user logged in if we get to this point
	$userId = $lm->getLoggedUserId();	
	
	$smarty = smartyInit();
	
	$e = $_GET['e'];
	$err = "";
	if ($e)
	{
		switch ($e)
		{
		case 1:
			$err .= "Please fill out all fields. <br/>";
		break;
		case 2:
			$err .= "No user found for that email address. <br/>";
		break;
		}
	}
	
	$smarty->assign('errorMsgs', $err);
				
	$smarty->display('addFriend.tpl');
?>
