{include file='header.tpl' title='DCWeb - Weekly Session View'}

<h1>Weekly Session View</h1>
<h2>{$user->lastName}, {$user->firstName} </h2>

<h3>Week of {$startDate|date_format:"%B %e, %Y"} - {$endDate|date_format:"%B %e, %Y"}</h3>

<div class = "weekview">
{$week}
<span class = "prev"><a href="week.php?u={$uId}&t={$prevWeek}">Previous Week</a></span>
<span class = "divider"> | </span>
<span class = "next"><a href="week.php?u={$uId}&t={$nextWeek}">Next Week</a></span>
</div>
<div class = "weekselector">

</div>

<br/>

<table class="summary">
  <tr>
    <th colspan="2">This Week</th>
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
<tr>
	<td>
		Average Velocity:
	</td>
	<td>
		{$averageVel|string_format:"%.2f"} {$velUnit}
	</td>
</tr>
</table>

{include file='footer.tpl'}