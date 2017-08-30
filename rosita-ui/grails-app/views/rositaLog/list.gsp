<!DOCTYPE HTML5>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="layout_main" />
</head>
</html>
<body>
	
	<h3>Log Messages for '${step.stepDescription.title}'</h3>
	<div class="logRadioButtons"> 
		<div class="logRadio level2" name="2"> Errors</div>
		<div class="logRadio level1" name="1"> Warnings</div>
		<div class="logRadio level0" name="0"> All</div>
	</div>
	<br/><br/>
	
	<div id="logTable">
		<img src="${resource(dir: 'images/icons', file: 'ajax-loader-flat.gif')}"/>
	</div>
	<br/>

	<div class="bottombar">
		<div class="bigbutton">
			<a href="javascript:history.back()"><div style="cursor: pointer;" class="stepicon back">Back</div></a>
		</div>
		<div class="bigbutton" style="width: 300px">
			<a href="${createLink([action:'export', controller:'rositaLog', id: stepId])}"><div style="cursor: pointer;" class="stepicon save">Export messages</div></a>
		</div>
	</div>
	
	<g:javascript>
        	$j(document).ready(function() {
        	
        		var level = ${level ?: 0};
        		$j('.logRadio[name=' + level + ']').addClass('selected');
        		updateList(${stepId}, level, 0);
        		
		        $j('.logRadio').bind('click', function() { 
		        	$j('.logRadio').removeClass('selected');
				    $j(this).addClass('selected');
				    
				    $j('#logTable').hide();
				    
					updateList(${stepId}, $j(this).attr('name'), 0);
				    
				});
								
			});
			
			function updateList(stepId, level, offset) {
				if (stepId == -1) { stepId = ${stepId ?: 0}; }
				if (level == -1) { level = $j('.logRadio.selected').attr('name'); }
				
				if (offset > 0) {
					$j('#viewMoreText').hide();
					$j('#viewMoreLoading').show();
				}
				
				$j.ajax({
						url: '/rosita/rositaLog/listAjax',
						type: 'GET',
						data: {id: stepId, level: level, offset: offset},
						success: function(msg) {
							//If this is an addition (has offset), remove the last row and then append
							if (offset > 0) {
								$j('#logTable tr:last').remove();
								$j('#logTable table tbody').append(msg);
							}
							else {
								$j('#logTable').html(msg);
								$j('#logTable').fadeIn(500);
							}
						},
						error: function(xhr) {
							alert(xhr.responseText);
						}
				});
			}
			
	</g:javascript>
</body>