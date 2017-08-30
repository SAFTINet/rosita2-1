<!DOCTYPE HTML5>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="layout_main" />
</head>
</html>
  <body>
	<div class="stack errorbox">
	   <span style="color: #C00; font-weight: bold;">Grails runtime exception! Please save this page and contact an administrator.</span>
	</div>
    <h2>Error Details</h2>
  	<div class="message errorbox">
		<strong>Error ${request.'javax.servlet.error.status_code'}:</strong> ${request.'javax.servlet.error.message'.encodeAsHTML()}<br/><br />
		<strong>Servlet:</strong> ${request.'javax.servlet.error.servlet_name'}<br/><br />
		<strong>URI:</strong> ${request.'javax.servlet.error.request_uri'}<br/><br />
		<g:if test="${exception}">
	  		<strong>Exception Message:</strong> ${exception.message?.encodeAsHTML()} <br /><br />
	  		<strong>Caused by:</strong> ${exception.cause?.message?.encodeAsHTML()} <br /><br />
	  		<strong>Class:</strong> ${exception.className} <br /><br />
	  		<strong>At Line:</strong> [${exception.lineNumber}] <br /><br />
	  		<strong>Code Snippet:</strong><br /><br />
	  		<div class="snippet">
	  			<g:each var="cs" in="${exception.codeSnippet}">
	  				${cs?.encodeAsHTML()}<br />
	  			</g:each>
	  		</div>
		</g:if>
  	</div>
	<g:if test="${exception}">
	    <h2>Stack Trace</h2>
	    <div class="stack errorbox">
	      <span style="font-family: Consolas, monospace;"><g:each in="${exception.stackTraceLines}">${it.encodeAsHTML()}<br/></g:each></span>
	    </div>
	</g:if>
	<br/>
  </body>
</html>