<?
	include("../dcdb/Dcdb.inc.php");
	include("../main/helper.inc.php");
	
	class DcSession 
	{
		var $sessionId;
		var $userId;
		var $deviceId;
		var $startTime;
		var $endTime;
		var $pointResolution;
		var $distance;
		var $averageVelocity;
		var $velPoints;
		var $elapsed;
		
		function DcSession($pSessionId, $pUserId, $pDeviceId, $pStartTime, 
						   $pEndTime, $pPointResolution, $pDistance, 
						   $pAverageVelocity, $pVelPoints)
		{
			$this->sessionId = $pSessionId;
			$this->userId = $pUserId;
			$this->deviceId = $pDeviceId;
			$this->startTime = $pStartTime;
			$this->endTime = $pEndTime;
			$this->pointResolution = $pPointResolution;			
			$this->distance = $pDistance;
			$this->averageVelocity = $pAverageVelocity;
			$this->velPoints = $pVelPoints;	
			$diff = getTimeDifference($pStartTime, $pEndTime);
			$this->elapsed = formatHms($diff);
		}
	
	}
	
	
?>