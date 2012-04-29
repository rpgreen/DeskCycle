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
	
	$numSessions = 10;
	$manager = new DcWebManager();
	$sessions = $manager->getLastSessions($uId, $numSessions);
	$user = $manager->loadUser($uId);
	$smarty = smartyInit();
	
	$convertedSessions = array();
	$distUnit = "km";
	$timeUnit = "h";
	$velUnit = $distUnit . "/" . $timeUnit;
	foreach ($sessions as $sess)
	{
		$s = DcUnitConverter::convertSession($sess, $distUnit, $timeUnit);
		array_push($convertedSessions, $s);
	}
	$sessions = $convertedSessions;
	
	//get max dist
	$maxdist = 0;
	for ($i=0; $i<count($sessions); $i++)
	{
		$thisDist = $sessions[$i]->distance;
		if ($thisDist > $maxDist)
		{
			$maxDist = $thisDist;
		}
	}
	
	//get max time
	//TODO:
	
	//do this in php, smarty hard
	$maxHeight = 300;
	for ($i=0; $i<count($sessions); $i++) 
	{
		$h = ($sessions[$i]->distance / $maxDist) * $maxHeight;
		if ($h < 10) $h=10;
		$cells1 .= "<td valign=\"bottom\">"
			. "<a href=\"viewSession.php?u=".$uId."&s=".$sessions[$i]->sessionId."\">"
			."<img src=\"images/metre_bg.gif\" width=\"40\" height=\"".$h."\" alt=\"".$sessions[$i]->distance."\" border=\"0\">"
			."</a></td>";
	}

	$smarty->assign('distUnit', $distUnit);	
	$smarty->assign('cells1', $cells1);	
	$smarty->assign('user', $user);	
	$smarty->assign('maxDist', $maxDist);	
	$smarty->assign('maxTime', $maxTime);	
	$smarty->assign('numSessions', $numSessions);
	$smarty->assign('sessions', $sessions);
	$smarty->display('recentSessions.tpl');
?>