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

package rosita

import org.codehaus.groovy.grails.commons.ApplicationHolder;

import com.recomdata.grails.domain.RositaJob;
import com.recomdata.grails.domain.RositaLog;
import com.recomdata.grails.domain.WorkflowSignal;
import com.recomdata.grails.domain.WorkflowStepInstance;
import com.recomdata.grails.rositaui.service.step.ParserService;
import com.recomdata.grails.rositaui.service.step.ProfilerService;
import com.recomdata.grails.rositaui.service.step.TruncatorService;
import com.recomdata.grails.rositaui.service.step.ValidatorService;

class SignalJob {

	def workflowService

	Boolean status = true
	def concurrent = false
	
	static def stepProcessFailures = [:]
	
    static triggers = {
		
		def startDelay = 10000
		def repeatInterval = 10000
			simple name: 'signalTrigger',
				   startDelay: startDelay,
				   repeatInterval: repeatInterval
	}
	

    def execute() {
		
		//Check for active workflow steps that have no process
		def runningSteps = WorkflowStepInstance.findAllByState('running');
		if (runningSteps) {
			if (runningSteps.size() > 1) {
				log.warn('More than one running step! Checking for dead processes...')
			}
			for (step in runningSteps) {
				if (step.stepDescription?.handler) {
					def service = ApplicationHolder.getApplication().getMainContext().getBean("${step.stepDescription.handler}")
					def stepStatus = service.getStatus()
					if (!stepStatus.processrunning) {
						//Check to see if there's a signal waiting - if there is, do nothing
						def hasSignalWaiting = WorkflowSignal.createCriteria().list {
							eq('step', step)
							eq('pending', true)
						}
						
						if (!hasSignalWaiting) {
							def failuresThisStep = stepProcessFailures[step.id] ?: 0
							stepProcessFailures[step.id] = ++failuresThisStep
							println("No process appears to be running for step " + step.id + " (warning " + failuresThisStep + ")")
						}
					}
					else {
						stepProcessFailures.remove(step.id)
					}
				}
				
				if (stepProcessFailures[step.id] > 2) {
					stepProcessFailures = [:]
					println("Process for step " + step.id + " is not responding.")
					step.state = 'dead'
					step.endDate = new Date()
					step.save()
				}
			}
		}
		
		//Now check the signal table
		
		def signals = WorkflowSignal.createCriteria().list() {
			eq('pending', true)
			order('signalDate', 'asc');
		}
		
		if (signals) {
			def signal = signals[0];
			WorkflowStepInstance currentWf = signal.step
			println("Received a signal, acting on it")
			
			signal.pending = false
			
			if (!currentWf || !currentWf.state.equals('running')) {
				signal.save(flush: true)
				println("Workflow step no longer existed/was no longer running, ignoring")
				return; //Do nothing if this signal has no workflow step
			}
			
			//Actual success = process exited successfully AND there were no errors
			if (signal.success && !currentWf.hasStepErrors) {
				currentWf.state = 'success'
			}
			else {
				currentWf.state = 'failed'
			}
			currentWf.endDate = new Date()
			//TODO Reimplement messages currentWf.message = signal.message
			currentWf.save(flush:true)
			
			//Any special updates to the job for this message
			//TODO How can we genericize this?
			if (signal.step.stepDescription?.stepNumber == 2) {
				String[] output = signal.message.split("\\|\\|\\|")
				if (output.length > 2) {
					//job.totalElements = Long.parseLong(output[2])
				}
			}
			
			
			//If successful, advance workflow to the next step
			//Only advance if we have a non-zero! Stops one-off tasks from trying to advance
			if (currentWf.state.equals('success') && signal.step.stepDescription.nextStep) {
				println("Signal indicated success, starting new workflow step")
				workflowService.advance(signal.step.job)
			}
		}
    }
}
