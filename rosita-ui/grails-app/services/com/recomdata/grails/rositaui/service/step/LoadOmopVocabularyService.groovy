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
import com.recomdata.grails.rositaui.thread.OmopVocabularyLoaderRunnable
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

class LoadOmopVocabularyService {

    static transactional = true
	
	static Thread omopVocabularyLoader = new Thread()
	static Runnable myRunnable = new OmopVocabularyLoaderRunnable()
	
	//Start a new Omop Vocabulary Loader thread and run it
    def start(paramMap) {
		if (!omopVocabularyLoader.isAlive()) {
			
			WorkflowStepInstance wf = paramMap.step;

			myRunnable.setScriptDir(ConfigurationHolder.config.rosita.jar.path)
			myRunnable.setUnix(ConfigurationHolder.config.rosita.unix)
			myRunnable.setFilename("x");
			myRunnable.setJobId(0L);
			myRunnable.setStepId(wf.id);
		
			omopVocabularyLoader = new Thread(myRunnable)
			omopVocabularyLoader.setName("Omop Vocabulary Loading Thread")
			omopVocabularyLoader.start();
			return "Omop Vocabulary Loading started"
		}
		else {
			return "The Omop Vocabulary Loader is already running!"
		}
    }
	
	def cancel() {
		if (omopVocabularyLoader.isAlive()) {
			omopVocabularyLoader.interrupt();
			return ("Omop Vocabulary Loading cancelled");
		}
		else {
			return "No Omop Vocabulary Loader was running";
		}
	}
	
	def getStatus() {
		return myRunnable.getStatus()
	}
}
