<!DOCTYPE HTML5>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="layout_main" />
</head>
</html>
<body>

	<g:set var="page" value="jobs" scope="request" />
	<h3>Job History</h3>

	<g:paginate next="Earlier" prev="Later" action="jobs"
		total="${jobCount}" max="${max}" />
	<br />
	<br />
	<table class="striped">
		<thead>
			<tr>
				<th>Name</th>
				<th>Started on</th>
				<th>Finished on</th>
				<th>Outcome</th>
				<th>&nbsp;</th>
			</tr>
		</thead>
		<g:each in="${jobs}" var="job" status="i">
			<tr>
				<td>
					${job.jobName}
				</td>
				<td><g:formatDate format="yyyy-MM-dd" date="${job.startDate}" /></td>
				<td><g:formatDate format="yyyy-MM-dd" date="${job.endDate}" /></td>
				<td style="line-height: 16px">
					<g:if test="${workflowTitles[job.workflowStepNumber-1]?.title == 'Complete'}">
						<a href="${createLink([action:'history',controller:'rositaJob',id:job.id])}">
							<img src="${resource(dir:'images/icons',file:'check.png') }" width="16" height="16" /> Complete
						</a>
					</g:if>
					<g:elseif test="${jobInProgress && i == 0 && offset == 0}">
						<a href="${createLink([action:'show',controller:'rositaJob',id:job.id])}">
							<img src="${resource(dir:'images/icons',file:'play.png') }" width="16" height="16" /> Current
						</a>
					</g:elseif>
					<g:else>
						<a href="${createLink([action:'history',controller:'rositaJob',id:job.id])}">
							<img src="${resource(dir:'images/icons',file:'close_delete_2.png') }" width="16" height="16" /> Cancelled
						</a>
					</g:else>
				</td>
				<td>
					<g:if test="${jobInProgress && i == 0 && offset == 0}">
						<a href="${createLink([action:'show',controller:'rositaJob',id:job.id])}">
							<img src="${resource(dir:'images/icons',file:'detail.png')}" width="16" height="16" />
						</a>
					</g:if>
					<g:else>
						<a href="${createLink([action:'history',controller:'rositaJob',id:job.id])}">
							<img src="${resource(dir:'images/icons',file:'detail.png')}" width="16" height="16" />
						</a>
					</g:else>
				</td>
			</tr>
		</g:each>
	</table>
	<br />
	<g:paginate next="Earlier" prev="Later" action="jobs"
		total="${jobCount}" max="${max}" />

	<div class="bottombar">
		<div class="bigbutton">
			<a href="${createLink([action:'index', controller:'rositaJob'])}"><div
					style="cursor: pointer;" class="stepicon back">Job list</div></a>
		</div>
	</div>

</body>