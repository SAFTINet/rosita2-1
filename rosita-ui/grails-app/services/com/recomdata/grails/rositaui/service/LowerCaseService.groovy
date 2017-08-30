package com.recomdata.grails.rositaui.service

import com.recomdata.grails.rositaui.thread.LowerCaseRunnable
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class LowerCaseService {

    static transactional = true

    static Thread lowerCase = new Thread()
    static Runnable myRunnable = new LowerCaseRunnable();

    //Start a new lowerCase thread and run it
    def start(String directoryName) {
        if (!lowerCase.isAlive()) {
            myRunnable.setScriptpath(ConfigurationHolder.config.rosita.jar.path)
            myRunnable.setUnix(ConfigurationHolder.config.rosita.unix)
            myRunnable.setDirectory(directoryName);

            lowerCase = new Thread(myRunnable)
            lowerCase.setName("LowerCase Thread")
            lowerCase.start();
            return "lowerCase started"
        }
        else {
            return "lowerCase is already running!"
        }
    }

    def cancelValidation() {
        if (lowerCase.isAlive()) {
            lowerCase.interrupt();
            return ("lowerCase cancelled");
        }
        else {
            return "lowerCase was not running";
        }
    }

    def getStatus() {
        return myRunnable.getStatus()
    }

}
