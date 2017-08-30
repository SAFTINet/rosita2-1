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

package com.recomdata.grails.rositaui.service.step

import com.recomdata.grails.domain.RositaJob;
import com.recomdata.grails.domain.WorkflowStepInstance;
import com.recomdata.grails.rositaui.thread.ValidatorRunnable
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

class ValidatorService {

    static transactional = true
	
	static Thread validator = new Thread()
	static Runnable myRunnable = new ValidatorRunnable()
	
	//Start a new validator thread and run it
    def start(paramMap) {
		if (!validator.isAlive()) {
			
			RositaJob j = paramMap.job;
			WorkflowStepInstance wf = paramMap.step;
			
			myRunnable.setFilename(j.fileName)
			myRunnable.setScriptDir(ConfigurationHolder.config.rosita.jar.path)
			myRunnable.setJobId(j.id);
			myRunnable.setStepId(wf.id);
			myRunnable.setUnix(ConfigurationHolder.config.rosita.unix)
		
			validator = new Thread(myRunnable)
			validator.setName("Validation Thread")
			validator.start();
			return "Validation started"
		}
		else {
			return "The validator is already running!"
		}
    }
	
	def cancel() {
		if (validator.isAlive()) {
			myRunnable.cancel();
			return ("Validation cancelled");
		}
		else {
			return "No validator was running";
		}
	}
	
	def getStatus() {
		return myRunnable.getStatus()
	}

}
