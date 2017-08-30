<table class="striped">
	<thead>
		<tr>
			<th colspan="8">Schema Layout</th>
		</tr>
		<g:if test="${missingColumnAlert}">
			<th colspan="8"><div class="warningMessage">This schema mentions columns that are missing from the database. Use the table below to review them, and "Create Database Tables" to create the missing columns. This will drop and recreate all tables mentioned by this schema.</div></th>
		</g:if>
		<tr>
			<th>Table</th>
			<th>Column</th>
			<th>Type</th>
			<th>Length</th>
			<th>Precision</th>
			<th>Scale</th>
			<th>Required</th>
			<th>Exists</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${tables}" var="table">
			<g:set var="numColumns" value="${table.value.size()}"/>
			<tr>
				<td rowspan="${numColumns}">${table.key}</td>
				<g:set var="firstColumn" value="true"/>
				<g:each in="${table.value}" var="column">
					<g:if test="${!firstColumn}">
						<tr>
					</g:if>
				<td>${column.name}</td>
				<td>${column.type}</td>
				<td>${column.length}</td>
				<td>${column.precision}</td>
				<td>${column.scale}</td>
				<td><tmpl:/ui/checkmark test="${column.required}"/></td>
				<td><tmpl:/ui/checkmark test="${column.exists}" showFail="true"/></td>
			</tr>
				<g:set var="firstColumn" value="false"/>
			</g:each>
		</g:each>
	</tbody>
</table>