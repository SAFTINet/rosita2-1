<head>
	<meta name="layout" content="layout_main" />
	<title>ROSITA</title>
</head>

<body>
	<g:set var="page" value="changeLogin" scope="request"/>
	<div id='login'>
		<div class='inner'>
			<div class='fheader'>Change login</div>
			<div>This will update the username and password used to log in.</div>
			<g:if test='${flash.message}'><div class='loginmessage'>${flash.message}</div></g:if>
			<g:form name="update" action="update" method="post">
				<g:hiddenField name="id" value="${person.id}"/>
				<table style="border: none;">
				<tr>
					<td class="loginLabel">New username</td>
					<td><g:textField name="username" class="loginfield" value="${person.username}" /></td>
				</tr>
				<tr>
					<td class="loginLabel">New password</td>
					<td><g:passwordField name="password" class="loginfield" /></td>
				</tr>
				<tr>
					<td class="loginLabel">Confirm password</td>
					<td><g:passwordField name="passwordconfirm" class="loginfield" /></td>
				</tr>
				<tr>
					<td colspan="2">			
						<center><g:submitButton name="j_login" value="Change" /></center>
					</td>
				</tr>
				</table>
			</g:form>
		</div>		
	</div>
	
<g:javascript>
<!--
(function(){
	document.forms['loginForm'].elements['j_username'].focus();
})();
// -->
</g:javascript>

</body>
