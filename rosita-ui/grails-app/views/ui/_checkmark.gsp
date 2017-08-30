<g:if test="${test}">
	<img src="${resource([dir: 'images/icons', file: 'check.png'])}" width="16" height="16"/>
</g:if>
<g:elseif test="${showFail}">
	<img src="${resource([dir: 'images/icons', file: 'close_delete_2.png'])}" width="16" height="16"/>
</g:elseif>