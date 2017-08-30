<!DOCTYPE HTML5>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="layout_main" />
</head>
</html>
<body>
	<g:set var="page" value="home" scope="request"/>

	<g:if test='${flash.message}'><div class='flashmessage'>${flash.message}</div></g:if>
	
	<g:if test="${jobInProgress}">
		<div class="stepstatus" id="stepstatus">
			<g:if test="${jobs[0].workflowStepNumber == 0}">
				<div id="stepicon" class="stepicon play">Job awaiting start</div>
			</g:if>
			<g:else>
				<g:if test="${latestWorkflowStep.state == 'paused'}">
					<div id="stepicon" class="stepicon paused">${workflowTitles[jobs[0].workflowStepNumber - 1].title} awaiting decision</div>
				</g:if>
				<g:elseif test="${latestWorkflowStep.state == 'failed'}">
					<div id="stepicon" class="stepicon failed">${workflowTitles[jobs[0].workflowStepNumber - 1].title} has failed</div>
				</g:elseif>
				<%-- Treat running and success as still running --%>
				<g:else>
					<div id="stepicon" class="stepicon running">${workflowTitles[jobs[0].workflowStepNumber - 1].title} in progress</div>
				</g:else>
			</g:else>
			<div class="stepmessage"><b>${jobs[0].jobName}</b>
			<table class="actiontable">
			<tr><td>
				<img src="${resource(dir:'images/icons', file:'detail.png')}" height="16" width="16"/>
			</td>
			<td><g:link class="anchor" controller="rositaJob" action="show" id="${jobs[0].id}">View the running job</g:link></td></tr>
			</table>
			<table class="striped" width="100%">
				<thead>
					<tr>
						<th>Data sources</th>
					</tr>
				</thead>
				<tbody>
					<g:each in="${clinicData}" var="clinic" status="j">
					<tr>
						<td><img src="${resource(dir:'images/icons',file:'folder_16.png')}"/> <b>${clinic.dataSource.dataSourceName}</b> (${clinic.dataSource.dataSourceDirectory})</td>
					</tr>
					</g:each>
				</tbody>
			</table>
			</div>
		</div>
	</g:if>
	<g:else>
		<div class="stepstatus" id="stepstatus">
			<g:form name="create" action="create" method="post">
			<div id="stepicon" class="stepicon play">New job</div>
			<div class="stepmessage">
			<table width="100%">
			
				
				<g:if test="${multiClinicFailures}">
					<tr><td colspan="2">
					<center><span class="texttiny">A job cannot be started because there are data folders whose file type could not be determined. Make sure that each data source's folder contains only a set of CSV/TXT files or an XML.</span></center>			
					</td></tr>
				</g:if>
				<g:elseif test="${!clinicData}">
					<tr><td colspan="2">
					<center><span class="texttiny">No active data sources have been set up - <a class="anchor" href="${createLink([action:'index', controller:'multiClinicDataSource'])}">go to Data Source Administration</a> to set up data sources.</span>
					</center></td></tr>
				</g:elseif>
				<g:else>
					<tr><td>Name</td><td><g:textField name="name" style="width: 90%" /></td></tr>
					<tr><td>Start at step:</td><td><g:select from="${workflowStepDescriptions}" noSelection="${['0':'(Start from beginning)']}" optionKey="stepNumber" optionValue="title" name="startStep" style="width: 90%" /></td></tr>
					<tr><td colspan="2">
					<center>
					<div class="bigbutton" style="margin: 0px 0px 16px 0px; background-color: #f8f8f8; border-color: #ccc;">
						<div style="cursor: pointer;" class="stepicon play" onclick="$j('#create').submit()">Start new job</div>
					</div>
					</center>
					</td></tr>
				</g:else>

				<tr><td colspan="2">
					<table class="striped" width="100%">
						<thead>
							<tr>
								<th>Data sources</th>
							</tr>
						</thead>
						<g:each in="${clinicData}" var="clinic" status="j">
						<tr>
							<td>
								<img src="${resource(dir:'images/icons',file:'folder_16.png')}"/> <b>${clinic.dataSource.dataSourceName}</b> (${clinic.dataSource.dataSourceDirectory})<br/>
								<g:if test="${clinic.schemaError}">
									<div class="errormessage">${clinic.schemaError}</div>
								</g:if>
								<g:else>
								<g:each in="${clinic.files}" var="fileInfo" status="i">
									<g:set var="file" value="${fileInfo.filename}"/>
									<g:set var="filePreview" value="${fileInfo.filePreview}"/>
									<%-- TODO: This would be much better done by setting a class name and reacting in the CSS --%>
									<g:set var="extension" value="${(file.substring(file.lastIndexOf('.')+1).toLowerCase())}"/>
									<g:set var="imageFile" value="document_file_unknown.png"/>
									<g:if test="${extension.equals('xml')}">
										<g:set var="imageFile" value="document_file_xml.png"/>
									</g:if>
									<g:elseif test="${extension.equals('csv')}">
										<g:set var="imageFile" value="document_file_csv.png"/>
									</g:elseif>
									<g:elseif test="${extension.equals('txt')}">
										<g:set var="imageFile" value="document_file_txt.png"/>
									</g:elseif>
									<div style="margin-left: 16px">
										<div style="float: left">
											<img src="${resource(dir:'images/icons',file:imageFile)}"/> ${file}
										</div>
										<div style="float: right">
											<g:if test="${!fileInfo.filePreview.error && !fileInfo.filePreview.warning}">
												<img src="${resource(dir:'images/icons',file:'plus.png')}" height="16" width="16" name="${j}${i}"  title="Expand" id="Expand" style="cursor: pointer;" class="tabletoggle"/>
											</g:if>
										</div>
									</div>
									
									<g:set var="visibility" value=""/>
									<g:if test="${!fileInfo.filePreview.error && !fileInfo.filePreview.warning}">
										<g:set var="visibility" value="display: none;"/>
									</g:if>
									
									<div class="breaker">&nbsp;</div>
									
									<div style="margin-left: 32px; max-width: 490px; overflow: auto; ${visibility}" id="preview${j}${i}">
										<g:if test="${fileInfo.filePreview.error}">
											<div class="errormessage">${fileInfo.filePreview.error}</div>
										</g:if>
										<g:elseif test="${fileInfo.filePreview.warning}">
											<div class="warningmessage">${fileInfo.filePreview.warning}</div>
										</g:elseif>
										<g:else>
											<tmpl:filePreview table="${fileInfo.filePreview.table}"/>
										</g:else>
									</div>
								</g:each>
								</g:else>
							</td>
						</tr>
						</g:each>
					</table>
                    <g:hiddenField name="filename" value="${latestFilename}"/>				
				</td></tr>
				

			</table>
			</div>
			</g:form>
		</div>
	</g:else>
	
	<g:if test="${jobs}">
		<h3>Job History</h3>
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
				<td>${job.jobName}</td>
				<td><g:formatDate format="yyyy-MM-dd" date="${job.startDate}"/></td>
				<td><g:formatDate format="yyyy-MM-dd" date="${job.endDate}"/></td>
				<td style="line-height: 16px">
					<g:if test="${workflowTitles[job.workflowStepNumber - 1]?.title == 'Complete'}">
						<a href="${createLink([action:'history',controller:'rositaJob',id:job.id])}"><img src="${resource(dir:'images/icons',file:'check.png') }" width="16" height="16"/> Complete</a>
					</g:if>
					<g:elseif test="${jobInProgress && i == 0}">
						<a href="${createLink([action:'show',controller:'rositaJob',id:job.id])}"><img src="${resource(dir:'images/icons',file:'play.png') }" width="16" height="16"/> Current</a>
					</g:elseif>
					<g:else>
						<a href="${createLink([action:'history',controller:'rositaJob',id:job.id])}"><img src="${resource(dir:'images/icons',file:'close_delete_2.png') }" width="16" height="16"/> Cancelled</a>
					</g:else>
				</td>
				<td>
					<g:if test="${jobInProgress && i == 0}">
						<a href="${createLink([action:'show',controller:'rositaJob',id:job.id])}">
							<img src="${resource(dir:'images/icons',file:'detail.png')}" width="16" height="16" />
						</a>
					</g:if>
					<g:else>
						<a href="${createLink([action:'history',controller:'rositaJob',id:job.id])}"><img src="${resource(dir:'images/icons',file:'detail.png')}" width="16" height="16"/></a>
					</g:else>
				</td>
			</tr>
		</g:each>
		<tr><td colspan="5" style="text-align: center;"><a class="anchor" href="${createLink([action:'jobs', controller:'rositaJob'])}">View more</a></td></tr>
		</table>
	</g:if>
	<br/>
	
	<g:if test="${!clinicData}">
		<div class="helparrow">&nbsp;</div>
	</g:if>
	
	<g:javascript>
       	$j(document).ready(function() {
	        $j('.tabletoggle').bind('click', function() { 
			    var tableToToggle = $j('#preview' + $j(this).attr('name'));
			    if(this.title == "Expand")	{	 		
			 		this.src="${resource(dir:'images/icons', file:'minus.png')}";
			 		tableToToggle.show();
			 		this.title="Collapse";
			 	}
			 	else{
			 		this.src="${resource(dir:'images/icons', file:'plus.png')}";
			 		tableToToggle.hide();
			 		this.title="Expand";
			    }
			});
		});
    </g:javascript>
</body>