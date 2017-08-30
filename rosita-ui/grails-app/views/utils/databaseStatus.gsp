<!DOCTYPE HTML5>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="layout_main" />
</head>
</html>
<body>

	<g:if test='${flash.message}'><div class='flashmessage'>${flash.message}</div></g:if>
	
	<table class="striped">
			<thead>
				<tr>
					<th width="24">&nbsp;</th>
					<th>PID</th>
					<th>Database Name</th>
					<th>Start Time</th>
					<th>Waiting</th>
					<th>State</th>
					<th width="24">&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${pgsList}" var="line" status="i">
					<tr>
						<td></td>
						<td>${line.pid}</td>
						<td>${line.datname}</td>
						<td><g:formatDate format="yyyy-MM-dd hh:mm:ss" date="${line.queryStart}"/></td>
						<td><tmpl:/ui/checkmark test="${line.waiting}"/></td>
						<td>${line.state}</td>
						<td>
							<a href="${createLink([action:'terminate',controller:'utils',id:line.pid])}" onclick="return confirm('You should only terminate a process if you\'re certain that it has hung and is blocking the system. Are you sure you want to terminate this process?')">
								<img src="${resource(dir:'images/icons',file:'database_cancel.png')}" width="16" height="16"/>
							</a>
						</td>
					</tr>
					<tr>	
						<td colspan="7"><div class="texttiny">
							${line.query}
						</td>
					</tr>
				</g:each>
			</tbody>
	</table>

	<div class="bottombar">
		<div class="bigbutton">
			<a href="${createLink([action:'about', controller:'utils', id: stepId])}"><div style="cursor: pointer;" class="stepicon back">Back</div></a>
		</div>
	</div>

</body>