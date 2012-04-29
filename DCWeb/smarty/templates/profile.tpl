{include file='header.tpl' title='DCWeb - User Profile'}

<h1>User Profile</h1>

<table class="profile">
	<tr>
		<td rowspan="6"  style="padding-right:20px;">
		<img src="{$user->displayPicture}" width="200" height="150" style="border:solid 1px;">
		</td>
	
		<td colspan="2">
		<span style="font-size:16px; font-weight:bold"> {$user->displayName}</span>
		</td>

	</tr>
	
	<tr>
		<td valign="top"><strong>Name:</strong>
		</td>
		<td valign="top">{$user->firstName} {$user->lastName}
		</td>
	</tr>
	
	<tr>

		<td valign="top"><strong>Age:</strong>
		</td>
		<td valign="top">{$user->age}
		</td>
	</tr>
	
	<tr>

		<td valign="top"><strong>Sex:</strong>
		</td>
		<td valign="top">{$user->sex}
		</td>
	</tr>
	
	<tr>

		<td valign="top"><strong>Height:</strong>
		</td>
		<td valign="top">{$user->height} cm
		</td>
	</tr>
	
	<tr>

		<td valign="top"><strong>Weight:</strong>
		</td>
		<td valign="top">{$user->weight} kg
		</td>
	</tr>
</table>

<p class="userDescription">
{$user->description}
</p>



<table class="summary">
  <tr>
    <th colspan="2">User Stats</th>
  </tr>
  <tr>
	<td>
		Started:
	</td>
	<td>
		{$firstTime|date_format:"%B %e, %Y"}
	</td>
</tr>
<tr>
	<td>
		Total Distance:
	</td>
	<td>
		{$totalDist|string_format:"%.2f"} {$distUnit}
	</td>
</tr>
<tr>
	<td>
		Total Time:
	</td>
	<td>
		{$totalTime}
	</td>
</tr>
</table>



{include file='footer.tpl'}

