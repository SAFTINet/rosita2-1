var UPDATE = false;
var AJAX_TIMER = null;
var JOB_ID = null;
var UPDATE_FREQUENCY = 1000;

function callAjax(element, url, params, callbackFunction) {
	
	request = $j.ajax({
		url: url,
		type: 'POST',
		data: params
	});
	
	request.fail(function(jqXHR, textStatus) {
//		if (jqXHR.responseText) {
//			alert(jqXHR.responseText);
//		}
	});
	
	request.done(function(msg) {
		$j('#' + element).html(msg);
		callbackFunction();
	});
	
}

function createWorkflowBox(id, title) {
	
	//Look at ALL workflow boxes, and if any are still apparently running, mark them as successful.
	
	$j('.stepstatus').each(function() {
		var id = this.id;
		var stepicon = $j('#' + id + ' .stepicon');
		var stepmessage = $j('#' + id + ' .stepmessage');
		if (stepicon.hasClass('running')) {
			stepicon.removeClass('running');
			stepicon.addClass('success');
			stepmessage.empty();
		}
	});
	
	// Now create the box for the new step.
	var newWorkflowBox = $j('<div/>', {
	    id: 'stepstatus' + id,
	    'class': 'stepstatus'
	});
	
	newWorkflowBox.append($j('<div/>', {
		id: 'stepicon' + id,
		'class': 'stepicon running',
		text: title
	}));
	
	newWorkflowBox.append($j('<div/>', {
		id: 'stepmessage' + id,
		'class': 'stepmessage'
	}));
	
	newWorkflowBox.append($j('<div/>', {
		id: 'stepconsole' + id,
		'class': 'stepconsole'
	}));
	
	$j('#workflowSteps').append(newWorkflowBox);
	newWorkflowBox.hide().fadeIn(1000);
}

//Main update function. When we get a status, show the appropriate template in the box for this workflow step.
//If we do not have a box for this workflow step, create one.
function updateScreen() {
	request = $j.ajax({
		url: '/rosita/rositaJob/status/' + JOB_ID,
		type: 'GET',
		data: {},
		
		success: function(msg) {
			var wfStep = msg['workflowStep'];
			var stepId = msg['stepId'];
			var status = msg['status'];
			var previousStatus = msg['previousStepStatus'];
			
			if ($j('#stepstatus' + stepId).size() == 0) {
				createWorkflowBox(stepId, workflowTitles[wfStep]);
			}
			
			//Handle updates to this workflow step
			if (status == 'failed' || status == 'dead') {
				showStatus(stepId, status);
				getErrorDisplay(JOB_ID, wfStep, msg, status);
				UPDATE = false;
			}
			else if (status == 'paused') {
				showStatus(stepId, status);
				getPausedDisplay(JOB_ID, wfStep, msg);
				UPDATE = false;
			}
			else if (status == 'completed') {
				showStatus(stepId, 'success');
				$j('#stepmessage' + stepId).html("Workflow complete!");
				$j('#cancelButton').hide();
				UPDATE = false;
			}
			else {
				showStatus(stepId, status);
				getStatusDisplay(JOB_ID, wfStep, msg);
			}
			
			//Handle update to status of previous step
			if (previousStatus != null) {
				showStatus(stepId-1, previousStatus);
				$j('#stepmessage' + (stepId-1)).html("");
			}
			
			//Continue updating, if we should.
			if (UPDATE) {
				startUpdating();
			}
			else {
				AJAX_TIMER = null;
			}
		},
		
		error: function(jqXHR, textStatus) {
			if (jqXHR.responseText != null && jqXHR.responseText != "") {
				alert(jqXHR.responseText);
			}
		}
	});

}

function clearAndShowWorking(elementId, text, big) {
	$j('#' + elementId).empty();
	var src = "/mdm/images/spinner.gif";
	var size = 16;
	if (big) {
		src = "/mdm/images/spinnerbig.gif";
		size = 66;
	}
	var imageElement = $j('<img>', { 
	    src : src, 
	    width : size,
	    height : size,
	    alt : "Working",
	    title : "Working"
	});
	
	var divElement = $j('<div>', {
		style : "width: 100%; height: 100%; vertical-align: middle; text-align: center"
	});
	
	if (text) {
		$j('#' + elementId).append(divElement);
		divElement.append(imageElement).append(' ' + text);
	}
	else {
		$j('#' + elementId).append(divElement);
		divElement.append(imageElement);
	}
}

function cancelJob(jobId) {
	showStatus('cancel', 'running');
	$j('#stepiconcancel').attr('onclick', '');
	window.location = '/rosita/rositaJob/cancel/' + JOB_ID;
}

function resumeJob(jobId, stepId) {
	callAjax('nothing', '/rosita/rositaJob/start/' + JOB_ID, {}, startUpdating);
	showStatus(stepId, 'running');
	$j('#stepicon' + stepId).attr('onclick', '');
	$j('#stepmessage' + stepId).html("Resuming...");
}

function runStep(jobId, stepId, targetStepNumber) {
	callAjax('nothing', '/rosita/rositaJob/runStep/' + JOB_ID, {stepNumber: targetStepNumber}, startUpdating);
	$j('#stepmessage' + stepId).html("Advancing...");
}

function rerunStep(jobId, stepId) {
	callAjax('nothing', '/rosita/rositaJob/rerun/' + JOB_ID, {}, startUpdating);
	$j('#stepmessage' + stepId).html("Restarting...");
}

function runImport(filename, stepId) {
	if (filename == null || filename == '') {
		return;
	}
	callAjax('nothing', '/rosita/rositaJob/unpauseStep/' + JOB_ID, {filename: filename, stepId: stepId}, startUpdating);
}

function retryImport(filename, stepId) {
	if (filename == null || filename == '') {
		return;
	}
	callAjax('nothing', '/rosita/rositaJob/duplicateAndUnpauseStep/' + JOB_ID, {filename: filename, stepId: stepId}, startUpdating);
}

function runBackup(stepId) {
	callAjax('nothing', '/rosita/rositaJob/unpauseStep/' + JOB_ID, {stepId: stepId}, startUpdating);
}

function advanceStep(jobId, stepNum, stepId, outcome) {
	callAjax('nothing', '/rosita/rositaJob/confirm/' + JOB_ID, {wfStep: stepId, step: stepNum, outcome: outcome}, startUpdating);
	showStatus(stepId, outcome);
	showStatus(stepId+1, 'running');
	$j('#stepicon' + stepId).attr('onclick', '');
	$j('#stepmessage' + stepId).html("");
	createWorkflowBox(stepId+1, workflowTitles[workflowNext[stepNum]]);
}

function confirmStep(jobId, stepNum, stepId) { advanceStep (jobId, stepNum, stepId, 'success'); }
function skipStep(jobId, stepNum, stepId) { advanceStep (jobId, stepNum, stepId, 'skipped'); }
function ignoreErrorsForStep(jobId, stepNum, stepId) { advanceStep (jobId, stepNum, stepId, 'successwitherrors'); }

function startUpdating() {
	UPDATE = true;
	AJAX_TIMER = setTimeout(function() {updateScreen();}, UPDATE_FREQUENCY);
}

function updateConsoleIcon(id) {
	request = $j.ajax({
		url: '/rosita/rositaJob/getConsoleIcon',
		type: 'POST',
		data: {jobId:JOB_ID, stepId: id}
	});
	
	request.done(function(msg) {
		$j('#stepconsole' + id).html(msg);
	});
}

function getErrorDisplay(id, wfStep, params, status) {
	var display = 'error';
	if (status == 'dead') {
		display = 'dead';
	}
	request = $j.ajax({
		url: '/rosita/workflowStep/' + display,
		type: 'POST',
		data: {jobId:JOB_ID, wfStep: wfStep, params: params}
	});
	
	request.fail(function(jqXHR, textStatus) {
	});
	
	request.done(function(msg) {
		$j('#stepmessage' + params.stepId).html(msg);
	});
}

function getStatusDisplay(id, wfStep, params) {
	request = $j.ajax({
		url: '/rosita/workflowStep/status',
		type: 'POST',
		data: {jobId:JOB_ID, wfStep: wfStep, params: params}
	});
	
	request.fail(function(jqXHR, textStatus) {
	});
	
	request.done(function(msg) {
		$j('#stepmessage' + params.stepId).html(msg);
		$j('.progressbar').css("backgroundPosition", barPos+"px 0");
	});
}

function getPausedDisplay(id, wfStep, params) {
	request = $j.ajax({
		url: '/rosita/workflowStep/paused',
		type: 'POST',
		data: {jobId:JOB_ID, wfStep: wfStep, params: params}
	});
	
	request.fail(function(jqXHR, textStatus) {
	});
	
	request.done(function(msg) {
		$j('#stepmessage' + params.stepId).html(msg);
	});
}

function showingRunning(id) {
	return $j('#stepicon'+id).hasClass('running');
}

function showStatus(id, status) {
	$j('#stepicon'+id).removeClass();
	$j('#stepicon'+id).addClass('stepicon');
	$j('#stepicon'+id).addClass(status);
	updateConsoleIcon(id);
}

// -------------

function updateSchemaPreview() {
	$j('#schemaPreview').hide();
	var schemaPath = $j('#schemaPath').val();
	if (schemaPath == null || schemaPath == "") {
		schemaPath = $j('#schemaPathDisplay').text();
	}
	request = $j.ajax({
		url: '/rosita/multiClinicDataSource/getSchemaLayout',
		type: 'POST',
		data: {schemaFile: schemaPath, type: 'schema'},
		success: function(msg) {
			$j('#schemaPreview').html(msg).fadeIn(500);
			if (msg != "") {
				$j('#createTablesDiv').fadeIn(500);
				$j('#schemaFileOK').fadeIn(500);
			}
		},
		error: function(jqXHR, textStatus) {
			$j('#schemaPreview').html(jqXHR.responseText);
			$j('#schemaPreview').fadeIn(500);
			if ($j('#createTablesDiv:visible').size() > 0) { $j('#createTablesDiv').fadeOut(500); }
			if ($j('#schemaFileOK:visible').size() > 0) { $j('#schemaFileOK').fadeOut(500); }
		}
	});
}

function checkEtlFileOK() {
	var path = $j('#etlRulesFile').val();
	if (path == null || path == "") {
		path = $j('#etlRulesFilePathDisplay').text();
	}
	request = $j.ajax({
		url: '/rosita/multiClinicDataSource/checkFileExists',
		type: 'POST',
		data: {filename: path, type: 'etlrules'},
		success: function(msg) {
			if (msg == "") {
				$j('#etlRulesFileOK').fadeIn(500);
			}
		},
		error: function(jqXHR, textStatus) {
			if ($j('#etlRulesFileOK:visible').size() > 0) { $j('#etlRulesFileOK').fadeOut(500); }
		}
	});
}

// -------------

var barPos = 0;
function barScroll(){

    barPos -= 1;
    if (barPos <= -24) {
    	barPos = 0;
    }
    $j('.progressbar').css("backgroundPosition", barPos+"px 0");

}

// -------------

function transferFilename(path) {

    var filename = $j('#filenamePicker').val()
    if (filename == null || filename == '') {
        $j('#filename').val('');
    }
    else {
        $j('#filename').val(path + filename);
    }
}
