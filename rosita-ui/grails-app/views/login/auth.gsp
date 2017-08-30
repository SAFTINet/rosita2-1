<head>
	<meta name="layout" content="layout_main" />
	<title>ROSITA</title>
</head>

<body>
	<div id='login'>
		<div class='inner'>
			<div class='fheader'>ROSITA</div>
			<g:if test='${flash.message}'><div class='loginmessage'>${flash.message}</div></g:if>
			<form action='${postUrl}' method='POST' id='loginForm' class='cssform'>
				<table style="border: none;">
				<tr>
					<td class="loginLabel">Username</td>
					<td><g:textField name="j_username" class="loginfield" value="${request.remoteUser}" /></td>
				</tr>
				<tr>
					<td class="loginLabel">Password</td>
					<td><g:passwordField name="j_password" class="loginfield" /></td>
				</tr>
				<tr>
					<td colspan="2">			
						<center><g:submitButton name="j_login" value="Login" /></center>
					</td>
				</tr>
				</table>
			</form>
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
