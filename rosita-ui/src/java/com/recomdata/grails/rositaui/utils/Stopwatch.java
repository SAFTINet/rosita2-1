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

package com.recomdata.grails.rositaui.utils;

public class Stopwatch {

	  private long startTime = 0;
	  private long stopTime = 0;
	  private boolean running = false;

	  public void start() {
	    this.startTime = System.currentTimeMillis();
	    this.running = true;
	  }


	  public void stop() {
	    this.stopTime = System.currentTimeMillis();
	    this.running = false;
	  }

	  public long getElapsedTime() {
	    long elapsed;
	    if (running) {
	      elapsed = (System.currentTimeMillis() - startTime);
	    }
	    else {
	      elapsed = (stopTime - startTime);
	    }
	    return elapsed;
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
	  
	  public long getStartTime() {
		  return this.startTime;
	  }
}
