<?
	include("../dcdb/Dcdb.inc.php");

	class DcUser 
	{
		var $userId;
		var $userName;
		var $lastName;
		var $firstName;
		var $email;
		var $age;
		var $sex;
		var $height;
		var $weight;
		var $displayName;
		var $displayPicture;
		var $description;
		var $inSession;
		var $pass;
		
		function DcUser($pUserId, $pUserName, $pLastName, $pFirstName,
						$pEmail, $pAge, $pSex, $pHeight, $pWeight, $pDisplayName, 
						$pDisplayPicture, $pDescription, $pInSession, $pPass="")
		{
			$this->userId = $pUserId;
			$this->userName = $pUserName;
			$this->lastName = $pLastName;
			$this->firstName = $pFirstName;
			$this->email = $pEmail;
			$this->age = $pAge;
			$this->sex = $pSex;
			$this->height = $pHeight;
			$this->weight = $pWeight;
			$this->displayName = $pDisplayName;
			
			//default image
			$picDir = "images/userImages/";
			if (!$pDisplayPicture || !file_exists($picDir.$pDisplayPicture))
			{
				$picPath = "images/defaultUser.gif";
			}
			else 
			{
				$picPath = "images/userImages/".$pDisplayPicture;
			}
			$this->displayPicture = $picPath;
			
			$this->description = $pDescription;
			$this->inSession = $pInSession;
			$this->pass = $pPass;
		}
	
	}
	
	
?>