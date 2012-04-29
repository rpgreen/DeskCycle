{include file='header.tpl' title='DCWeb - Session View'}

<h1>Session View</h1>
<h2>{$lastName}, {$firstName} </h2>
{*
<h3>{$date|date_format:"%A %B %e, %Y"}</h3>
*}
<table cellpadding="4">
<tr>
	<td valign="top">
		<table cellpadding="2">
			<tr>
				<td valign="top"><strong>Device:</strong>
				</td>
				<td>{$deviceName}
				</td>
			</tr>
			<tr>
				<td valign="top"><strong>Start Time</strong>
				</td>
				<td>{$startTime|date_format:"%B %e, %Y %I:%M %p"}
				</td>
			</tr>
			<tr>
				<td valign="top"><strong>End Time:</strong>
				</td>
				<td>{$endTime|date_format:"%B %e, %Y %I:%M %p"}
				</td>
			</tr>
			<tr>
				<td valign="top"><strong>Duration:</strong>
				</td>
				<td>{$duration}
				</td>
			</tr>
			<tr>
				<td valign="top"><strong>Total Distance:</strong>
				</td>
				<td>{$totalDist|string_format:"%.2f"} {$distUnit}
				</td>
			</tr>
			<tr>
				<td valign="top"><strong>Average Velocity:</strong>
				</td>
				<td>
					{$averageVel|string_format:"%.2f"} {$velUnit}
				</td>
			</tr>
		</table>
	</td>
	
	<td>
	<img src="sessionGraph.php?s={$sessId}"/>
	</td>
</tr>
</table>






{include file='footer.tpl'}
	
	
	
	
	

