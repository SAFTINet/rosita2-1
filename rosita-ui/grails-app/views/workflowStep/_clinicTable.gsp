<table class="striped">
	<g:each in="${clinicTable}" var="clinic">
		<tr><td>
			<g:if test="${clinic.success}">
				<img src="${resource(dir:'images/icons', file:'check.png')}" height="16" width="16"/>
			</g:if>
			<g:else>
				<img src="${resource(dir:'images/icons', file:'close_delete_2.png')}" height="16" width="16"/>
			</g:else>
		</td>
		<td>${clinic.name}</td></tr>
	</g:each>
</table>