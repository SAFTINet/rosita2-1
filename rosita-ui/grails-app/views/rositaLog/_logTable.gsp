<g:if test="${lines}">
	<g:if test="${!offset}">
	<table class="striped">
		<thead>
			<tr>
				<th>&nbsp;</th>
				<th>Type</th>
				<th>Code</th>
				<th>Function</th>
				<th>Date</th>
				<th>Message</th>
			</tr>
		</thead>
		<tbody>
	</g:if>
		<g:each in="${lines}" var="line" status="i">
			<tr>
				<td><tmpl:/ui/statusicon status="${line.messageType}"/></td>
				<td>${line.messageType}</td>
				<td>${line.errorCode}</td>
				<td>${line.functionName}</td>
				<td><g:formatDate format="yyyy-MM-dd hh:mm:ss" date="${line.logDate}"/></td>
				<td>${line.message}</td>
			</tr>
		</g:each>
		<g:if test="${(offset + 100) < totalLines}">
			<tr><td colspan="6" style="text-align: center;"><span id="viewMoreText" class="anchor" onclick="updateList(-1, -1, ${offset+100})">View more</span><img style="display: none;" id="viewMoreLoading" src="${resource([file:'ajax-loader-flat.gif', dir:'images/icons'])}" /></td></tr>
		</g:if>
	<g:if test="${!offset}">
		</tbody>
		</table>
	</g:if>
</g:if>
<g:elseif test="${level == 2}">
	<div class="noResultsMessage">No errors for this step.</div>
</g:elseif>
<g:elseif test="${level == 1}">
	<div class="noResultsMessage">No warnings for this step.</div>
</g:elseif>
<g:else>
	<div class="noResultsMessage">No messages for this step.</div>
</g:else>