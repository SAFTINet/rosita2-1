<g:if test="${!status.shouldUpdate}">
	<tmpl:/ui/checkmark test="${status?.publishRulesLoaded}" showFail="true"/> <span class="anchor" onclick="startPublishRulesImport()">Import now</span>
	
	<g:if test="${status.lastStep}">
		<div style="float: right">
			<span class="texttiny">Last run <g:formatDate date="${status.lastStepStartDate}" format="yyyy-MM-dd"/></span>
			<tmpl:/rositaJob/consoleLink id="${status.lastStepId}" errorsExist="${!status.lastStepSuccess}"/>
		</div>
	</g:if>
</g:if>
<g:else>
	<img src="${resource([dir: 'images/icons', file: 'ajax-loader.gif'])}" width="16" height="16" style="float: left;"/>
	<div style="margin-left: 24px"><tmpl:/workflowStep/bar maxwidth="250" numerator="${status?.current}" denominator="${status?.max}" color="blue"/></div>
	
</g:else>