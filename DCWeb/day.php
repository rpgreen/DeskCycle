<?
	require("include/main/DcWebManager.inc.php");
	require("include/main/smartyInit.inc.php");
	require("include/main/DcUnitConverter.inc.php");
	require("include/main/DcLoginManager.inc.php");
	require("include/main/DcFriendManager.inc.php");
		
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
		
	$startStamp = $_GET['t'];
	if (!$startStamp)
	{
		$startStamp = time();	//default to today
	}
	
	//assuming startStamp has been set to midnight 0:0:0
	$start = date("Y-m-d H:i:s", $startStamp);
	$endStamp = mktime(0, 0, 0, date("m", $startStamp), date("d", $startStamp)+1, date("y", $startStamp));
	$end = date("Y-m-d H:i:s", $endStamp);
	
	$manager = new DcWebManager();
	$user = $manager->loadUser($uId);
	$sessions = $manager->getDaysSessions($uId, $startStamp);
	
	//$sessions = $manager->getSessions($uId);	//TEST

	$convertedSessions = array();
	$distUnit = "km";
	$timeUnit = "h";
	$velUnit = $distUnit . "/" . $timeUnit;
	foreach ($sessions as $sess)
	{
		$s = DcUnitConverter::convertSession($sess, $distUnit, $timeUnit);
		array_push($convertedSessions, $s);
	}

	$totalDist = 0;
	$totalVel = 0;
	$averageVel = 0;
	$totalTime = array();

	$i=0;
	foreach ($convertedSessions as $sess)
	{
		$sTime = $sess->startTime;
		$eTime = $sess->endTime;
		$diff = getTimeDifference($sTime, $eTime);
		$totalTime['hours'] += $diff['hours'];
		$totalTime['minutes'] += $diff['minutes'];
		$totalTime['seconds'] += $diff['seconds'];
		$totalVel += $sess->averageVelocity;
		$totalDist += $sess->distance;
	}
	$totalTimeSecs = $totalTime['seconds'] + 60 * $totalTime['minutes'] + 3600 * $totalTime['hours'];
	$totalTime = sec2hms($totalTimeSecs);
	$averageVel = $totalVel / count($convertedSessions);
	
	$smarty->assign('uId', $uId);
	$smarty->assign('lastName', $user->lastName);
	$smarty->assign('firstName', $user->firstName);
	$smarty->assign('sessions', $convertedSessions);
	$smarty->assign('date', $start);
	$smarty->assign('distUnit', $distUnit);
	$smarty->assign('velUnit', $velUnit);
	$smarty->assign('totalDist', $totalDist );
	$smarty->assign('totalTime', formatHms($totalTime),1);
	$smarty->assign('averageVel', $averageVel);
	
	$smarty->display('day.tpl');
?>