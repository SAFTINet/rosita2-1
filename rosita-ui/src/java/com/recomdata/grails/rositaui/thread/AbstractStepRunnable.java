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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.recomdata.grails.rositaui.utils.SignalService;

public abstract class AbstractStepRunnable implements Runnable {

	boolean unix = false;
	String scriptDir = "";
	Long jobId = 0L;
	Long stepId = 0L;
	Process process = null;
	String scriptFile = "";
	
	String latestOutput = "";
	int exitCode = -1;
	
	public AbstractStepRunnable() {
	}
	
	public abstract Integer getWorkflowStep();
	public abstract String getCommandName();
	
	public void setUnix(boolean unix) {
		this.unix = unix;
	}
	
	public boolean getUnix() {
		return unix;
	}
	
	public Long getStepId() {
		return stepId;
	}

	public void setScriptDir(String scriptDir) {
		this.scriptDir = scriptDir;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public void setStepId(Long stepId) {
		this.stepId = stepId;
	}

	@Override
	public void run() {
		try {
			exitCode = -1;
			latestOutput = "";
			String canonicalPath = "";
			SignalService sig = SignalService.getInstance();
			String scriptname = "rosita";

			if (!scriptDir.endsWith("/")) {
				scriptDir = scriptDir + "/";
			}
			String suffix = (unix ? ".sh" : ".bat");
			scriptFile = "./" + scriptname + suffix;

			ProcessBuilder pb = new ProcessBuilder(getProcessParams());
			canonicalPath = new File(scriptDir).getCanonicalPath();
			pb.directory(new File(canonicalPath));
			pb.redirectErrorStream(true);
			Thread.sleep(500); //Cheat - give the database running in the parent thread a chance to record the new workflow step
			try {
				process = pb.start();
			}
			catch (Exception e) {
				SignalService.getInstance().sendConsole(jobId, stepId, e.getMessage(), "ERROR");
				SignalService.getInstance().sendSignal(stepId, false);
				return;
			}
			BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			
			System.out.println("Started process and reading...");
			
			sig.sendConsole(jobId, stepId, "START", "START");
			while ((line = r.readLine()) != null) {
				if (line.startsWith("STATUS")) {
					latestOutput = line;
				}
				System.out.println("rosita-lib:" + line);
				sig.sendConsole(jobId, stepId, line);
			}
			
			try {
				System.out.println("...Finished reading. Process exit code was " + process != null ? process.waitFor() : "not found.");
				sig.sendConsole(jobId, stepId, "COMPLETE", "COMPLETE");
				exitCode = process != null ? process.exitValue() : 1;
				sendSignal();
			}
			catch (InterruptedException e) {
				System.out.println("Step " + stepId + " cancelled - processing thread was interrupted");
				sig.sendConsole(jobId, stepId, "CANCELLED", "CANCELLED");
				exitCode = 1;
				sendSignal();
			}
		}
		catch (Exception e) {
			SignalService.getInstance().sendConsole(jobId, stepId, e.getMessage(), "ERROR");
			e.printStackTrace();
			SignalService.getInstance().sendSignal(stepId, false);
		}
		finally {
			process = null;
		}
	}
	
	public String[] getProcessParams() {
		return new String[] {scriptDir + scriptFile, getCommandName(), "jobid=" + jobId, "stepid=" + stepId, "forui"};
	}
	
	public Map<String, Object> getStatus() {
		Map<String, Object> status = getStepStatus();
		if (!status.containsKey("latestOutput")) {
			String[] output = latestOutput.split("\\|\\|\\|");
			status.put("latestOutput", output[0]);
		}
		status.put("exitCode", exitCode);
		status.put("processrunning", (process != null));

		return status;
	}
	
	public Map<String, Object> getStepStatus() {
		return new HashMap<String, Object>();
	}

	public void sendSignal() {
		SignalService.getInstance().sendSignal(stepId, exitCode == 0, latestOutput);
	}
	
	public void cancel() {
		if (process != null) {
			process.destroy();
			process = null;
			SignalService.getInstance().sendSignal(stepId, false, "CANCELLED");
		}
	}
}
