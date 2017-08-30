<!DOCTYPE HTML5>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="layout_main" />
</head>
</html>
<body>
	
	<h3>Validation Errors</h3>
	<g:paginate next="Forward" prev="Back" action="index" id="${id}" total="${errorCount}" max="${max}" />
	<br/><br/>
	<table class="striped">
		<thead>
			<tr>
				<th>Clinic ID</th>
				<th>Clinic</th>
				<th>Type</th>
				<th>Object</th>
				<th>Line</th>
				<th>Date</th>
				<th>Message</th>
			</tr>
		</thead>
	<g:each in="${errors}" var="error" status="i">
		<tr>
			<td>${error.mcDataSource?.id}</td>
			<td>${error.mcDataSource?.dataSourceName}</td>
			<td>${error.errorType}</td>
			<td>${error.sourceType}</td>
			<td>${error.lineNumber}</td>
			<td><g:formatDate format="yyyy-MM-dd hh:mm:ss" date="${error.datetime}"/></td>
			<td>${error.message}</td>
		</tr>
	</g:each>
	</table>
	<br/>
	<g:paginate next="Forward" prev="Back" action="index" id="${id}" total="${errorCount}" max="${max}" />

	<div class="bigbutton" style="width: 300px">
		<a href="${createLink([action:'export', controller:'validationError', id: id])}"><div style="cursor: pointer;" class="stepicon save">Export errors</div></a>
	</div>
	<div class="bigbutton" style="width: 300px">
		<g:if test="${from.equals('show')}">
			<a href="${createLink([action:'show', controller:'rositaJob', id: jobId])}"><div style="cursor: pointer;" class="stepicon back">Back to job</div></a>
		</g:if>
		<g:elseif test="${from.equals('history')}">
			<a href="${createLink([action:'history', controller:'rositaJob', id: jobId])}"><div style="cursor: pointer;" class="stepicon back">Back to history</div></a>
		</g:elseif>
	</div>
</body>