{include file='header.tpl' title='DCWeb - Weekly Session View'}

<h1>Monthly Session View</h1>
<h2>{$user->lastName}, {$user->firstName} </h2>

<h3>{$startDate|date_format:"%B %Y"}</h3>

<div class = "monthview">
{$month}
</div>
{* 
<div class = "monthselector">
<span class = "prev" ><a href="month.php?t={$prevMonth}">Previous Month</a></span>
<span class = "next" ><a href="month.php?t={$nextMonth}">Next Month</a></span> 
</div>
*}
<br/>

<table class="summary">
  <tr>
    <th colspan="2">This Month</th>
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