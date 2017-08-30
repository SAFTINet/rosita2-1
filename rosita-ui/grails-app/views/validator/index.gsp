<!DOCTYPE HTML5>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="x-ua-compatible" content="ie=edge" />
	<meta name="description" content="ROSITA UI" />
	<script src="../js/jquery.js"></script>
	<script src="../js/rosita.js"></script>
	<link rel="stylesheet" href="${resource(dir:'css',file:'rosita.css')}" />
	<title>Rosita UI</title>
</head>
</html>
<body>
	<g:javascript>
	$j = jQuery.noConflict();
	
	function startValidation() {
		callAjax('validatorStatus', '/rosita/validator/start', {});
		callAjaxContinuous('validatorStatus', '/rosita/validator/status', {});
		UPDATE_VALIDATOR = true;
	}
	
	function stopValidation() {
		callAjax('validatorStatus', '/rosita/validator/stop', {});
		UPDATE_VALIDATOR = false;
	}
	
	</g:javascript>
	<div>Validator status page!</div>
	<div style="cursor: pointer; border: 1px solid black; padding: 4px; width: 100px; text-align: center" onclick="startValidation();">START</div>
	<div style="cursor: pointer; border: 1px solid black; padding: 4px; width: 100px; text-align: center" onclick="stopValidation();">STOP</div>
	<div id="validatorStatus" style="background-color: #EEEEEE; border-radius: 8px; margin: 32px;">Validator status will appear here</div>
</body>