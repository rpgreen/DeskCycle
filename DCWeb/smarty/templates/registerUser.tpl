{include file='header.tpl' title='DCWeb - Register User'}

<h1>Register User</h1>

<form action="registerUser_action.php" method="post" name="registerUser">
<table class="form">
	<tr>
		<td>
			Username:
		</td>
		<td>
			<input name="userName" type="text" class="textInput" size="20" />
		</td>
	</tr>
	<tr>
		<td>
			Password:
		</td>
		<td>
			<input name="password" type="password" class="textInput" size="20" />
		</td>
	</tr>
	<tr>
		<td>
			Repeat Password:
		</td>
		<td>
			<input name="password2" type="password" class="textInput" size="20" />
		</td>
	</tr>
	<tr>
		<td>
			First Name:
		</td>
		<td>
			<input name="firstName" type="text" class="textInput" size="20" />
		</td>
	</tr>	
	<tr>
		<td>
			Last Name:
		</td>
		<td>
			<input name="lastName" type="text" class="textInput" size="20" />
		</td>
	</tr>	
	<tr>
		<td>
			Email:
		</td>
		<td>
			<input name="email" type="text" class="textInput" size="20" />
		</td>
	</tr>	
	<tr>
		<td>
			Age:
		</td>
		<td>
			<input name="age" type="text" class="textInput" size="20" />
		</td>
	</tr>	
	<tr>
		<td>
			Sex:
		</td>
		<td>
			<input name="sex" type="text" class="textInput" size="20" />
		</td>
	</tr>	
	<tr>
		<td>
			Height (cm):
		</td>
		<td>
			<input name="height" type="text" class="textInput" size="20" />
		</td>
	</tr>	
	<tr>
		<td>
			Weight (kg):
		</td>
		<td>
			<input name="weight" type="text" class="textInput" size="20" />
		</td>
	</tr>		
	<tr>
		<td>
			Display Name:
		</td>
		<td>
			<input name="displayName" type="text" class="textInput" size="20" />
		</td>
	</tr>	
	<tr>
		<td>
			Display Picture:
		</td>
		<td>
			<input name="displayPicture" type="text" class="textInput" size="20" />
		</td>
	</tr>	
	<tr>
		<td>
			Description:
		</td>
		<td>
			<input name="description" type="text" class="textInput" size="20" />
		</td>
	</tr>	
	
	
	
	<tr>
		<td colspan="2">
			<input name="submit" type="submit" value="Register" />
		</td>
	</tr>
</table>
</form>

<p class="error">
{$errorMsgs}
</p>

{include file='footer.tpl'}