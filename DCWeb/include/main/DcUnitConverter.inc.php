<?
	class DcUnitConverter
	{
		
		function &convertSession($session, $distUnit, $timeUnit)
		{
			$distFactor = 1;
			$timeFactor = 1;
			
			//TODO: more unit support
			switch ($distUnit)
			{
				case "km":	//cm to km
				$distFactor = 0.00001;
				break;
			}
			switch ($timeUnit)
			{
				case "h":
				$timeFactor = 1./3600000;	//ms to h
				break;
			}
			$velFactor = $distFactor / $timeFactor;
			
			//distance, average velocity, velPoints
			$session->distance = $session->distance * $distFactor;
			$session->averageVelocity = $session->averageVelocity * $velFactor;

			$vPoints = $session->velPoints;
			for ($i=0; $i<count($vPoints); $i++)
			{
				$vPoints[$i] = $vPoints[$i] * $velFactor;
			}
			$session->velPoints = $vPoints;
			
			return $session;
		}
		
		function convertSessions($ss, $distUnit, $timeUnit)
		{
			$sessions = array();
			foreach ($ss as $sess)
			{
				$s = DcUnitConverter::convertSession($sess, $distUnit, $timeUnit);
				array_push($sessions, $s);
			}
			return $sessions;
		}
		
		function convertDistance($d, $distUnit)
		{
			$distFactor = 1;
			
			//TODO: more unit support
			switch ($distUnit)
			{
				case "km":	//cm to km
				$distFactor = 0.00001;
				break;
			}
			return $d * $distFactor;
		}
	
		function convertVelocity($v, $velUnit)
		{
		}
	}
	
	
?>
