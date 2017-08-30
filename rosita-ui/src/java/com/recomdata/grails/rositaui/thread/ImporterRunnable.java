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

package com.recomdata.grails.rositaui.thread;

import java.util.HashMap;
import java.util.Map;

public class ImporterRunnable extends AbstractStepRunnable {

	String filename = "";
	
	public Integer getWorkflowStep() { return 7; }
	public String getCommandName() { return "loadvocabulary"; }
	
	public void setFilename(String name) {
		this.filename = name;
	}
	
	public String[] getProcessParams() {
		return new String[] {scriptDir+scriptFile, getCommandName(), "file="+filename, "jobid="+jobId, "stepid="+stepId, "forui"};
	}
	
	public Map<String, Object> getStepStatus() {
		
		Map<String, Object> status = new HashMap<String, Object>();
		String[] output = latestOutput.split("\\|\\|\\|");
		if (output[0].equals("STATUS")) {
			status.put("messageType", output[0]);

			if (output[1].equals("TRUNCATE")) {
				status.put("taskType", output[1]);
			}
			else {
				status.put("taskType", "LOAD");
				status.put("latestOutput", output[1]);
			}
		}
		else {
			status.put("messageType", "ERROR");
			status.put("latestOutput", output[0]);
		}
		return status;
	}

}
