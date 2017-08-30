<!DOCTYPE HTML5>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="layout_main" />
</head>
</html>
<body>
	<g:set var="page" value="job" scope="request"/>
	<h3>${rositaJobInstance.jobName}</h3>
	
	<table class="striped" width="40%">
		<thead>
			<tr>
				<th>Data sources</th>
			</tr>
		</thead>
		<tbody>
			<g:each in="${dataSources}" var="dataSource" status="j">
			<tr>
				<td><img src="${resource(dir:'images/icons',file:'folder_16.png')}"/> <b>${dataSource.dataSourceName}</b> (${dataSource.dataSourceDirectory})</td>
			</tr>
			</g:each>
		</tbody>
	</table>
	
	<div id="workflowSteps">
		<g:each in="${workflowSteps}" var="step" status="i">
			<g:if test="${step}">
				<div class="stepstatus" id="stepstatus${step.id}">
					<%-- If the step has been created (has been successful or this is the current step) --%>
					<div id="stepicon${step.id}" class="stepicon ${step.state}">${workflowTitles[step.stepDescription.stepNumber-1]?.title}</div>
					<div id="stepmessage${step.id}" class="stepmessage">
						<g:if test="${i == workflowSteps.size() - 1}">
							<img src="${resource([file: 'ajax-loader-flat.gif', dir: 'images/icons'])}"/>
						</g:if>
					</div>
					<div id="stepconsole${step.id}" class="stepconsole">
						<tmpl:consoleLink id="${step.id}"/>
					</div>
				</div>
			</g:if>
		</g:each>
	</div>
	
	<div class="bottombar">
	
	<div class="bigbutton">
		<a href="${createLink([action:'index', controller:'rositaJob'])}"><div style="cursor: pointer;" class="stepicon back">Job list</div></a>
	</div>
	
	<div class="bigbutton" id="cancelButton">
		<div id="stepiconcancel" style="cursor: pointer;" class="stepicon failed" onclick="if (confirm('Are you sure you want to cancel this job?')) {cancelJob(${rositaJobInstance.id});}">Cancel job</div>
	</div>
	
	</div>
	
	<g:javascript>
		
		var workflowTitles = [];
		var workflowNext = [];
		<g:each in="${workflowTitles}" var="wt" status="i">
		workflowTitles[${i+1}] = "${wt.title}";
		workflowNext[${i+1}] = "${wt.nextStep}";
		</g:each>
		
		$j = jQuery.noConflict();	
		setInterval(barScroll, 100);
		JOB_ID = ${rositaJobInstance.id};
		startUpdating();
	</g:javascript>
</body>