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

public class LowerCaseRunnable implements Runnable {
    boolean unix = false;
    String scriptPath = "";
    String directory = "";

    //String latestOutput = "";
    int exitCode = -1;
    Process process = null;

    public LowerCaseRunnable() {

    }

    @Override
    public void run() {
        try {
            exitCode = -1;
            String canonicalPath = "";
            //SignalService sig = SignalService.getInstance();
            if (!this.scriptPath.endsWith("/")) {
                this.scriptPath = this.scriptPath + "/";
            }
            String suffix = (unix ? ".sh" : ".bat");
            String scriptfile;
            scriptfile = "tolowercase" + suffix;
            ProcessBuilder pb = new ProcessBuilder(this.scriptPath + scriptfile, this.directory, "forui");
            canonicalPath = new File(scriptPath).getCanonicalPath();
            pb.directory(new File(canonicalPath));
            pb.redirectErrorStream(true);
            process = pb.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            System.out.println("Started process and reading...");

            while ((line = r.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("...Finished reading. Process exit code was " + process.waitFor());
            exitCode = process.exitValue();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void setDirectory(String name) {
        this.directory = name;
    }

    public void setScriptpath(String name) {
        this.scriptPath = name;
    }

    public void setUnix(boolean unix) {
        this.unix = unix;
    }

}
