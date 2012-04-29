{include file='header.tpl' title='DCWeb - Login'}

<h1>Login</h1>
<form action="login_action.php" method="post" name="login">
<table>
	<tr>
		<td>
			Username:
		</td>
		<td>
			<input name="username" type="text" class="textInput" size="10" />
		</td>
	</tr>
	<tr>
		<td>
			Password:
		</td>
		<td>
			<input name="password" type="password" class="textInput" size="10" />
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<input name="submit" type="submit" value="Login" />
		</td>
	</tr>
</table>
</form>

<p class="error"> {$errorMsgs} </p>

<p>
<a href="registerUser.php">Register</a> new user.
</p>
{include file='footer.tpl'}