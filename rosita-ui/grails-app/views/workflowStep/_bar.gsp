	<g:if test="${!numerator}"><g:set var="numerator" value="${0}"/></g:if>
	<g:if test="${!maxwidth}"><g:set var="maxwidth" value="${540}"/></g:if>
	<g:if test="${!denominator}"><g:set var="denominator" value="${0}"/></g:if>
	<g:if test="${!(numerator instanceof Long || numerator instanceof Integer)}">
		<g:set var="numerator" value="${Long.parseLong(numerator)}"/>
	</g:if>
	<g:if test="${!(denominator instanceof Long || denominator instanceof Integer)}">
		<g:set var="denominator" value="${Long.parseLong(denominator)}"/>
	</g:if>
	<g:if test="${!(maxwidth instanceof Long || maxwidth instanceof Integer)}">
		<g:set var="maxwidth" value="${Long.parseLong(maxwidth)}"/>
	</g:if>


<div class="progressbarborder" style="border-color: ${color}; width: ${maxwidth}">	
	<g:if test="${denominator == 0}">
		<g:set var="barwidth" value="${0}"/>
	</g:if>
	<g:else>
		<g:set var="barwidth" value="${Math.round((numerator*maxwidth)/denominator)}"/>
		<g:if test="${barwidth > maxwidth}">
			<g:set var="barwidth" value="${maxwidth}"/>
		</g:if>
	</g:else>
	<div class="progressbar ${color}" style="background-color: ${color}; width: ${barwidth}px">&nbsp;</div>
</div>

<table class="progressbartable" width="${maxwidth}">
	<tr>
		<td>
			<g:if test="${label}">
				${label}: 
			</g:if>
			<g:if test="${denominator != 0}">
				${numerator}/${denominator}
			</g:if>
			<g:else>
				${numerator}
			</g:else>
		</td>
		
		<td align="right">
			<g:if test="${barwidth}">
				<%-- Show 99%, do not indicate complete until a step has actually been confirmed --%>
				${Math.min(Math.round(barwidth/(maxwidth/100.0)), 99)}%
			</g:if>
		</td>
	</tr>
</table>