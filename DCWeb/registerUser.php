<?
	require("include/main/DcWebManager.inc.php");
	require("include/main/smartyInit.inc.php");
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
			$err .= "Password fields do not match. Please retype password. <br/>";
		break;
		}
	}
	
	$smarty->assign('errorMsgs', $err);

	$smarty->display('registerUser.tpl');
?>