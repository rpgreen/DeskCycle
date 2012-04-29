<?
	require("include/main/DcWebManager.inc.php");
	require("include/main/DcLoginManager.inc.php");
	
	//$lm = new DcLoginManager();
	//$lm->checkLogin();

	//user logged in if we get to this point
	//$userId = $lm->getLoggedUserId();	
	
	$userName = $_POST["userName"];
	$password = $_POST["password"];
	$password2 = $_POST["password2"];
	$lastName = $_POST["lastName"];
	$firstName = $_POST["firstName"];
	$email = $_POST["email"];
	$age = $_POST["age"];
	$sex = $_POST["sex"];
	$height = $_POST["height"];
	$weight = $_POST["weight"];
	$displayName = $_POST["displayName"];
	$displayPicture = $_POST["displayPicture"];
	$description = $_POST["description"];
	
	if (! ($userName && $password && password2 && 
		lastName && firstName && email && age && sex && 
		height && weight && displayName /*&& displayPicture */
		&& description))
	{
		header("Location: registerUser.php?e=1");
	}
	else if ($password != $password2)
	{
		header("Location: registerUser.php?e=2");
	}
	else 
	{
		$user = new DcUser("", $userName, $lastName, $firstName, 
			$email, $age, $sex, $height, $weight, 
			$displayName, $displayPicture, $description,"", $password);
		
		//TODO: make sure user doesnt exist with that username or email
		$manager = new DcWebManager();
		$manager->addUser($user);
		
		/* log the user in */
		$id = $manager->getUserId($userName);
		$lm = new DcLoginManager();
		$lm->loginUser($id);
		
		header("Location: profile.php");		
	}


?>