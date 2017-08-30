/*
*   Copyright 2012-2013 The Regents of the University of Colorado
*
*   Licensed under the Apache License, Version 2.0 (the "License")
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/

package com.recomdata.grails.domain

class WorkflowStepController {
	
	def fileService

    def error = {
		def paramMap = params
		def model = sanitizeParams(params);
		if (model.workflowStep.equals('7')) {
			model.put("files", fileService.getFiles(".csv|.txt"))
		}
		def clinicTableResult = getClinicTable(model.stepId)
		model.put("clinicTable", clinicTableResult.clinicTable)
		model.put("allClinicsFailed", clinicTableResult.allFailed)
		render(template: "error", model: model)
	}
	
	def dead = {
		def paramMap = params
		def model = sanitizeParams(params);
		render(template: "dead", model: model)
	}
	
	def status = {
		def paramMap = params
		def model = sanitizeParams(params);
		model.put("estimateMs", getTimeRemaining(model.stepId))
		def clinicTableResult = getClinicTable(model.stepId)
		model.put("clinicTable", clinicTableResult.clinicTable)
		model.put("allClinicsFailed", clinicTableResult.allFailed)
		def job = RositaJob.get(model.jobId)
		def elementsProperty = null;
		def elementsResults = JobProperty.createCriteria().list {
			eq('job', job)
			eq('key', 'rows')
			order('id', 'desc')
		}
		if (elementsResults) {
			elementsProperty = elementsResults[0]
		}
		model.put("totalElements", elementsProperty?.value)
		render(template: "status", model: model)
	}
	
	def paused = {
		def paramMap = params
		def model = sanitizeParams(params);
		model.put("jasperUrl", grailsApplication.config.rosita.jasper.url)
		
		//For export, check for new values
		if (model.workflowStep.equals('6')) { 
			model.put("hasUnmapped", checkUnmappedValues())
		}
		else if (model.workflowStep.equals('7')) {
			model.put("files", fileService.getFiles(".csv|.txt"))
		}
		render(template: "paused", model: model)
	}
	
	def sanitizeParams(prms) {
		def sanitizedParams = [:]
		for (param in prms) {
			String key = param.key
			String value = param.value
			if (key.startsWith("params[")) {
				sanitizedParams.put(key.substring(7, key.length()-1), value)
			}
			else {
				sanitizedParams.put(key, value)
			}
		}
		
		return sanitizedParams
	}
	
	def getTimeRemaining(wfStepId) {
		
		//Get this step and the latest step of its type
		WorkflowStepInstance wfStep = WorkflowStepInstance.get(wfStepId)
		if (!wfStep) { return null; }
		
		def oldWfSteps = WorkflowStepInstance.createCriteria().list([max: 1]) {
			eq('stepDescription', wfStep.stepDescription)
			eq('state', 'success')
			order('startDate', 'desc')
		}
		if (!oldWfSteps) { return null; }
		def oldWfStep = oldWfSteps[0];
		
		Date lastRunStart = oldWfStep.startDate;
		Date lastRunEnd = oldWfStep.endDate;
		Long targetTimeMs = lastRunEnd.getTime() - lastRunStart.getTime();
		//println("Got old wf step: " + lastRunStart + " " + lastRunEnd + ", ran for " + targetTimeMs);
		
		Date thisRunStart = wfStep.startDate;
		Date now = new Date();
		Long elapsedMs = now.getTime() - thisRunStart.getTime();
		//println("This step started at " + thisRunStart + ", now = " + now + ", been running for " + elapsedMs)
		
		Long timeRemaining = targetTimeMs - elapsedMs
		//println(timeRemaining + " remaining")
		return timeRemaining
	}
	
	boolean checkUnmappedValues() {
		def mappings = ConceptMap.findAllByMapped('N');
		return mappings
	}
	
	def getClinicTable(wfStep) {
		def clinicTable = []
		def result = ClinicStatus.findAllByWorkflowStep(WorkflowStepInstance.get(Long.parseLong(wfStep)))
		def allFailed = true
		
		for (clinic in result) {
			clinicTable.push([name: clinic.mcDataSource.dataSourceName, success: clinic.success])
			if (clinic.success) {
				allFailed = false
			}
		}
		
		return [clinicTable: clinicTable, allFailed: allFailed]
	}
	
	def getStepTitle(stepId) {
		def wfStep = WorkflowStepInstance.get(stepId)
		if (wfStep) {
			def wt = WorkflowStepDescription.findByStepNumber(wfStep.workflowStep);
			render wt.title;
		}
		else {
			render ("Unknown step")
		}	
	}
}
