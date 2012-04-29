<?
	include("include/xmlrpc/xmlrpc.inc");
	include("include/xmlrpc/xmlrpcs.inc");
	include("include/xmlrpc/xmlrpc_wrappers.inc");
	include("include/dcdb/Dcdb.inc.php");
	
	$s = new xmlrpc_server(
	array(
	  "DCWeb.getUserId" => array("function" => "getUserId"),
	  "DCWeb.uploadSession" => array("function" => "uploadSession"),
  	  "DCWeb.validateDeviceKey" => array("function" => "validateDeviceKey"),
 	  "DCWeb.validateUser" => array("function" => "validateUser"),
	));


	function getUserId ($xmlrpcmsg) 	//verified
	{
		$dcdb = new Dcdb;
		$p1 = $xmlrpcmsg->getParam(0);
		$userName = $p1->scalarval();
		
		$q = "SELECT `userId` FROM `dcdb_users` WHERE `userName` = '".$userName."'";
		$rslt = $dcdb->query($q);	
		$row = mysql_fetch_row($rslt);
		$userId = $row[0];
		
		return new xmlrpcresp(new xmlrpcval($userId, "int"));
	}
	
	function uploadSession ($xmlrpcmsg)
	 {	
		$dcdb = new Dcdb;
		$return = true;
		
		/*	Get the data */
	    $p1 = $xmlrpcmsg->getParam(0);
	    $p2 = $xmlrpcmsg->getParam(1);
	    $p3 = $xmlrpcmsg->getParam(2);
	    $p4 = $xmlrpcmsg->getParam(3);		
	    $p5 = $xmlrpcmsg->getParam(4);
	    $p6 = $xmlrpcmsg->getParam(5);
	    $p7 = $xmlrpcmsg->getParam(6);
   	    $p8 = $xmlrpcmsg->getParam(7);
		
		$userId = $p1->scalarval();
		$deviceKey = $p2->scalarval();
		$startTime8601 = $p3->scalarval();
		$endTime8601 = $p4->scalarval();
		$pointInterval = $p5->scalarval();
		$totalDist = $p6->scalarval();
		$averageVel = $p7->scalarval();
		$startTimestamp = iso8601_decode($startTime8601);
		$endTimestamp = iso8601_decode($endTime8601);
		$startDatetime = date("Y-m-d H:i:s", $startTimestamp);
		$endDatetime = date("Y-m-d H:i:s", $endTimestamp);
		
		$velPoints = array();
		for ($i = 0; $i < $p8->arraysize(); $i++)
		{
			$val = $p8->arraymem($i);
			$pt = $val->scalarval();
			array_push($velPoints, $pt);
		}
		
		//need to get the device id from the key
		$q = "SELECT `deviceTypeId` from `dcdb_devices` WHERE `key` = '".$deviceKey."'";
		$rslt = $dcdb->query($q);
		$row = mysql_fetch_row($rslt);
		$deviceId = $row[0];
		
		if (mysql_error()){
			$return = false;
		}
		
		/* Add session to db */
		$q = "INSERT INTO `dcdb_sessions` (userId, deviceId, startTime, endTime, "
			."pointResolution, distance, averageVelocity) VALUES('".$userId."','".$deviceId."','"
			.$startDatetime."','".$endDatetime."','".$pointInterval."','".$totalDist."','".$averageVel."')";
		$dcdb->query($q);
		$sessionId = mysql_insert_id();
		
		if (mysql_error()){
			$return = false;
		}
		
		/* Add velocity points to db */
		for ($i=0; $i < count($velPoints); $i++)
		{
			$q = "INSERT INTO `dcdb_sessionVelocityData` (sessionId, pointIndex, velocityPoint) "
				."VALUES('".$sessionId."','".$i."','".$velPoints[$i]."')";
			$dcdb->query($q);
		}
		
		if (mysql_error()){
			$return = false;
		}
						
		return new xmlrpcresp(new xmlrpcval($return, "boolean"));
	}
	
	function validateDeviceKey ($xmlrpcmsg) 	//verified
	{
		$dcdb = new Dcdb;
		
		$p1 = $xmlrpcmsg->getParam(0);
		$key = $p1->scalarval();	
		$return = false;
		
		$q = "SELECT `deviceId` FROM `dcdb_devices` WHERE `key` = '".$key."'";
		$rslt = $dcdb->query($q);	
		if (mysql_num_rows($rslt) > 0)
		{
			$return = true;
		}
				
		return new xmlrpcresp(new xmlrpcval($return, "boolean"));
	}
	
	function validateUser ($xmlrpcmsg) //verified
	{	
		$dcdb = new Dcdb;
		$p1 = $xmlrpcmsg->getParam(0);
		$p2 = $xmlrpcmsg->getParam(1);
		
		$userName = $p1->scalarval();
		$pass = $p2->scalarval();
		$passHash = sha1($pass);
		$return = false;
		
		$q = "SELECT * FROM `dcdb_users` WHERE `username` = '".$userName."' AND `pass` = '".$passHash."'";

		$rslt = $dcdb->query($q);	
		if (mysql_num_rows($rslt) > 0)
		{
			$return = true;
		}
			
		return new xmlrpcresp(new xmlrpcval($return, "boolean"));
	}
		
	
?>