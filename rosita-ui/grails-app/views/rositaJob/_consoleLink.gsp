<a href="${createLink(controller: 'rositaLog', action: 'list', id: id)}">
	<g:set var="icon" value="computer_monitor_16.png"/>
	<g:if test="${errorsExist}">
		<g:set var="icon" value="computer_monitor_error_16.png"/>
	</g:if>
	<g:elseif test="${warningsExist}">
		<g:set var="icon" value="computer_monitor_warning_16.png"/>
	</g:elseif>
	<img src="${resource(dir:'images/icons', file:icon)}"/>
</a>