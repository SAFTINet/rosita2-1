package com.recomdata.grails.domain

import edu.ucdenver.rosita.utils.LibInfoService;
import edu.ucdenver.rosita.xml.RawTableService;
import grails.converters.JSON;

import org.codehaus.groovy.grails.commons.ConfigurationHolder;

class UtilsController {
	
	def loadOmopVocabularyService
	def loadOscarService
	def loadPublishRulesService
	def rositaPropertiesService

	def index = {
		redirect(action: 'about')
	}
	
	def about = {
		def info = [:]
		info.ver = grailsApplication.metadata['app.version']
		def userList = User.list()
		if (userList) {
			info.user = userList[0];
		}
		info.rositaLibVersion = '2.1';
		//info.rositaLibVersion = LibInfoService.getVersion();
		info.workflowSteps = WorkflowStepDescription.list() ?: false
		info.validationRules = ValidationErrorHandler.list() ?: false
		try { info.libConnection = LibInfoService.testDatabaseConnection(); } catch (Exception e) { info.libConnection = false; info.libConnectionError = e.getMessage(); }
		try { info.gridConnection = LibInfoService.testGridConnection(); } catch (Exception e) { info.gridConnection = false; info.gridConnectionError = e.getMessage(); }
		
		def shellScriptPath = grailsApplication.config.rosita.jar.path + File.separator + "rosita.sh"
		File shellScript = new File(shellScriptPath)
		info.shellScriptReachable = shellScript.exists() && shellScript.canRead() && shellScript.canExecute()
		
		def props = rositaPropertiesService.getAll();
		Set keySet = props.keySet();
		def propsForDisplay = []
		for (key in keySet) {
			propsForDisplay.add([key, props.get(key)])
		}
		
		propsForDisplay.sort({it[0]})
		
		
		render(view: 'about', model: [info: info, props: propsForDisplay])
	}
	
	def databaseStatus = {
		
		def rawTableService = new RawTableService(null)
		def pgsList = rawTableService.getDatabaseProcesses()
		
		render(view:'databaseStatus', model: [pgsList: pgsList])
		
	}
	
	def terminate = {
		
		def pid = params.int('id')
		
		def rawTableService = new RawTableService(null)
		def result = rawTableService.terminateProcess(pid)
		
		if (result) {
			flash.message = 'Process ' + pid + ' terminated'
		}
		else {
			flash.message = 'Process ' + pid + ' was not stopped - it may already have expired.'
		}
		redirect(action: 'databaseStatus')
	}
	
	///
	
	def startLoadVocabulary = {
		WorkflowStepInstance step = new WorkflowStepInstance(state: 'running');
		step.setStartDate(new Date())
		step.setStepDescription(WorkflowStepDescription.findByStepNumber(17))
		step.save(flush: true)
		loadOmopVocabularyService.start([step: step])
		render(text: "Started")
	}
	
	def loadVocabularyStatus = {
		def status = loadOmopVocabularyService.getStatus()
		if (!status.processrunning) {
			status.vocabularyLoaded = OmopVocabulary.count() ?: false
			status = addLastStepToStatus(status, 17)			
		}
				
		def shouldUpdate = WorkflowStepInstance.get(status.stepid)?.state?.equals("running");
		status.shouldUpdate = shouldUpdate
		def html = g.render(template: "loadVocabularyStatus", model: [status: status])
		def jsonMap = [html: html, shouldUpdate: shouldUpdate]
		render jsonMap as JSON
		
	}
	
	///
	
	def startLoadOscar = {
		WorkflowStepInstance step = new WorkflowStepInstance(state: 'running');
		step.setStartDate(new Date())
		step.setStepDescription(WorkflowStepDescription.findByStepNumber(18))
		step.save(flush: true)
		loadOscarService.start([step: step])
		render(text: "Started")
	}
	
	def loadOscarStatus = {
		def status = loadOscarService.getStatus()
		if (!status.processrunning) {
			status.oscarLoaded = com.recomdata.grails.domain.OscarRule.count() ?: false
			status = addLastStepToStatus(status, 18)			
		}
		
		def shouldUpdate = WorkflowStepInstance.get(status.stepid)?.state?.equals("running");
		status.shouldUpdate = shouldUpdate
		def html = g.render(template: "loadOscarStatus", model: [status: status])
		def jsonMap = [html: html, shouldUpdate: shouldUpdate]
		render jsonMap as JSON
		
	}
	
	///
	
	def startLoadPublishRules = {
		WorkflowStepInstance step = new WorkflowStepInstance(state: 'running');
		step.setStartDate(new Date())
		step.setStepDescription(WorkflowStepDescription.findByStepNumber(19))
		step.save(flush: true)
		loadPublishRulesService.start([step: step])
		render(text: "Started")
	}
	
	def loadPublishRulesStatus = {
		def status = loadPublishRulesService.getStatus()
		if (!status.processrunning) {
			status.publishRulesLoaded = (com.recomdata.grails.domain.EtlRule.findByRuleType('GRID') ? true : false);
			status = addLastStepToStatus(status, 19)
		}
		def shouldUpdate = WorkflowStepInstance.get(status.stepid)?.state?.equals("running");
		status.shouldUpdate = shouldUpdate
		def html = g.render(template: "loadPublishRulesStatus", model: [status: status])
		def jsonMap = [html: html, shouldUpdate: shouldUpdate]
		render jsonMap as JSON
	}
	
	/* To be used if we move rosita.properties into the database 
	def setSystemProperty = {
		def key = params.key
		def value = params.value
		def prop = SystemProperty.findByKey(key)
		etc etc etc
	}
	*/
	
	def refreshProperties = {
		rositaPropertiesService.refresh()
		redirect(action: 'about')
	}
	
	private def addLastStepToStatus(status, stepNumber) {
		status.lastStep = null;
		def result = WorkflowStepInstance.createCriteria().list() {
			eq('stepDescription', WorkflowStepDescription.findByStepNumber(stepNumber))
			order('startDate', 'desc')
			maxResults(1)
		}
		if (result) {
			status.lastStep = result[0];
			status.lastStepId = result[0].id;
			status.lastStepStartDate = result[0].startDate;
			status.lastStepSuccess = (result[0].state.equals('success'))
		}
		
		return status;
	}
	
}
