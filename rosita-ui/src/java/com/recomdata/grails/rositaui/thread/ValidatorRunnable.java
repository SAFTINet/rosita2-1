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

public class ValidatorRunnable extends AbstractStepRunnable {

	String filename = "";
	
	public Integer getWorkflowStep() { return 2; }
	public String getCommandName() { return "validate"; }
	
	public void setFilename(String name) {
		this.filename = name;
	}
	
	public String[] getProcessParams() {
		return new String[] {scriptDir+scriptFile, getCommandName(), "file="+filename, "jobid="+jobId.toString(), "stepid="+stepId.toString(), "forui"};
	}
	
	public Map<String, Object> getStepStatus() {
		Map<String, Object> status = new HashMap<String, Object>();
		
		if (process == null && exitCode == -1) {
			status.put("status", "failed");
			return status;
		}
		
		status.put("filename", filename);
		String[] stats = latestOutput.split("\\|\\|\\|");
		if (stats[0].equals("COMPLETE")) {
			status.put("messageType", stats[0]);
			status.put("status", "success");
			status.put("errors", stats[1]);
			status.put("elements", stats[2]);
		}
		else if (stats[0].equals("STATUS")) { //Normal status message
			status.put("messageType", stats[0]);
			status.put("elements", stats[1]);
			//Reduce location to just the first node
			String location = stats[2];
			int endIndex = location.indexOf("/", 1);
			if (endIndex > -1) {
				String shortLocation = location.substring(0, endIndex);
				status.put("location", shortLocation);
			}
			else {
				status.put("location", "");
			}
			status.put("errors", stats[3]);
			status.put("dataSourceId", stats[4]);
			status.put("currentClinic", stats[5]);
			status.put("maxClinics", stats[6]);
		}
		else if (stats[0].equals("ERROR")) {
			status.put("messageType", stats[0]);
			status.put("error", stats[1]);
		}
		else {
			status.put("messageType", "ERROR");
			status.put("error", stats[0]);
		}
		return status;
	}

}
