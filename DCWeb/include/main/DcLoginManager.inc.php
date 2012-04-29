<?
	// This class can only be included from root (script) directory.
	require_once("include/dcdb/Dcdb.inc.php");
	
	class DcLoginManager 
	{
		var $db;
		
		function DcLoginManager()
		{
			$this->db = &new Dcdb();
			session_start();
		}
		
		
		function validateUser ($userName, $pass) //verified
		{	
			$dcdb = new Dcdb;

			$passHash = sha1($pass);
			$return = false;
			
			$q = "SELECT * FROM `dcdb_users` WHERE `username` = '".$userName."' AND `pass` = '".$passHash."'";
	
			$rslt = $dcdb->query($q);	
			if (mysql_num_rows($rslt) > 0)
			{
				$return = true;
			}
				
			return $return;
		}
		
		function checkLogin()
		{
			if (!$_SESSION['userId'])
			{	
				//header("Location: login.php");	//header wont work cuz of session_start
				echo "<meta HTTP-EQUIV=\"REFRESH\" content=\"0; url=login.php\">";	//this stinks
				die();
			}
		}
		
		function getLoggedUserId()
		{
			return $_SESSION['userId'];
		}	
		
		function loginUser($userId)
		{
			$_SESSION['userId'] = $userId;
		}
		
		/* //TODO
		function getFriends($userId)
		{
		}*/

	
	}

	
?>