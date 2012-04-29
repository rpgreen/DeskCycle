<?
	require("include/main/DcWebManager.inc.php");
	require("include/main/smartyInit.inc.php");
	require("include/main/DcUnitConverter.inc.php");
	require("include/main/DcLoginManager.inc.php");
	require("include/main/DcFriendManager.inc.php");
	
	$lm = new DcLoginManager();
	$lm->checkLogin();

	//user logged in if we get to this point
	$userId = $lm->getLoggedUserId();	
	$fm = new DcFriendManager();
			
	$usr = $_GET['u'];
	if (!$usr)
	{
		$usr = $userId;	//current user profile
		$fullProfile = true;
	}
	else 
	{
		$fullProfile = false;	//no effect yet
		
		if (!$fm->isFriend($userId, $usr))	//trying to view someone elses profile
		{ 
			header("Location: profile.php");
		}
	}
		
	$manager = new DcWebManager();
	$user = $manager->loadUser($usr);
		
	$smarty = smartyInit();
	
	$distUnit = "km";
	$totalDist = $manager->getDistanceSince($usr, 0);
	$totalDist = DcUnitConverter::convertDistance($totalDist, $distUnit);
	$firstSession = $manager->getFirstSession($usr);
	$firstTime = $firstSession->startTime;
	$totalTime = $manager->getTotalTimeSince($usr, 0);
	$totalTime = formatHms(sec2hms($totalTime),1);

	if (!$firstTime) $firstTime = "n/a";
	
	$smarty->assign('distUnit', $distUnit);	
	$smarty->assign('totalDist', $totalDist);	
	$smarty->assign('firstTime', $firstTime);
	$smarty->assign('totalTime', $totalTime);
	$smarty->assign('user', $user);	
	$smarty->display('profile.tpl');
?>