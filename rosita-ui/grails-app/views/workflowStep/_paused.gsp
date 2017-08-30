<%@ page import="com.recomdata.grails.rositaui.service.RositaPropertiesService" %>
<%
    def rositaPropertiesService = grailsApplication.classLoader.loadClass('com.recomdata.grails.rositaui.service.RositaPropertiesService').newInstance()
%>

<g:if test="${workflowStep == '5' || workflowStep == '10'}">
	This step requires Jasper reports to be run.<br/><br/>
	<div class="actiontitle">Actions</div>	
	<table class="actiontable">
			<tr><td>
				<img src="${resource(dir:'images/icons', file:'calculator.png')}" height="16" width="16"/>
			</td>
			<td><a target="_blank" class="anchor" href="${jasperUrl}">Run Jasper reports here</a></td></tr>
			<tr><td>
				<img src="${resource(dir:'images/icons', file:'check.png')}" height="16" width="16"/>
			</td>
			<td><span class="anchor" onclick="confirmStep(JOB_ID, ${workflowStep}, ${stepId})">Click here to resume the job</span> once the reports have been run.</td></tr>
	</table>
</g:if>
<g:elseif test="${workflowStep == '6'}">
	<g:if test="${hasUnmapped}">
		Unmapped concept values were found!<br/><br/>
	<div class="actiontitle">Actions</div>
		<table class="actiontable">
				<tr><td>
					<img src="${resource(dir:'images/icons', file:'tableexport.png')}" height="16" width="16"/>
				</td>
				<td><a class="anchor" href="/rosita/conceptMap/export">Export unmapped values</a></td></tr>
				<tr><td>
					<img src="${resource(dir:'images/icons', file:'tableexportall.png')}" height="16" width="16"/>
				</td>
				<td><a class="anchor" href="/rosita/conceptMap/export?all=true">Export all values</a></td></tr>
				<tr><td>
					<img src="${resource(dir:'images/icons', file:'check.png')}" height="16" width="16"/>
				</td>
				<td><span class="anchor" onclick="confirmStep(JOB_ID, ${workflowStep}, ${stepId})">Mark this step as completed</span></td></tr>
				<tr><td>
					<img src="${resource(dir:'images/icons', file:'skipped.png')}" height="16" width="16"/>
				</td>
				<td><span class="anchor" onclick="skipStep(JOB_ID, ${workflowStep}, ${stepId})">Skip this step</span></td>
				</tr>
		</table>
	</g:if>
	<g:else>
		No unmapped concept values were found.<br/><br/>
	<div class="actiontitle">Actions</div>		
		<table class="actiontable">
				<tr><td>
					<img src="${resource(dir:'images/icons', file:'skipped.png')}" height="16" width="16"/>
				</td>
				<td><span class="anchor" onclick="skipStep(JOB_ID, ${workflowStep}, ${stepId})">Skip this step</span></td>
				</tr>
		</table>
	</g:else>
</g:elseif>
<g:elseif test="${workflowStep == '7'}">

	<g:if test="${files}">
		Select a CSV file to import.<br/><br/>
	
		<g:select name="csvPicker" style="width: 90%" from="${files}" noSelection="${['':'Select...']}" /><br/>
	</g:if>
	<g:else>
		No CSV files were found for import! Add one to the import folder (${rositaPropertiesService.get('folder.datasources')}) and refresh the page to see it appear here.<br/><br/>
	</g:else>
	
	<div class="actiontitle">Actions</div>
	<table class="actiontable">
		<g:if test="${files}">
		<tr><td>
			<img src="${resource(dir:'images/icons', file:'tableimport.png')}" height="16" width="16"/>
		</td>
		<td><span class="anchor" onclick="runImport($j('#csvPicker').val(), ${stepId})">Import this file</span></td>
		</tr>
		</g:if>	
		<tr><td>
			<img src="${resource(dir:'images/icons', file:'skipped.png')}" height="16" width="16"/>
		</td>
		<td><span class="anchor" onclick="skipStep(JOB_ID, ${workflowStep}, ${stepId})">Skip this step</span></td>
		</tr>
	</table>

</g:elseif>
<g:elseif test="${workflowStep == '12'}">
	Back up the CZ and RAW schemas?<br/><br/>

	<div class="actiontitle">Actions</div>
	<table class="actiontable">
		<tr><td>
			<img src="${resource(dir:'images/icons', file:'backup.png')}" height="16" width="16"/>
		</td>
		<td><span class="anchor" onclick="runBackup(${stepId})">Back up now</span></td>
		</tr>	
		<tr><td>
			<img src="${resource(dir:'images/icons', file:'skipped.png')}" height="16" width="16"/>
		</td>
		<td><span class="anchor" onclick="skipStep(JOB_ID, ${workflowStep}, ${stepId})">Skip this step and back up manually later</span></td>
		</tr>
	</table>
</g:elseif>