<?
	require("include/main/DcWebManager.inc.php");
	require("include/main/DcFriendManager.inc.php");
	require("include/main/smartyInit.inc.php");
	require("include/main/DcUnitConverter.inc.php");
	require("include/main/DcLoginManager.inc.php");
	
	$lm = new DcLoginManager();
	$lm->checkLogin();

	//user logged in if we get to this point
	$uId = $lm->getLoggedUserId();	
	
	$fm = new DcFriendManager();
	//handle friend param
	$u = $_GET['u'];
	if ($u)
	{
		if (!$fm->isFriend($uId, $u))	//trying to view someone elses profile
		{ 
			header("Location: day.php");
		}
		else 
		{
			$uId = $u;
		}
	}
		
	$smarty = smartyInit();

	$sId = $_GET['s'];

	$manager = new DcWebManager();
	$user = $manager->loadUser($uId);
	$s = $manager->getSession($sId);
	
	if (!$manager->isSessionOwnedByUser($sId, $uId))
	{
		//make sure session belongs to user
		$err[0] = "You do not have permission to view this session.";
		$smarty->assign('errors', $err );
		$smarty->display('error.tpl');
	}
	
	else 
	{
		$device = $manager->getDevice($s->deviceId);
		
		$distUnit = "km";
		$timeUnit = "h";
		$velUnit = $distUnit . "/" . $timeUnit;
		
		$session = DcUnitConverter::convertSession($s, $distUnit, $timeUnit);
		
		$smarty->assign('sessId', $session->sessionId);
		$smarty->assign('deviceName', $device->name);
		$smarty->assign('lastName', $user->lastName);
		$smarty->assign('firstName', $user->firstName);
		$smarty->assign('startTime', $session->startTime);
		$smarty->assign('endTime', $session->endTime);
		$smarty->assign('duration', $session->elapsed);
		$smarty->assign('distUnit', $distUnit);
		$smarty->assign('velUnit', $velUnit);
		$smarty->assign('totalDist', $session->distance);
		$smarty->assign('averageVel', $session->averageVelocity);
		
		$smarty->display('viewSession.tpl');
	}
?>