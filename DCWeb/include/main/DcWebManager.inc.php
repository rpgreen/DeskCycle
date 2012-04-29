<?
	// This class can only be included from root (script) directory.
	require_once("include/dcdb/Dcdb.inc.php");
	require_once("include/main/DcUser.inc.php");
	require_once("include/main/DcSession.inc.php");
	require_once("include/main/DcDevice.inc.php");
	require_once("include/main/helper.inc.php");
	
	class DcWebManager 
	{
		var $db;
		
		function DcWebManager()
		{
			$this->db = &new Dcdb();
		}
		
		function &loadUser($userId)
		{
			$q = "SELECT * FROM `dcdb_users` where `userId` = '".$userId."'";
			$rslt = $this->db->query($q);
			$row = mysql_fetch_assoc($rslt);
			$user = &new DcUser($row["userId"], 
								$row["userName"],
								$row["lastName"],
								$row["firstName"],
								$row["email"],
								$row["age"],
								$row["sex"],
								$row["height"],
								$row["weight"],
								$row["displayName"],
								$row["displayPicture"],
								$row["description"],
								$row["inSession"]);
			return $user;
		}
		
		function getUserId($userName)
		{
			$q = "SELECT `userId` from `dcdb_users` where `userName` = '".$userName."'";
			$rslt = $this->db->query($q);
			$row = mysql_fetch_assoc($rslt);
			return $row["userId"];
		}
		
		function saveUser(&$user)
		{	
			//ensure userId didnt change, by using update
			$q = "UPDATE `dcdb_users`  SET ";
			$q .= "`userName` = '".$user->userName."', ";
			$q .= "`lastName` = '".$user->lastName."', ";
			$q .= "`firstName` = '".$user->firstName."', ";
			$q .= "`email` = '".$user->email."', ";
			$q .= "`age` = '".$user->age."', ";
			$q .= "`sex` = '".$user->sex."', ";
			$q .= "`height` = '".$user->height."', ";
			$q .= "`weight` = '".$user->weight."', ";
			$q .= "`displayName` = '".$user->displayName."', ";
			$q .= "`displayPicture` = '".$user->displayPicture."', ";
			$q .= "`description` = '".$user->description."', ";
			$q .= "`inSession` = '".$user->inSession."' ";		
			$q .= " WHERE `userId` = '".$user->userId."'";
			
  		   $this->db->query($q);
		}
		
		function addUser(&$user)
		{
			$q = "INSERT INTO `dcdb_users` (userName, pass, lastName, firstName, email, ";
			$q .= "age, sex, height, weight, displayName, displayPicture, description, inSession ) VALUES (";
			$q .= "'".$user->userName."', ";
			$q .= "'".sha1($user->pass)."', ";
			$q .= "'".$user->lastName."', ";
			$q .= "'".$user->firstName."', ";
			$q .= "'".$user->email."', ";
			$q .= "'".$user->age."', ";
			$q .= "'".$user->sex."', ";
			$q .= "'".$user->height."', ";
			$q .= "'".$user->weight."', ";
			$q .= "'".$user->displayName."', ";
			$q .= "'".$user->displayPicture."', ";
			$q .= "'".$user->description."', ";
			$q .= "'".$user->inSession."') ";	
     		
			$this->db->query($q);	
		}
				
		function getSessions($userId, $startedAfter="", $startedBefore="")
		{
			$q = "SELECT * FROM `dcdb_sessions` WHERE `userId` = '".$userId."'";
			if ($startedAfter) 
			{
				$q .= " AND `startTime` >= '".$startedAfter."' ";  
			}
			if ($startedBefore)
			{
				$q .= " AND `startTime` < '".$startedBefore."' ";  
			}
			$q .= " ORDER BY `sessionId`";
			
			$rslt = $this->db->query($q);
			$sessions = array();
			while ($row = mysql_fetch_array($rslt))
			{
				$q2 = "SELECT `velocityPoint` FROM `dcdb_sessionVelocityData` ";
				$q2 .= "WHERE `sessionId` = '".$row["sessionId"]."' ORDER BY `sessionVelocityDataId`";
			
				$rslt2 = $this->db->query($q2);
				$vPoints = array(); 
								
				while ($row2 = mysql_fetch_array($rslt2))
				{ 
					$pt = $row2["velocityPoint"];
					array_push($vPoints, $pt);
				}
				
				$sess = &new DcSession($row["sessionId"], 
								$row["userId"],
								$row["deviceId"],
								$row["startTime"],
								$row["endTime"],
								$row["pointResolution"],
								$row["distance"],
								$row["averageVelocity"],
								$vPoints);
				array_push($sessions, $sess);
			}
			return $sessions;
		}
		
		function getDaysSessions($userId, $dayStamp)
		{
			//assuming startStamp has been set to midnight 0:0:0
			$start = date("Y-m-d H:i:s", $dayStamp);
			$endStamp = mktime(0, 0, 0, date("m", $dayStamp), date("d", $dayStamp)+1, date("y", $dayStamp));
			$end = date("Y-m-d H:i:s", $endStamp);
			$s = $this->getSessions($userId, $start, $end);
			//echo "Getting sessions between $start $end<br>";
			return $s;
		}
		
		function getMonthsSessions($userId, $monthStamp)
		{
			//assuming startStamp has been set to midnight 0:0:0
			$start = date("Y-m-d H:i:s", $monthStamp);
			$endStamp = mktime(0, 0, 0, date("m", $monthStamp)+1, date("d", $monthStamp), date("y", $monthStamp));
			$end = date("Y-m-d H:i:s", $endStamp);
			$s = $this->getSessions($userId, $start, $end);
			return $s;
		}
		
		function &getSession($sessId)
		{
			$q = "SELECT * FROM `dcdb_sessions` WHERE `sessionId` = '".$sessId."'"; 
			$rslt = $this->db->query($q);
			$row = mysql_fetch_assoc($rslt);
			//print_r($row);
			$q2 = "SELECT `velocityPoint` FROM `dcdb_sessionVelocityData` ";
			$q2 .= "WHERE `sessionId` = '".$sessId."' ORDER BY `sessionVelocityDataId`";
			$rslt2 = $this->db->query($q2);
			
			$vPoints = array(); 
								
			while ($row2 = mysql_fetch_array($rslt2))
			{ 
				$pt = $row2["velocityPoint"];
				array_push($vPoints, $pt);
			}
			$sess = &new DcSession($row["sessionId"], 
				$row["userId"],
				$row["deviceId"],
				$row["startTime"],
				$row["endTime"],
				$row["pointResolution"],
				$row["distance"],
				$row["averageVelocity"],
				$vPoints);
			return $sess;
		}
		
		function &getDevice($devId)
		{
			$q = "SELECT * FROM `dcdb_deviceTypes` WHERE `deviceTypeId` = '".$devId."'"; 
			$rslt = $this->db->query($q);
			$row = mysql_fetch_assoc($rslt);
			$d = new DcDevice($row["name"]);
			return $d;
		}
		
		function isSessionOwnedByUser($sId, $uId)
		{
			$return = false;
			$sess = $this->getSession($sId);
			if ($sess->userId == $uId)
			{
				$return = true;
			}
			return $return;
		}
		
		function getUserIdByEmail($email)
		{
			$q = "SELECT `userId` FROM `dcdb_users` WHERE `email` = '".$email."'";
			$rslt = $this->db->query($q);
			$row = mysql_fetch_assoc($rslt);
			return $row["userId"];
		}

		function getDistanceSince($userId, $startTime)
		{
			$q = "SELECT SUM(distance) FROM `dcdb_sessions` WHERE `userId` = '".$userId."' AND `startTime` >= '".$startTime."'";
			$rslt = $this->db->query($q);
			$row = mysql_fetch_row($rslt);
			return $row[0];
		}
		
		function getTotalTimeSince($userId, $startTime)
		{
			$q = "SELECT SUM(endTime - startTime) FROM `dcdb_sessions` WHERE `userId` = '".$userId."' ";
			$q .= "AND `startTime` >= '".$startTime."'";
			$rslt = $this->db->query($q);
			$row = mysql_fetch_row($rslt);
			return $row[0];	//time in seconds
		}
		
		function getFirstSession($userId)
		{
			$q = "SELECT `sessionId` FROM `dcdb_sessions` WHERE `userId` = '".$userId."' ORDER BY `startTime` LIMIT 1";
			//echo $q;
			$rslt = $this->db->query($q);
			$row = mysql_fetch_row($rslt);
			return $this->getSession($row[0]);
		}
		
		function getLastSessions($userId, $numSessions)
		{
			$q = "SELECT `sessionId` FROM `dcdb_sessions` WHERE `userId` = '".$userId."' ORDER BY `startTime` DESC LIMIT ".$numSessions;
			$rslt = $this->db->query($q);
			$ids = array();
			$i=0;
			while ($row = mysql_fetch_array($rslt))
			{
				$ids[$i++] = $row[0];
			}

			$ids = array_reverse($ids);
			$sessions = array();
			for ($i=0; $i<count($ids); $i++)
			{
				array_push($sessions, $this->getSession($ids[$i]));
			}
			return $sessions;
		}
		
		/* //TODO
		function getFriends($userId)
		{
		}*/

	
	}

	
?>