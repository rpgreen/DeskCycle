{include file='header.tpl' title='DCWeb - Friends'}

<h1>Friends</h1>

<table class="friendsList">

{section name=user loop=$users}

<tr>
	<td>
		<a href="profile.php?u={$users[user]->userId}">
		<img src="{$users[user]->displayPicture}" width="100" height="75" border="0">
		</a>
	</td>
	<td>
		{$users[user]->firstName} {$users[user]->lastName}  
	</td>
	<td>
		<a href="recentSessions.php?u={$users[user]->userId}">Sessions</a>: <a href="month.php?u={$users[user]->userId}">Month</a> | 
		<a href="week.php?u={$users[user]->userId}">Week</a> | 
		<a href="day.php?u={$users[user]->userId}">Day</a>
	</td>
</tr>

{/section}

</table>

{include file='footer.tpl'}