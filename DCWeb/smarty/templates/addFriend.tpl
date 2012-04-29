{include file='header.tpl' title='DCWeb - Add Friend'}

<h1>Add Friend</h1>
<form action="addFriend_action.php" method="post" name="addFriend">
<table>
	<tr>
		<td>
			Email:
		</td>
		<td>
			<input name="email" type="text" size="15"/>
		</td>
		<td>
			<input name="submit" type="submit" value="Add" />
		</td>
	</tr>
</table>
</form>

<p class="error"> {$errorMsgs} </p>

{include file='footer.tpl'}