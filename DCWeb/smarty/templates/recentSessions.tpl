{include file='header.tpl' title='DCWeb - Session View'}

<h1>Session View</h1>
<h2>{$user->lastName}, {$user->firstName} </h2>

<h3>Recent Sessions</h3>

<table class="recentSessions">
<tr>
{$cells1}
</tr>

<tr>
{section name=session loop=$sessions}
<td class="recentSessionsText">
{$sessions[session]->startTime|date_format:"%B %e"}
</td>
{/section}
</tr>

<tr>
{section name=session loop=$sessions}
<td class="recentSessionsText">
{$sessions[session]->distance|string_format:"%.2f"} {$distUnit}
</td>
{/section}
</tr>

<tr>
{section name=session loop=$sessions}
<td class="recentSessionsText">
{$sessions[session]->elapsed}
</td>
{/section}
</tr>
</table>

{include file='footer.tpl'}