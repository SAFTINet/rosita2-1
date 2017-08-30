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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

import org.codehaus.groovy.grails.commons.ApplicationHolder;
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

import com.recomdata.grails.rositaui.service.*;
import com.recomdata.grails.rositaui.utils.SignalService;

import grails.converters.JSON

class RositaJobController {

    def lowerCaseService
	def fileService
	def workflowService
	def rositaJobService
	def grailsApplication
	def rositaPropertiesService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	
	def index = {
        def xmlfiles = []
        def thedirs = []
		def clinicData = []
        def latestFilename = ""
		def multiClinicFailures = false

		def browseFolder = fileService.getBrowseFolder()
        System.out.println("browseFolder = ${browseFolder}")
        
        //we want to allowDups here since that tells us if there are mixed types in a clinic's directory
        //i.e. if there is > 1 entry in the list that setFileType uses then we have mixed types in one directory
        //and therefore that dir should not be processed.
		//WHAT THE BLOODY HELL ARE YOU DOING?!
        fileService.setFileType(browseFolder,true /*allowDups*/)
        def dataSources = MultiClinicDataSource.findAll("from MultiClinicDataSource as mc where mc.active = true")
        for(source in dataSources) {
			try {
				def schemaLayout = fileService.getSchemaLayout(rositaPropertiesService.get('folder.schemas') + File.separator + source.schemaPath)
				thedirs.add(source.toString())
				def filenames = fileService.getFiles(null, browseFolder + source.dataSourceDirectory)
				def fileInfos = []
				for (filename in filenames) {
					def fileInfo = [filename: filename, filePreview: fileService.getParsePreview(filename, source, schemaLayout)]
					fileInfos.push(fileInfo)
				}
				clinicData.add([dataSource: source, files: fileInfos])
				if (!source.fileType) {
					multiClinicFailures = true
				}
			}
			catch (Exception e) {
				clinicData.add([dataSource: source, schemaError: "Error in schema file " + source.schemaPath + ":" + e.getMessage()])
			}
        }
        
        def jobs = RositaJob.createCriteria().list ([max: 10]){
			order('id', 'desc')
		}
		//Set a flag if we have a job currently in progress, and get latest file.
		def latestWorkflowStep = null;
		def jobInProgress = false;
		if (jobs) {
			def firstJob = jobs.get(0);
			jobInProgress = firstJob.endDate ? false : true
			latestWorkflowStep = workflowService.getLatestWorkflowStep(firstJob)?.latestStep
		}
        latestFilename = rositaPropertiesService.get('folder.datasources') ?: null

		response.setHeader('Cache-Control', 'no-cache, no-store, must-revalidate')
		response.setIntHeader('Expires', -1)
		[jobs: jobs, jobInProgress: jobInProgress,
                workflowTitles: WorkflowStepDescription.listOrderByStepNumber(),
                latestFilename: latestFilename,
                latestWorkflowStep: latestWorkflowStep,
                browseFolder: browseFolder,
                files: xmlfiles += thedirs,
				clinicData: clinicData,
				multiClinicFailures: multiClinicFailures,
				workflowStepDescriptions: workflowService.getWorkflowStepsInOrder()]
	}
	
	def currentJob = {
		def jobs = RositaJob.createCriteria().list ([max: 10]){
			order('id', 'desc')
		}
		def jobInProgress = false;
		def firstJob = null
		if (jobs) {
			firstJob = jobs.get(0);
			jobInProgress = firstJob.endDate ? false : true
		}
		
		if (jobInProgress) {
			redirect(action: show, id: firstJob.id)
			return
		}
		
		flash.message = 'There is no job currently running.'
		redirect(action: index)
	}
	
	def jobs = {
		def offset = (params.int('offset') ?: 0)
		def max = (params.int('max') ?: 50)
		def jobs = RositaJob.createCriteria().list ([max: max, offset: offset]){
			order('id', 'desc')
		}
		def jobInProgress = false;
		if (jobs) {
			def firstJob = jobs.get(0);
			jobInProgress = firstJob.endDate ? false : true
		}
		
		[jobs: jobs, jobCount: jobs.getTotalCount(), jobInProgress: jobInProgress, offset: offset, max: max, workflowTitles: WorkflowStepDescription.listOrderById()]
	}
	
	def history = {
		def rositaJobInstance = RositaJob.get(params.id)
		if (!rositaJobInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'rositaJob.label', default: 'RositaJob'), params.id])}"
			redirect(action: "index")
		}
		else {
			def steps = WorkflowStepInstance.createCriteria().list() {
				eq('job', rositaJobInstance);
				order('startDate', 'asc');
			}
			
			[rositaJobInstance: rositaJobInstance, steps: steps, workflowTitles: WorkflowStepDescription.listOrderById()]
		}
	}
	
    def show = {
        def rositaJobInstance = RositaJob.get(params.id)
        if (!rositaJobInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'rositaJob.label', default: 'RositaJob'), params.id])}"
            redirect(action: "index")
        }
        else {
			def workflowSteps = WorkflowStepInstance.createCriteria().list {
				eq('job', rositaJobInstance)
				order('id');
			}

			response.setHeader('Cache-Control', 'no-cache, no-store, must-revalidate')
			response.setIntHeader('Expires', -1)
			def workflowTitles = WorkflowStepDescription.listOrderByStepNumber();
			def activeDataSources = MultiClinicDataSource.findAll("from MultiClinicDataSource as mc where mc.active = true")
            [rositaJobInstance: rositaJobInstance, workflowSteps: workflowSteps, workflowTitles: workflowTitles, dataSources: activeDataSources]
        }
    }
	
	def start = {
		def job = RositaJob.get(params.id)
		workflowService.advance(job)
		render "OK";
	}
	
	def runStep = {
		def job = RositaJob.get(params.id)
		def stepNumber = params.int('stepNumber')
		workflowService.runStep(job, stepNumber)
		render "OK";
	}
	
	def rerun = {
		def job = RositaJob.get(params.id)
		workflowService.rerun(job)
		render "OK";
	}
	
	//Confirm - for steps that are manually advanced. Could be skipped, confirmed, confirmed with errors...
	def confirm = {
		def job = RositaJob.get(params.id)
		def outcome = params.outcome
		if (!outcome) {outcome = 'success'}
		
		def latestWfStep = workflowService.getLatestWorkflowStep(job).latestStep
		latestWfStep.state = outcome
		latestWfStep.endDate = new Date()
		latestWfStep.save(flush: true)
		workflowService.advance(job)
		render "OK";
	}
	
	//Unpause - update a step's status to 'running' and start its handler.
	def unpauseStep = {
		def job = RositaJob.get(params.id)
		workflowService.unpause(job, params)
		render "OK";
	}
	
	//Duplicate and unpause - create a new instance of the step, set it to 'running' and start its handler.
	def duplicateAndUnpauseStep = {
		def job = RositaJob.get(params.id)
		workflowService.duplicateAndUnpause(job, params)
		render "OK";
	}
	
	def create = {
		
		def filename = params.filename;
        //get the browsefolder from the filename string, then get the dir name if this is flatfiles
        //if not this is .xml stuff

		def name = params.name;
		if (!name) {
			name = "ROSITA Job " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
		}
		
		def latestSameFile = RositaJob.createCriteria().list() {
			eq('fileName', filename)
			order('startDate', 'desc')
		}
		
		def lastElementCount = 0;
		if (latestSameFile) {
			//lastElementCount = latestSameFile[0].totalElements;
		}
		
		def startStep = params.int('startStep');
		
		RositaJob newJob = new RositaJob(schemaName: rositaPropertiesService.get("schema"), jobName: name, fileName: filename, startDate: new Date());
		newJob.save(flush: true);
		
		if (startStep > 0) {
			workflowService.insertClinicsUntilStartStep(newJob, startStep)
			workflowService.runStep(newJob, startStep)
		}
		else {
			workflowService.advance(newJob)
		}
			
		redirect(action: 'show', id: newJob.id);

	}
	
	def cancel = {
		def rositaJobInstance = RositaJob.get(params.id)
		
		def workflowStep = rositaJobInstance.getWorkflowStep()
		def handler = workflowStep.stepDescription.handler
		
		if (handler) {
			//If there is a handler, send a signal to cancel the service and terminate any processes using rosita-lib
			def service = ApplicationHolder.getApplication().getMainContext().getBean("${handler}")
			System.out.println(service.cancel())
			
			def process
			def scriptDir = ConfigurationHolder.config.rosita.jar.path
			ProcessBuilder pb = new ProcessBuilder(scriptDir + "/" + "canceljava.sh");
			def canonicalPath = new File(scriptDir).getCanonicalPath();
			pb.directory(new File(canonicalPath));
			pb.redirectErrorStream(true);
			try {
				process = pb.start();
				BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line;
				while ((line = r.readLine()) != null) {
					System.out.println("Cancelling rosita-lib:" + line);
				}
				def exitCode = process.waitFor();
				if (exitCode > 0) {
					throw new Exception("Canceller process exit code was " + exitCode + ", see catalina.out for details");
				}
			}
			catch (Exception e) {
				println("Error terminating rosita-lib process: " + e.getMessage());
				e.printStackTrace();
			}
		}

		rositaJobInstance.endDate = new Date();
		rositaJobInstance.save(flush: true);
		redirect(action: 'index');

	}
	
	def status = {
		def job = RositaJob.get(params.id)
		def status = workflowService.getStatus(job)
		render status as JSON
	}
	
	def getConsoleIcon = {
		if (params.stepId) {
			def step = WorkflowStepInstance.get(params.int('stepId'));
			if (!step) {
				render (contentType: 'text/plain', text: " ");
				return;
			}
			def errorsExist = step.hasStepErrors
			def warningsExist = false
			if (!errorsExist) {
				warningsExist = step.hasStepWarnings
			}
			render (template: 'consoleLink', model: [id: params.stepId, errorsExist: errorsExist, warningsExist: warningsExist])
		}
		else {
			render (contentType: 'text/plain', text: " ");
		}
	}
	
}
