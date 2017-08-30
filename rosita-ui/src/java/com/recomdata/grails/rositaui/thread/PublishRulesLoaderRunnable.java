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

public class PublishRulesLoaderRunnable extends AbstractStepRunnable {

	String filename = "";
	
	public Integer getWorkflowStep() { return 19; }
	public String getCommandName() { return "NONE"; }
	
	public void setFilename(String name) {
		this.filename = name;
	}
	
	public String[] getProcessParams() {
		return new String[] {scriptDir+"rosita"+(getUnix()?".sh":".bat"), "loadpublishrules", "jobid=0", "stepid="+stepId.toString(), "forui"};
	}
	
	public Map<String, Object> getStepStatus() {
		Map<String, Object> status = new HashMap<String, Object>();
		
		status.put("stepid", getStepId());
		
		String[] stats = latestOutput.split("\\|\\|\\|");
		if (stats[0].equals("STATUS")) {
			status.put("messageType", stats[0]);
			status.put("current", stats[1]);
			status.put("max", stats[2]);
		}
		else if (stats[0].equals("SUCCESS")) {
			status.put("messageType", stats[0]);
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
