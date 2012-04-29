<?
	require("include/main/DcLoginManager.inc.php");
	require("include/main/DcWebManager.inc.php");
	
	$u = $_POST['username'];
	$p = $_POST['password'];
	
	$lm = new DcLoginManager();
	$manager = new DcWebManager();
	$v = $lm->validateUser($u, $p);
	
	if (!$v)
	{
		header("Location: login.php?e=1");
	}
	else 
	{
		$id = $manager->getUserId($u);
		$lm->loginUser($id);
		header("Location: profile.php");	//TODO: change to home
	}
	
?>