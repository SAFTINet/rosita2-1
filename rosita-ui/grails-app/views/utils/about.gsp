<!DOCTYPE HTML5>
<%@ page import="com.recomdata.grails.rositaui.service.RositaPropertiesService" %>
<%
    def rositaPropertiesService = grailsApplication.classLoader.loadClass('com.recomdata.grails.rositaui.service.RositaPropertiesService').newInstance()
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="layout_main" />
	<script src="${resource(dir:'js',file:'backgroundtasks.js')}"></script>
</head>
</html>
<body>
	<g:set var="page" value="about" scope="request"/>
<g:if test='${flash.message}'><div class='flashmessage'>${flash.message}</div></g:if>
<div class="stepstatus" id="stepstatus">

		<center><b>ROSITA frontend v${grailsApplication.metadata['app.version']}</b></center>
		
		<table class="striped" width="100%">
			<tr><td style="width: 250px">ROSITA login</td><td>${com.recomdata.grails.domain.User.list()[0].getName()} / **** <g:link controller="user" action="edit" class="anchor">Change</g:link></td></tr>
			<tr><td>Server time</td><td>${new Date()}</td></tr>
			<tr><td>Connection to database</td><td><tmpl:/ui/checkmark test="${true}" showFail="true"/> <%-- NB. Pointless --%> <g:link controller="utils" action="databaseStatus" class="anchor">View database processes</g:link></td></tr>
			<tr><td>ROSITA lib version</td><td><tmpl:/ui/checkmark test="${2.1}" showFail="true"/> 2.1 </td></tr>
			<tr><td>Lib connection to database</td><td><tmpl:/ui/checkmark test="${info?.libConnection}" showFail="true"/> ${info?.libConnectionError ?: ''}</td></tr>
			<tr><td>Lib connection to GRID node</td><td><tmpl:/ui/checkmark test="${info?.gridConnection}" showFail="true"/> ${info?.gridConnectionError ?: ''}</td></tr>
			<tr><td>rosita.sh is reachable</td><td><tmpl:/ui/checkmark test="${info.shellScriptReachable}" showFail="true"/></td></tr>
			<tr><td>Workflow steps loaded</td><td><tmpl:/ui/checkmark test="${info?.workflowSteps}" showFail="true"/></td></tr>
			<tr><td>Validation rules loaded</td><td><tmpl:/ui/checkmark test="${info?.validationRules}" showFail="true"/></td></tr>
			<tr><td>Linkage data</td><td><g:link controller="linkageLog" action="list" class="anchor">View linkage log</g:link><br/><g:link controller="linkageResult" action="list" class="anchor">View linkage results</g:link></td></tr>
			<tr><td>Import cohort data</td><td><g:link controller="user" action="importCohortData" class="anchor">Import cohort data</g:link></td></tr>
			<tr><td>OMOP Vocabulary loaded<div class="texttiny">Looking in folder: ${rositaPropertiesService.get('folder.vocabulary')}</div></td><td><div id="loadOmopVocabularyStatus">&nbsp;</div></td></tr>
			<tr><td>Profile rules loaded<div class="texttiny">Will import from file: ${rositaPropertiesService.get('file.profilerules')}</div></td><td><div id="loadOscarStatus">&nbsp;</div></td></tr>
			<tr><td>Publish rules loaded<div class="texttiny">Will import from file: ${rositaPropertiesService.get('file.publishrules')}</div></td><td><div id="loadPublishRulesStatus">&nbsp;</div></td></tr>
		</table>		
</div>

<div class="stepstatus">
	<center><b>ROSITA properties</b></center>
	<table class="striped" width="100%">
		<g:each in="${props}" var="prop">
		<tr>
			<td>
				${prop[0]}
			</td>
			<td>
				<g:if test="${prop[0].indexOf('password') > -1}">
					****
				</g:if>
				<g:else>
					${prop[1]}
				</g:else>
			</td>
		</tr>
		</g:each>
	</table>
	<div style="text-align: center">
		<table class="actiontable" style="margin: auto;">
			<tr><td>
				<img src="${resource(dir:'images/icons', file:'refresh.png')}" height="16" width="16"/>
			</td>
			<td>
				<a href="${createLink(action:'refreshProperties')}" class="anchor">Reload properties</a>
			</td>
			</tr>
		</table>
	</div>
</div>

<%-- For potential future properties in the database

<div class="stepstatus">
	<center><b>Administrative options</b></center>
	<table class="striped" width="100%">
		<tr>
			<td>Skip verification steps in workflow</td>
			<td>
	           	<div><input type="radio" class="workflowSkipVerification" ${!(systemPropertyMap?.workflowSkipVerification) ? 'checked="checked"' : ''} name="workflowSkipVerification" value=""/> No - pause after loading raw and OMOP tables and wait for user confirmation</div>
	           	<div><input type="radio" class="workflowSkipVerification" ${systemPropertyMap?.workflowSkipVerification ? 'checked="checked"' : ''} name="workflowSkipVerification" value="true"/> Yes - continue with the workflow after completing raw and OMOP loads</div>
			</td>
		</tr>
	</table>
</div>


<g:javascript>
	$j(document).ready(function() {
	    $j('.workflowSkipVerification').bind('change', function() {
	    	if ($j(this).is(':checked')) {
	    		var value = $j(this).val();
				$j.ajax({
					url: '/rosita/utils/setSystemProperty',
					type: 'GET',
					data: {key: 'workflowSkipVerification', value: value},
					
					success: function(msg) {
					},
					
					error: function(jqXHR, textStatus) {
						if (jqXHR.responseText != null && jqXHR.responseText != "") {
							alert(jqXHR.responseText);
						}
					}
				});
	    	}
	    });
	});
</g:javascript>
--%>
</body>