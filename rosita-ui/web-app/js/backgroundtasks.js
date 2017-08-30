var UPDATE_VOCABULARY = false;
var AJAX_TIMER_VOCABULARY = null;
var UPDATE_OSCAR = false;
var AJAX_TIMER_OSCAR = null;
var UPDATE_PUBLISH_RULES = false;
var AJAX_TIMER_PUBLISH_RULES = null;

function startOmopVocabularyImport() {
	request = $j.ajax({
		url: '/rosita/utils/startLoadVocabulary',
		type: 'GET',
		data: {},
		success: function(msg) {
			UPDATE = true;
			$j('#loadOmopVocabularyStatus').html('<img src="/rosita/images/icons/ajax-loader-flat.gif"/>');
			setTimeout(function() {updateVocabularyStatus();}, 5000);
		},
		error: function(jqXHR, textStatus) {
			if (jqXHR.responseText != null && jqXHR.responseText != "") {
				alert(jqXHR.responseText);
			}
		}
	});
}

function startOscarImport() {
	request = $j.ajax({
		url: '/rosita/utils/startLoadOscar',
		type: 'GET',
		data: {},
		success: function(msg) {
			UPDATE = true;
			$j('#loadOscarStatus').html('<img src="/rosita/images/icons/ajax-loader-flat.gif"/>');
			setTimeout(function() {updateOscarStatus();}, 5000);
		},
		error: function(jqXHR, textStatus) {
			if (jqXHR.responseText != null && jqXHR.responseText != "") {
				alert(jqXHR.responseText);
			}
		}
	});
}

function startPublishRulesImport() {
	request = $j.ajax({
		url: '/rosita/utils/startLoadPublishRules',
		type: 'GET',
		data: {},
		success: function(msg) {
			UPDATE = true;
			$j('#loadPublishRulesStatus').html('<img src="/rosita/images/icons/ajax-loader-flat.gif"/>');
			setTimeout(function() {updatePublishRulesStatus();}, 5000);
		},
		error: function(jqXHR, textStatus) {
			if (jqXHR.responseText != null && jqXHR.responseText != "") {
				alert(jqXHR.responseText);
			}
		}
	});
}

//////////////////////

function updateVocabularyStatus() {
	request = $j.ajax({
		url: '/rosita/utils/loadVocabularyStatus',
		type: 'GET',
		data: {},
		
		success: function(msg) {
			$j('#loadOmopVocabularyStatus').html(msg.html);
			
			//Continue updating, if we should.
			if (msg.shouldUpdate) {
				startUpdatingVocabulary();
			}
			else {
				AJAX_TIMER_VOCABULARY = null;
			}
		},
		
		error: function(jqXHR, textStatus) {
			if (jqXHR.responseText != null && jqXHR.responseText != "") {
				alert(jqXHR.responseText);
			}
		}
	});
}

function updateOscarStatus() {
	request = $j.ajax({
		url: '/rosita/utils/loadOscarStatus',
		type: 'GET',
		data: {},
		
		success: function(msg) {
			$j('#loadOscarStatus').html(msg.html);
			
			//Continue updating, if we should.
			if (msg.shouldUpdate) {
				startUpdatingOscar();
			}
			else {
				AJAX_TIMER_OSCAR = null;
			}
		},
		
		error: function(jqXHR, textStatus) {
			if (jqXHR.responseText != null && jqXHR.responseText != "") {
				alert(jqXHR.responseText);
			}
		}
	});
}

function updatePublishRulesStatus() {
	request = $j.ajax({
		url: '/rosita/utils/loadPublishRulesStatus',
		type: 'GET',
		data: {},
		
		success: function(msg) {
			$j('#loadPublishRulesStatus').html(msg.html);
			
			//Continue updating, if we should.
			if (msg.shouldUpdate) {
				startUpdatingPublishRules();
			}
			else {
				AJAX_TIMER_PUBLISH_RULES = null;
			}
		},
		
		error: function(jqXHR, textStatus) {
			if (jqXHR.responseText != null && jqXHR.responseText != "") {
				alert(jqXHR.responseText);
			}
		}
	});
}

//////////////////////

function startUpdatingVocabulary() {
	UPDATE_VOCABULARY = true;
	AJAX_TIMER_VOCABULARY = setTimeout(function() {updateVocabularyStatus();}, UPDATE_FREQUENCY);
}

function startUpdatingOscar() {
	UPDATE_OSCAR = true;
	AJAX_TIMER_OSCAR = setTimeout(function() {updateOscarStatus();}, UPDATE_FREQUENCY);
}

function startUpdatingPublishRules() {
	UPDATE_PUBLISH_RULES = true;
	AJAX_TIMER_PUBLISH_RULES = setTimeout(function() {updatePublishRulesStatus();}, UPDATE_FREQUENCY);
}

//////////////////////

$j(document).ready(function() {
	updateVocabularyStatus();
	updateOscarStatus();
	updatePublishRulesStatus();
});
