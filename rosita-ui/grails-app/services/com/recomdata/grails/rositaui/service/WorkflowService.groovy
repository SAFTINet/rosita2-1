package com.recomdata.grails.rositaui.service

import org.codehaus.groovy.grails.commons.ApplicationHolder

import com.recomdata.grails.domain.ClinicStatus;
import com.recomdata.grails.domain.MultiClinicDataSource;
import com.recomdata.grails.domain.RositaJob;
import com.recomdata.grails.domain.WorkflowStepInstance;
import com.recomdata.grails.domain.WorkflowStepDescription;

class WorkflowService {

	def rositaPropertiesService
	
    static transactional = true
	
	/**
	 * Advance a job's workflow. Get the next step from the workflow table, update the current job
	 * and start the handler, if necessary.
	 * @param job
	 * @return
	 * @throws Exception
	 */
    def advance(RositaJob job) throws Exception {
		
		//If this job has an end date, do not advance!
		if (job.endDate){
			println("Job was already cancelled: " + job.id)
			return
		}
		
		println("Advancing job: " + job.id)
		Integer currentStepNumber = 0
		def latestWfStep = getLatestWorkflowStep(job)?.latestStep
		if (latestWfStep) { currentStepNumber = latestWfStep.stepDescription.stepNumber }
		
		//Get the next step for this job as defined in the workflow table
		WorkflowStepDescription wt = WorkflowStepDescription.findByStepNumber(currentStepNumber)
		
		Integer nextStepNumber = 1
		if (wt) {
			nextStepNumber = wt.nextStep
		}
		
		//SPECIAL CONDITIONS!
		//If we want to skip verification steps, go straight to the next non-verify step
		//If we want to skip all pausing steps, find the next non-pausing step
		def okToProceed = false
		while (!okToProceed) {
			if (!rositaPropertiesService.get("skipsteps")) {
				okToProceed = true
			}
			else if (rositaPropertiesService.get("skipsteps").equals("validatetables")) {
				def nextStep = WorkflowStepDescription.findByStepNumber(nextStepNumber)
				if (nextStep.title.startsWith("Validate") && nextStep.title.endsWith("Tables")) {
					nextStepNumber = nextStep.getNextStep()
				}
				else {
					okToProceed = true
				}
			}
			else if (rositaPropertiesService.get("skipsteps").equals("donotpause")) {
				def nextStep = WorkflowStepDescription.findByStepNumber(nextStepNumber)
				if (nextStep.initialState.equals("paused")) {
					nextStepNumber = nextStep.getNextStep()
				}
				else {
					okToProceed = true
				}
			}
			
		}
		runStep(job, nextStepNumber)
    }
	
	def rerun(RositaJob job) throws Exception {
		
		println("Rerunning step for job: " + job.id)
		Integer currentStepNumber = 0
		def latestWfStep = getLatestWorkflowStep(job)?.latestStep
		if (latestWfStep) { currentStepNumber = latestWfStep.stepDescription.stepNumber }
		
		runStep(job, currentStepNumber)
	}
		
	def runStep(RositaJob job, Integer nextStepNumber) {
		WorkflowStepDescription nextWt = WorkflowStepDescription.findByStepNumber(nextStepNumber)
		WorkflowStepInstance wf = new WorkflowStepInstance(startDate: new Date(), stepDescription: nextWt, state: nextWt.initialState, job: job)
		println("New workflow step: " + nextWt.title + " (" + nextWt.stepNumber + ") " + nextWt.initialState)
		wf.save(flush: true);
		println("Saved workflow instance")
		job.addToWorkflowSteps(wf)
		job.save(flush: true);
		
		String handler = nextWt.handler;
		
		if (handler && nextWt.initialState.equalsIgnoreCase('running')) {
			def service = ApplicationHolder.getApplication().getMainContext().getBean("${handler}")
			service.start([job: job, step: wf])
		}
    }
	
	def unpause(RositaJob job, paramMap) throws Exception {
		def workflowStep = getLatestWorkflowStep(job).latestStep
		
		paramMap.job = job
		paramMap.step = workflowStep
		
		WorkflowStepDescription wt = WorkflowStepDescription.findByStepNumber(workflowStep.stepDescription.stepNumber)
		String handler = wt.handler;
		if (handler) {
			workflowStep.state = 'running'
			workflowStep.save(flush: true)
			def service = ApplicationHolder.getApplication().getMainContext().getBean("${handler}")
			service.start(paramMap)
		}
	}
	
	def duplicateAndUnpause(RositaJob job, paramMap) throws Exception {
		def workflowStep = getLatestWorkflowStep(job).latestStep
		
		paramMap.job = job
		paramMap.step = workflowStep
		
		WorkflowStepDescription wt = WorkflowStepDescription.findByStepNumber(workflowStep.stepDescription.stepNumber)
		WorkflowStepInstance wf = new WorkflowStepInstance(startDate: new Date(), stepDescription: wt, state: 'running', job: job)
		println("New workflow step: " + wt.title + " (" + wt.stepNumber + ") " + wt.initialState)
		wf.save(flush: true);
		println("Saved workflow instance")
		job.addToWorkflowSteps(wf)
		job.save(flush: true);
		
		paramMap.step = wf
		String handler = wt.handler;
		if (handler) {
			def service = ApplicationHolder.getApplication().getMainContext().getBean("${handler}")
			service.start(paramMap)
		}
	}
	
	def getStatus(RositaJob job) throws Exception {

		def status = [:]
		
		def latestSteps = getLatestWorkflowStep(job)
		def latestStep = latestSteps.latestStep
		def previousStep = latestSteps.previousStep
		
		if (latestStep) {
			WorkflowStepDescription wt = latestStep.stepDescription
			String handler = wt.handler;
			
			if (handler) {
				def service = ApplicationHolder.getApplication().getMainContext().getBean("${handler}")
				status = service.getStatus()
			}
			
			status.put('workflowStep', wt.stepNumber)
			status.put('stepId', latestStep.id)
			//status.put('totalElements', job.totalElements)
			status.put("status", latestStep.state)
			status.put("previousStepStatus", previousStep?.state)
			
			return status
		}
		
		//If no steps, the job hasn't started.
		status.put("status", "pending")
		return status
	}
	
	def getLatestWorkflowStep(RositaJob job) throws Exception {
		def jobWfSteps = WorkflowStepInstance.createCriteria().list() {
			eq('job.id', job.id)
			order('startDate', 'desc')
		}
		if (jobWfSteps) {
			if (jobWfSteps.size() > 1) {
				return [latestStep: jobWfSteps[0], previousStep: jobWfSteps[1]]
			}
			return [latestStep: jobWfSteps[0]]
		}
		return null
	}
	
	def insertClinicsUntilStartStep(RositaJob job, Integer startStep) {
		def clinics = MultiClinicDataSource.findAllByActive(true)
		def date = new Date()
		def wfsteps = getWorkflowStepsInOrder()
		def stepIndex = 0
		def currentStepDesc = wfsteps.get(0)
		while (currentStepDesc.stepNumber != startStep) {
			def wsi = new WorkflowStepInstance(job: job, stepDescription: currentStepDesc, startDate: date, endDate: date, state: 'skipped').save()
			for (clinic in clinics) {
				new ClinicStatus(job: job, workflowStep: wsi, mcDataSource: clinic, success: true).save()
			}
			stepIndex++
			currentStepDesc = wfsteps.get(stepIndex)
		}
	}
	
	static def workflowStepsInOrder = []
	def getWorkflowStepsInOrder() {
		if (workflowStepsInOrder) {
			return workflowStepsInOrder
		}
		
		def step = WorkflowStepDescription.findByStepNumber(1)
		while (step.nextStep) {
			workflowStepsInOrder += step
			step = WorkflowStepDescription.findByStepNumber(step.nextStep)
		}
		return workflowStepsInOrder
	}
}
