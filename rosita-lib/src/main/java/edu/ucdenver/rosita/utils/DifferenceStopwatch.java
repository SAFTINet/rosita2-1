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

package edu.ucdenver.rosita.utils;

import java.text.DecimalFormat;

public class DifferenceStopwatch {

	  private long startTime = 0;
	  private long stopTime = 0;
	  private boolean running = false;
	  private long lastTime = 0;
	  private static DifferenceStopwatch instance = null;
	  private DecimalFormat df = new DecimalFormat("00.000");
	  private DecimalFormat mf = new DecimalFormat("###,###,###");
	  
	  public static DifferenceStopwatch getInstance() {
		  if (instance == null) {
			  instance = new DifferenceStopwatch();
		  }
		  return instance;
	  }

	  public void start() {
	    this.startTime = System.currentTimeMillis();
	    this.running = true;
	  }

	  public void stop() {
	    this.stopTime = System.currentTimeMillis();
	    this.running = false;
	  }

	  public String getTimeAndDiff() {
	    long elapsed;
	    if (running) {
	      elapsed = (System.currentTimeMillis() - startTime);
	    }
	    else {
	      elapsed = (stopTime - startTime);
	    }
	    Double totalTime = elapsed / 1000.0;
	    Double fromLastTime = (elapsed - lastTime) / 1000.0;
	    lastTime = elapsed;
	    
	    return df.format(totalTime) + " (" + df.format(fromLastTime) + ") " + mf.format(Runtime.getRuntime().freeMemory()) + ": ";
	  }

	  public long getElapsedTimeSecs() {
	    long elapsed;
	    if (running) {
	      elapsed = ((System.currentTimeMillis() - startTime) / 1000);
	    }
	    else {
	      elapsed = ((stopTime - startTime) / 1000);
	    }
	    return elapsed;
	  }
}
