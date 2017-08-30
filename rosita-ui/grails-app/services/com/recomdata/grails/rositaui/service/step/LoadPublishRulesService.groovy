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
import com.recomdata.grails.rositaui.thread.PublishRulesLoaderRunnable
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

class LoadPublishRulesService {

    static transactional = true
	
	static Thread publishRulesLoader = new Thread()
	static Runnable myRunnable = new PublishRulesLoaderRunnable()
	
	//Start a new PublishRules Loader thread and run it
    def start(paramMap) {
		if (!publishRulesLoader.isAlive()) {
			
			WorkflowStepInstance wf = paramMap.step;

			myRunnable.setScriptDir(ConfigurationHolder.config.rosita.jar.path)
			myRunnable.setUnix(ConfigurationHolder.config.rosita.unix)
			myRunnable.setFilename("");
			myRunnable.setJobId(0L);
			myRunnable.setStepId(wf.id);
		
			publishRulesLoader = new Thread(myRunnable)
			publishRulesLoader.setName("PublishRules Loading Thread")
			publishRulesLoader.start();
			return "PublishRules Loading started"
		}
		else {
			return "The PublishRules Loader is already running!"
		}
    }
	
	def cancel() {
		if (publishRulesLoader.isAlive()) {
			publishRulesLoader.interrupt();
			return ("PublishRules Loading cancelled");
		}
		else {
			return "No PublishRules Loader was running";
		}
	}
	
	def getStatus() {
		return myRunnable.getStatus()
	}
}
