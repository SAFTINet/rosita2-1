<table class="datatable" style="font-size: 10pt">
	<tr>
		<td class="datalabel" style="width: 15%">File</td>
		<td class="datavalue">${status.get('filename')}</td>
	</tr>
	<tr>
		<td class="datalabel">Schema</td>
		<td class="datavalue">${status.get('schemaname')}</td>	
	</tr>
	<tr>
		<td class="datalabel">Time started</td>
		<td class="datavalue"><g:dateFromLong longDate="${status.get('timeStarted')}"/></td>
	</tr>
	<tr>
		<td class="datalabel">Time elapsed</td>
		<td class="datavalue"><g:timeSpanFromMs ms="${status.get('timeElapsed')}"/></td>
	</tr>
	<tr>
		<td class="datalabel">Elements validated</td>
		<td class="datavalue">${status.get('elementsValidated')}</td>
	</tr>
	<tr>
		<td class="datalabel">Location</td>
		<td class="datavalue">${status.get('location')}</td>
	</tr>
	<tr>
		<td class="datalabel">Errors</td>
		<td class="datavalue">${status.get('errors')}</td>
	</tr>
	<tr>
		<td class="datalabel">Last errors</td>
		<td class="datavalue">
			<g:each in="${status.get('lastErrors')}" var="error">
				<div style="font-size: 8pt;">${error.lineNumber}: ${error.type}: ${error.message}</div>
			</g:each>
		</td>
	</tr>
</table>