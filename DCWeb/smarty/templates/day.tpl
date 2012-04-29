{include file='header.tpl' title='DCWeb - Daily Session View'}

<h1>Daily Session View</h1>
<h2>{$lastName}, {$firstName} </h2>

<h3>{$date|date_format:"%A %B %e, %Y"}</h3>

{if count($sessions) eq 0}
<p>There are no sessions for today. </p>
{else}


<table class="summary">
  <tr>
    <th>Start Time</th>
	<th>Duration</th>
    <th>Distance</th>
    <th>Average Velocity</th>
  	<th>Details</th>
  </tr>
{section name=session loop=$sessions}
<tr>

	<td>
		{$sessions[session]->startTime|date_format:"%I:%M %p"}
	</td>

	<td>
		{$sessions[session]->elapsed}
	</td>
	
	<td>
		{$sessions[session]->distance|string_format:"%.2f"} {$distUnit}
	</td>
	
	<td>
		{$sessions[session]->averageVelocity|string_format:"%.2f"} {$velUnit}
	</td>
	
	<td>
		<a href="viewSession.php?u={$uId}&s={$sessions[session]->sessionId}">View</a>
	</td>
	
</tr>
{/section}
</table>

<br/>

<table class="summary">
  <tr>
    <th colspan="2">This Day</th>
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

{/if}


{include file='footer.tpl'}