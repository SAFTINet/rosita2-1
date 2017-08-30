<!DOCTYPE HTML5>
<html>
    <head>
        <title>ROSITA</title>
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <script src="${resource(dir:'js',file:'rosita.js')}"></script>
		<script src="${resource(dir:'js',file:'jquery.js')}"></script>
		<g:javascript>$j = jQuery.noConflict(); $j.ajaxSetup({cache: false});</g:javascript>
		<script src="${resource(dir:'js',file:'jqueryui.js')}"></script>
		<link rel="stylesheet" href="${resource(dir:'css',file:'rosita.css')}" />
        <g:layoutHead />
    </head>

	<body>
	<center>
		<div id="content" style="width: 80%;"><g:layoutBody /></div>
	</center>
	
	<div class="topbar" style="text-align: right">
		<g:isLoggedIn>
			<g:link controller="rositaJob" action="index" class="topbutton ${page?.equals('home') ? 'highlight' : ''}">Home</g:link>
			<g:link controller="rositaJob" action="currentJob" class="topbutton ${page?.equals('job') ? 'highlight' : ''}">View current job</g:link>
			<g:link controller="rositaJob" action="jobs" class="topbutton ${page?.equals('jobs') ? 'highlight' : ''}">Job history</g:link>
			<g:link controller="multiClinicDataSource" action="list" class="topbutton ${page?.equals('multiClinic') ? 'highlight' : ''}">Data source admin</g:link>
			<g:link controller="validationRules" action="index" class="topbutton ${page?.equals('validationRules') ? 'highlight' : ''}">Validation rules</g:link>
			<g:link controller="utils" action="about" class="topbutton ${page?.equals('about') ? 'highlight' : ''}">System admin</g:link>
			<g:link controller="logout" class="topbutton">Log off</g:link>
		</g:isLoggedIn>
		<g:isNotLoggedIn>
			<g:link controller="login" class="topbutton">Log on</g:link>
		</g:isNotLoggedIn>
		<g:set var="ver" value="${grailsApplication.metadata['app.version']}"/>
		<a class="toptext">ROSITA v${ver.substring(0, Math.min(ver.length(), 5))}</a>
	</div>
	<div class="logocutout">
		<a href="${createLink([action:'index',controller:'rositaJob'])}"><img src="${resource(dir:'images',file:'banner.png')}"/></a>
	</div>
	</body>
</html>