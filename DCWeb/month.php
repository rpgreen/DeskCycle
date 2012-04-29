<?
	require("include/main/DcWebManager.inc.php");
	require("include/main/DcLoginManager.inc.php");
	require("include/main/DcFriendManager.inc.php");
	require("include/main/smartyInit.inc.php");
	require("include/main/DcUnitConverter.inc.php");
	require("include/activecalendar/source/activecalendarweek.php");
	
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
			header("Location: month.php");
		}
		else 
		{
			$uId = $u;
		}
	}
	
			
	$distUnit = "km";
	$timeUnit = "h";
	$velUnit = "km/h";
	
	$manager = new DcWebManager();
	$user = $manager->loadUser($uId);
	
	$smarty = smartyInit();
		
	$startStamp = $_GET['t'];	//the first day of the week. 
	if (!$startStamp)
		$startStamp = time();	//default to today
	
	$year = date("Y", $startStamp);
	$month = date("n", $startStamp);
	$day = date("j", $startStamp);
	$cal = new ActiveCalendar($year,$month);	
	if ($uId == $lm->getLoggedUserId()) 
		 $cal->enableDayLinks("day.php");
	$cal->enableMonthNav("month.php?u=".$uId);
	
	//$cal->enableDatePicker();		//TODO: consider this for future
	
	$day = 1;	//set timestamp to the start of the month
	$startDate = $startStamp;
	$prevMonth = mktime(0, 0, 0, $month-1, $day, $year);
	$nextMonth = mktime(0, 0, 0, $month+1, $day, $year);
			
	$totalDist = 0;
	$totalVel = 0;
	$averageVel = 0;
	$totalTime = array();

	$days = daysInMonth($month, $year);
	for ($i=0; $i<$days; $i++)
	{
		$dayStamp = mktime(0, 0, 0, $month, $day+$i, $year);
		$ss = $manager->getDaysSessions($uId, $dayStamp);
		$sessions = DcUnitConverter::convertSessions($ss, $distUnit, $timeUnit);
	
		$content = "";
		$dayDist = 0;
		$numSessions = count($sessions);
		for ($j=0; $j<$numSessions; $j++)
		{
			$session = $sessions[$j];
			$ts = strtotime($session->startTime);
			$dateFrmtd = date("g:ia", $ts);
			$sTime = $session->startTime;
			$eTime = $session->endTime;
			$diff = getTimeDifference($sTime, $eTime);
			$totalTime['hours'] += $diff['hours'];
			$totalTime['minutes'] += $diff['minutes'];
			$totalTime['seconds'] += $diff['seconds'];
			$totalVel += $session->averageVelocity;
			$totalDist += $session->distance;
			$dayDist += $session->distance;
		}		
		if ($numSessions > 0)
		{
			$content .= "<div class=\"monthDayDist\">";
			//$content .= $dateFrmtd.": <a href=\"viewSession.php?s=";
			$content .= "<br/><a href=\"day.php?u=".$uId."&t=";
			$content .= $dayStamp."\">".number_format($dayDist,2)."</a> ".$distUnit." in ";
			$content .= $numSessions . " sessions. </div>";
				
			$cal->setEventContent($year, $month, $day+$i, $content);
		}
	}

	$totalTimeSecs = $totalTime['seconds'] + 60 * $totalTime['minutes'] + 3600 * $totalTime['hours'];
	$totalTime = sec2hms($totalTimeSecs);
	$averageVel = $totalVel / (count($sessions)*$days);
		
	$month = $cal->showMonth();

	$smarty->assign('user', $user);
	$smarty->assign('startDate', $startDate);
	$smarty->assign('distUnit', $distUnit);
	$smarty->assign('velUnit', $velUnit);
	$smarty->assign('endDate', $endDate);
	$smarty->assign('nextMonth', $nextMonth);
	$smarty->assign('prevMonth', $prevMonth);
	$smarty->assign('totalDist', $totalDist);
	$smarty->assign('totalTime', formatHms($totalTime),1);
	$smarty->assign('averageVel', $averageVel);
	$smarty->assign('month', $month);
	$smarty->display('month.tpl');
?>
