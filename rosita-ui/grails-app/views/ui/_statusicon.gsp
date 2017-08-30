<g:if test="${status.equals('ERROR')}">
	<img src="${resource([file: 'close_small.png', dir: 'images/icons'])}"/>
</g:if>
<g:elseif test="${status.equals('WARNING')}">
	<img src="${resource([file: 'attention.png', dir: 'images/icons'])}" height="16" width="16"/>
</g:elseif>
<g:elseif test="${status.equals('SUCCESS')}">
	<img src="${resource([file: 'check.png', dir: 'images/icons'])}" height="16" width="16"/>
</g:elseif>
<g:else>
	<img src="${resource([file: 'bullet.png', dir: 'images/icons'])}"/>
</g:else>