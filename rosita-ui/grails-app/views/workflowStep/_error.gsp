<g:if test="${workflowStep == '1'}">

	<b>There was a problem verifying the existence of the schema or source data files.</b> Check the <g:link class="anchor" controller="rositaLog" action="list" id="${stepId}">message log</g:link> for more information.<br/>	
	<g:if test="${clinicTable}"><tmpl:clinicTable clinicTable="${clinicTable}"/></g:if>

	To resolve this problem, either:
	<ul>
		<li>Ensure that all source files mentioned in the schema exist and can be read</li>
		<li>Edit the schema to remove references to the missing files</li>
		<li>Cancel this job and disable the failing data sources</li>
	</ul>
	
	<div class="actiontitle">Actions</div>
	
	<table class="actiontable">
			<tr><td>
				<img src="${resource(dir:'images/icons', file:'refresh.png')}" height="16" width="16"/>
			</td>
			<td><span class='anchor' onclick='rerunStep(${jobId}, ${stepId})'>Retry</span> this step</td></tr>
			<g:if test="${!allClinicsFailed}">
				<tr><td>
				<img src="${resource(dir:'images/icons', file:'skipped.png')}" height="16" width="16"/>
				</td>
				<td>You can also ignore the errors and <span class='anchor' onclick='ignoreErrorsForStep(${jobId}, ${workflowStep}, ${stepId})'>continue with only the successful data sources</span>.</td></tr>				
			</g:if>
	</table>

</g:if>
<g:elseif test="${workflowStep == '2'}">
	
	<b>Validation encountered ${errors.equals(0) ? errors : ''} errors!</b> Check the <g:link class="anchor" controller="rositaLog" action="list" id="${stepId}">message log</g:link> for more information.<br/>
	
	<g:if test="${clinicTable}"><tmpl:clinicTable clinicTable="${clinicTable}"/></g:if>
	
	To resolve this problem:
	<ul>
		<li>Ensure that the source files conform to the given schema</li>
		<li>Check that the <g:link class="anchor" controller="validationRules" action="index" id="${stepId}">validation rules</g:link> are set up to permit any expected errors</li>
	</ul>
	
	<div class="actiontitle">Actions</div>
	
	<table class="actiontable">
			<tr><td>
				<img src="${resource(dir:'images/icons', file:'computer_monitor_error_16.png')}" height="16" width="16"/>
			</td>
			<td><g:link class="anchor" controller="rositaLog" action="list" params="[id: stepId, level: 2]">View</g:link> the validation errors</td></tr>
			<tr><td>
				<img src="${resource(dir:'images/icons', file:'refresh.png')}" height="16" width="16"/>
			</td>
			<td><span class='anchor' onclick='rerunStep(${jobId}, ${stepId})'>Retry</span> this step</td></tr>
			<g:if test="${!allClinicsFailed}">
				<tr><td>
				<img src="${resource(dir:'images/icons', file:'skipped.png')}" height="16" width="16"/>
				</td>
				<td>You can also ignore the errors and <span class='anchor' onclick='ignoreErrorsForStep(${jobId}, ${workflowStep}, ${stepId})'>continue with only the successful data sources</span>.</td></tr>				
			</g:if>
	</table>

</g:elseif>
<g:elseif test="${workflowStep == '3'}">
	
	<b>Errors were encountered while loading the data!</b> Check the <g:link class="anchor" controller="rositaLog" action="list" id="${stepId}">message log</g:link> for more information.<br/>
	
	<g:if test="${clinicTable}"><tmpl:clinicTable clinicTable="${clinicTable}"/></g:if>
	
	<div class="actiontitle">Actions</div>
	
	<table class="actiontable">
			<tr><td>
				<img src="${resource(dir:'images/icons', file:'computer_monitor_error_16.png')}" height="16" width="16"/>
			</td>
			<td><g:link class="anchor" controller="rositaLog" action="list" params="[id: stepId, level: 2]">View</g:link> the load errors</td></tr>
			<tr><td>
				<img src="${resource(dir:'images/icons', file:'refresh.png')}" height="16" width="16"/>
			</td>
			<td><span class='anchor' onclick='rerunStep(${jobId}, ${stepId})'>Retry</span> this step</td></tr>
			<g:if test="${!allClinicsFailed}">
				<tr><td>
				<img src="${resource(dir:'images/icons', file:'skipped.png')}" height="16" width="16"/>
				</td>
				<td>You can also ignore the errors and <span class='anchor' onclick='ignoreErrorsForStep(${jobId}, ${workflowStep}, ${stepId})'>continue with only the successful data sources</span>.</td></tr>				
			</g:if>
	</table>
</g:elseif>
<g:elseif test="${workflowStep == '7'}">
	
	<b>Problems were encountered during the import.</b> Check the <g:link class="anchor" controller="rositaLog" action="list" id="${stepId}">message log</g:link> for more information.<br/>
	
	To resolve this problem:
	<ul>
		<li>Check the message log, correct any fields that are marked as errors, and retry this step.</li>
	</ul>
	<g:if test="${files}">
		<g:select name="csvPicker" style="width: 90%" from="${files}" noSelection="${['':'Select...']}" /><br/>
	</g:if>
	<span class="anchor" onclick="retryImport($j('#csvPicker').val(), ${stepId})">Retry with this file</span><br/><br/>
	
	<span class="anchor" onclick="skipStep(JOB_ID, ${workflowStep}, ${stepId})">Skip</span> this step<br/><br/>
</g:elseif>
<g:elseif test="${workflowStep == '14'}">
	
	<b>Errors were encountered while loading the standard tables!</b> Check the <g:link class="anchor" controller="rositaLog" action="list" id="${stepId}">message log</g:link> for error messages - changes may need to be made to the ETL rules file.<br/>
	
	<div class="actiontitle">Actions</div>
	
	<table class="actiontable">
			<tr><td>
				<img src="${resource(dir:'images/icons', file:'computer_monitor_error_16.png')}" height="16" width="16"/>
			</td>
			<td><g:link class="anchor" controller="rositaLog" action="list" params="[id: stepId, level: 2]">View</g:link> the errors</td></tr>
			<tr><td>
				<img src="${resource(dir:'images/icons', file:'refresh.png')}" height="16" width="16"/>
			</td>
			<td><span class='anchor' onclick='runStep(${jobId}, ${stepId}, 15)'>Load the ETL rules again</span> and then retry this step</td></tr>
			<tr><td>
				<img src="${resource(dir:'images/icons', file:'refresh.png')}" height="16" width="16"/>
			</td>
			<td><span class='anchor' onclick='rerunStep(${jobId}, ${stepId})'>Retry</span> this step without reloading the ETL rules</td></tr>
	</table>
</g:elseif>
<g:else>
	<b>This step failed to complete.</b> Check the <g:link class="anchor" controller="rositaLog" action="list" id="${stepId}">message log</g:link> for more information.<br/>
	<div class="actiontitle">Actions</div>
	<table class="actiontable">
	<tr><td>
			<img src="${resource(dir:'images/icons', file:'refresh.png')}" height="16" width="16"/>
		</td>
	<td><span class='anchor' onclick='rerunStep(${jobId}, ${stepId})'>Retry</span> this step</td></tr>
	</table>
</g:else>