<?
	require("include/main/DcWebManager.inc.php");
	require("include/main/smartyInit.inc.php");
	require("include/main/DcUnitConverter.inc.php");
	require("include/main/DcFriendManager.inc.php");
	require("include/activecalendar/source/activecalendarweek.php");
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
			header("Location: week.php");
		}
		else 
		{
			$uId = $u;
		}
	}
			
	$manager = new DcWebManager();
	$user = $manager->loadUser($uId);
	
	$smarty = smartyInit();
		
	$startStamp = $_GET['t'];	//the first day of the week. 
	if (!$startStamp)
	{
		$today = time();	//default to monday of this week
		$year = date("Y", $today);
		$month = date("n", $today);
		$day = date("j", $today);
		$dayNum = date('w', $today);	//start the week on the previous monday
		if ($dayNum == 0)
			$delta = 6;
		else if ($dayNum == 1)
			$delta = 0;
		else $delta = $dayNum - 1; 	//1 is monday	
		$day = $day - $delta;
		$startStamp = mktime(0,0,0, $month, $day, $year);
	}
	
	$year = date("Y", $startStamp);
	$month = date("n", $startStamp);
	$day = date("j", $startStamp);
	$startDate = $startStamp;
	$endDate = mktime(23, 59, 59, $month, $day+6, $year);
	$prevWeek = mktime(0, 0, 0, $month, $day-7, $year);
	$nextWeek = mktime(0, 0, 0, $month, $day+7, $year);
	
	$cal = new ActiveCalendarWeek($year,$month,$day);	
	if ($uId == $lm->getLoggedUserId()) 
		$cal->enableDayLinks("day.php");
	
	$distUnit = "km";
	$timeUnit = "h";
	$velUnit = "km/h";
	
	$totalDist = 0;
	$totalVel = 0;
	$averageVel = 0;
	$totalTime = array();
	//set the session events
	for ($i=0; $i<7; $i++)
	{
		$dayStamp = mktime(0, 0, 0, $month, $day+$i, $year);
		$ss = $manager->getDaysSessions($uId, $dayStamp);
		$sessions = DcUnitConverter::convertSessions($ss, $distUnit, $timeUnit);
	
		$content = "";
		for ($j=0; $j<count($sessions); $j++)
		{
			$session = $sessions[$j];
			$ts = strtotime($session->startTime);
			$dateFrmtd = date("g:ia", $ts);
			$content .= "<div class=\"wkSessionDist\">";
			$content .= $dateFrmtd.": <a href=\"viewSession.php?u=".$uId."&s=";
			$content .= $session->sessionId."\">".number_format($session->distance,2)."</a> ".$distUnit."</div>";
			
			$sTime = $session->startTime;
			$eTime = $session->endTime;
			$diff = getTimeDifference($sTime, $eTime);
			$totalTime['hours'] += $diff['hours'];
			$totalTime['minutes'] += $diff['minutes'];
			$totalTime['seconds'] += $diff['seconds'];
			$totalVel += $session->averageVelocity;
			$totalDist += $session->distance;
		}
		$cal->setEventContent($year, $month, $day+$i, $content);
	}

	$totalTimeSecs = $totalTime['seconds'] + 60 * $totalTime['minutes'] + 3600 * $totalTime['hours'];
	$totalTime = sec2hms($totalTimeSecs);
	$averageVel = $totalVel / count($sessions);
		
	$week = $cal->showWeeks();

	$smarty->assign('uId', $uId);
	$smarty->assign('user', $user);
	$smarty->assign('startDate', $startDate);
	$smarty->assign('distUnit', $distUnit);
	$smarty->assign('velUnit', $velUnit);
	$smarty->assign('endDate', $endDate);
	$smarty->assign('nextWeek', $nextWeek);
	$smarty->assign('prevWeek', $prevWeek);
	$smarty->assign('totalDist', $totalDist);
	$smarty->assign('totalTime', formatHms($totalTime),1);
	$smarty->assign('averageVel', $averageVel);
	$smarty->assign('week', $week);
	$smarty->display('week.tpl');
?>
