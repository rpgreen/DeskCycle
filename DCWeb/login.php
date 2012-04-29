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
			$err .= "Invalid Username/Password.";
		break;
		}
	}
	
	$smarty->assign('errorMsgs', $err);
	$smarty->display('login.tpl');
?>
