<!DOCTYPE HTML5>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="layout_main" />
</head>
</html>
<body>

	<h3>Workflow steps for <i>${rositaJobInstance.jobName}</i></h3>
	<table class="striped">
		<thead>
			<tr>
				<th>Step</th>
				<th>Started on</th>
				<th>Finished on</th>
				<th>Result</th>
				<th>&nbsp;</th>
			</tr>
		</thead>
	<g:each in="${steps}" var="wfStep" status="i">
			<tr>
				<td>${workflowTitles[wfStep.stepDescription.stepNumber-1]?.title}</td>
				<td><g:formatDate format="yyyy-MM-dd HH:mm:ss" date="${wfStep.startDate}"/></td>
				<td><g:formatDate format="yyyy-MM-dd HH:mm:ss" date="${wfStep.endDate}"/></td>
				<td>
					<g:if test="${wfStep.state == 'success' || wfStep.state == 'completed'}">
						<img src="${resource(dir:'images/icons',file:'check.png')}" width="16" height="16"/>
					</g:if>
					<g:elseif test="${wfStep.state == 'failed'}">
						<img src="${resource(dir:'images/icons',file:'close_delete_2.png')}" width="16" height="16"/>
					</g:elseif>
					<g:elseif test="${wfStep.state == 'skipped'}">
						<img src="${resource(dir:'images/icons',file:'skipped.png')}" width="16" height="16"/>
					</g:elseif>
					<g:elseif test="${wfStep.state == 'successwitherrors'}">
						<img src="${resource(dir:'images/icons',file:'checkwitherror.png')}" width="16" height="16"/>
					</g:elseif>
					<g:elseif test="${wfStep.state == 'paused'}">
						<img src="${resource(dir:'images/icons',file:'pause.png')}" width="16" height="16"/>
					</g:elseif>
					<g:elseif test="${wfStep.state == 'running'}">
						<img src="${resource(dir:'images/icons',file:'ajax-loader.gif')}" width="16" height="16"/>
					</g:elseif>
					<g:else>
						<img src="${resource(dir:'images/icons',file:'dead.png')}" width="16" height="16"/>
					</g:else>
				</td>
				<td>
					<tmpl:/rositaJob/consoleLink id="${wfStep.id}" errorsExist="${wfStep.hasStepErrors}" warningsExist="${wfStep.hasStepWarnings}"/>
				</td>
			</tr>
	</g:each>
	</table>
	
	<div class="bottombar">
		<div class="bigbutton">
			<a href="${createLink([action:'index', controller:'rositaJob'])}"><div style="cursor: pointer;" class="stepicon back">Job list</div></a>
		</div>
	</div>

</body>