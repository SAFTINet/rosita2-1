<b>The process for this step appears to have stopped without a reply.</b>

<br/><br/>

Check the <g:link class="anchor" controller="rositaLog" action="list" id="${stepId}">message log</g:link> to see any potential problems, and verify whether the step completed or not.

<br/><br/>

	Additionally:
	<ul>
		<li>Check the database to see if a process is blocked</li>
		<li>Use the <g:link class="anchor" controller="utils" action="about">system admin page</g:link> to verify that the ROSITA library can access the database</li>
	</ul>

<div class="actiontitle">Actions</div>

<table class="actiontable">
<tr><td>
	<img src="${resource(dir:'images/icons', file:'check.png')}" height="16" width="16"/>
</td>
<td><span class="anchor" onclick="confirmStep(JOB_ID, ${workflowStep}, ${stepId})">Mark this step as completed</span></td></tr>
<tr><td>
		<img src="${resource(dir:'images/icons', file:'refresh.png')}" height="16" width="16"/>
	</td>
<td><span class='anchor' onclick='rerunStep(${jobId}, ${stepId})'>Retry</span> this step</td></tr>
</table>
