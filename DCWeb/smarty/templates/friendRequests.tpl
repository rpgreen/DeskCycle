{include file='header.tpl' title='DCWeb - Friend Requests'}

<h1>Friend Requests</h1>

<table class="summary">

{section name=user loop=$users}

<tr>
	<td>
		<img src="{$users[user]->displayPicture}" width="85" style="border:solid 1px;">
	</td>
	<td>
		{$users[user]->firstName} {$users[user]->lastName}  
	</td>
	<td>
		<a href="authorizeFriend.php?u={$users[user]->userId}&a=1">Authorize</a> / <a href="authorizeFriend.php?u={$users[user]->userId}&a=-1">Decline</a>
	</td>
</tr>

{/section}

</table>

{include file='footer.tpl'}