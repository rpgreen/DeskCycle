<?
	// This class can only be included from root (script) directory.
	require_once("include/dcdb/Dcdb.inc.php");
	
	class DcFriendManager 
	{
		var $db;
		
		function DcFriendManager()
		{
			$this->db = &new Dcdb();
		}
	
		function getFriends($userId)
		{
			//2 queries
			$q = "SELECT `user1Id` FROM `dcdb_friends` WHERE `user2Id` = '".$userId."'";
			$rslt = $this->db->query($q);	
			$r = array();
			while ($row = mysql_fetch_array($rslt))
			{
				if (!in_array($row['user1Id'], $r))
				{
					array_push($r, $row['user1Id']);
				}
			}
			
			$q = "SELECT `user2Id` FROM `dcdb_friends` WHERE `user1Id` = '".$userId."'";
			$rslt = $this->db->query($q);	
			while ($row = mysql_fetch_array($rslt))
			{
				if (!in_array($row['user12d'], $r))
				{
					array_push($r, $row['user2Id']);
				}
			}
			
			return $r;		
		}
		
		
		
		function addFriendRequest($inviterId, $inviteeId)
		{
			$q = "INSERT INTO `dcdb_friendRequests` (inviterUserId, inviteeUserId) VALUES (";
			$q .= "'".$inviterId."', '".$inviteeId."')";
			$this->db->query($q);	
		}
		
		function getFriendRequests($userId)	//array of user Id's
		{
			$q = "SELECT `inviterUserId` FROM `dcdb_friendRequests` WHERE `inviteeUserId` = '".$userId."'";
			$rslt = $this->db->query($q);	
			
			$r = array();
			while ($row = mysql_fetch_array($rslt))
			{
				if (!in_array($row['inviterUserId'], $r))
				{
					array_push($r, $row['inviterUserId']);
				}
			}
			return $r;
		}
		
		function isFriend($fId1, $fId2)
		{
			if ($fId1 == $fId2) return true;	//friends with yourself
			
			$q = "SELECT `friendId` FROM `dcdb_friends` WHERE `user1Id` = '".$fId1."' AND `user2Id` = '".$fId2."'";
			$rslt = $this->db->query($q);	
			$c = mysql_num_rows($rslt);

			$q = "SELECT `friendId` FROM `dcdb_friends` WHERE `user1Id` = '".$fId2."' AND `user2Id` = '".$fId1."'";
			$rslt = $this->db->query($q);	
			$c += mysql_num_rows($rslt);

			if ($c > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
				
		function authorizeFriendRequest($userId, $friendId)
		{
			// delete friend request
			$q = "DELETE FROM `dcdb_friendRequests` WHERE `inviteeUserId` = '".$userId."' AND `inviterUserId` = '".$friendId."'";
			$this->db->query($q);	
			
			//add to friends table
			$q = "INSERT INTO `dcdb_friends` (user1Id, user2Id, level) VALUES ('".$userId."','".$friendId."','1')";
			$this->db->query($q);	
		}
		
		function declineFriendRequest($userId, $friendId)
		{
			// delete friend request
			$q = "DELETE FROM `dcdb_friendRequests` WHERE `inviteeUserId` = '".$userId."' AND `inviterUserId` = '".$friendId."'";
			$this->db->query($q);	
		}
		
		
	}
?>