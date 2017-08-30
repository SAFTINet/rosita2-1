<table class="striped filepreviewtable" width="100%">
	<g:set var="rowNum" value="${0}"/>
	<g:each in="${table}" var="row">
		<g:if test="${rowNum==0}">
			<thead>
		</g:if>
		<g:if test="${rowNum==1}">
			<tbody>
		</g:if>
		
		<tr>
			<g:each in="${row}" var="item">
				<g:if test="${rowNum==0}">
					<th>${item.name}
						<g:if test="${item.required}">
							*
						</g:if>
						<br/>
						
						<span class="tablecolumntype">${item.type}
							<g:if test="${item.type == 'varchar'}">
								(${item.length})
							</g:if>
							<g:if test="${item.type == 'decimal' }">
								(${item.precision}, ${item.scale})
							</g:if>
						</span>
					</th>
				</g:if>
				<g:else>
					<td class="even">${item}</td>
				</g:else>
			</g:each>
		</tr>
		
		<g:if test="${rowNum==0}">
			</thead>
		</g:if>
		<g:if test="${rowNum==(table.size())-1}">
			</tbody>
		</g:if>
		<g:set var="rowNum" value="${rowNum+1}"/>
	</g:each>
</table>