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

import javax.sql.DataSource;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;

public class DataPublisherStateMachine {

	private static final long POLLING_INTERVAL = 15L * 1000L;
	private static final long TIMEOUT = 5L * 60L * 1000L;
    private DataSource ds       = null;
    private JdbcTemplate jdbc   = null;
    private HashMap<String, String> stateMap    = null;
    private String tableName    = "state";
    private String selectStmt   = null;

    public DataPublisherStateMachine(DataSource ds) {
        this.ds         = ds;
        this.jdbc       = new JdbcTemplate(ds);
        this.selectStmt = "select state from " + this.tableName + ";";

        //state map = "CurrentState", "TransitionState"
        stateMap = new HashMap<String, String>();
        this.stateMap.put("RUNNING","LOAD_PENDING");
        this.stateMap.put("LOAD_PENDING","READY_TO_LOAD");
        this.stateMap.put("READY_TO_LOAD","LOADING");
        this.stateMap.put("LOAD_SUCCESSFUL","LOADING");
        this.stateMap.put("LOAD_FAILED","LOADING");
        this.stateMap.put("LOADING","LOADING");
        this.stateMap.put("SHUTDOWN","");
    }

    public String canProceed() throws Exception {
        List<Map<String, Object>> result = null;
        String currentState = null;

        try {
            result          = this.jdbc.queryForList(this.selectStmt);
            currentState    = result.get(0).get("state").toString();

            if (currentState.equals("RUNNING")) {
                this.jdbc.update("update " + tableName + " set state=\"" + stateMap.get(currentState) + "\"");
                currentState = doTransition(currentState);
                if(currentState.equals("TIMEOUT")) {
                    RositaLogger.log(RositaLogger.forUi(),"'RUNNING' state never resolved");
                    return "TIMEOUT";
                }
            }
            if(currentState.equals("LOAD_PENDING")) {
                currentState = doTransition(currentState);
                if(currentState.equals("TIMEOUT")) {
                    RositaLogger.log(RositaLogger.forUi(),"'LOAD PENDING' state never resolved to '" + (String)stateMap.get(currentState) + "'");
                    return "TIMEOUT";
                }
            }
            if(currentState.equals("READY_TO_LOAD") ||
                    currentState.equals("LOAD_FAILED") ||
                    currentState.equals("LOAD_SUCCESSFUL") ||
                    currentState.equals("LOADING")) {
                //the following line is used to generate a sqlexception for testing
                //this.jdbc.update("update " + tableName + " set reason=\"" + stateMap.get(currentState) + "\"");

                //set the "LOADING" state and return "LOAD" to indicate go ahead and do the export
                this.jdbc.update("update " + tableName + " set state=\"" + stateMap.get(currentState) + "\"");
                return "LOAD";
            }
            if(currentState.equals("SHUTDOWN")) {
                currentState = doTransition(currentState);
                if(currentState.equals("TIMEOUT")) {
                    RositaLogger.log(RositaLogger.forUi(),"'LOAD_SUCCESSFUL' state never resolved to '" + (String)stateMap.get(currentState) + "'");
                    return "TIMEOUT";
                }
            }
        } catch (Exception e) {
            RositaLogger.log(RositaLogger.forUi(),"Caught Exception...rethrowing one level up");
            throw e;
        }

        //should never arrive here so throw an exception
        throw new Exception("DataPublisherStateMachine: Fatal Error: State Table is in an invalid initial state: " +
                result + " for data synchronization process");
    }

    public void setSuccess() throws SQLException {
        this.jdbc.update("update " + tableName + " set state=\"LOAD_SUCCESSFUL\"");
        RositaLogger.log(RositaLogger.forUi(),"Successfully completed OMOP publish task to Triad Grid Node");
        System.out.println("Inside setSuccess : completed...");
    }

    public void setFailure(String reason) throws SQLException {
        this.jdbc.update("update " + tableName + " set state=\"LOAD_FAILED\"");
        RositaLogger.log(RositaLogger.forUi(),reason);
        System.out.println("Inside setFailure : " + reason);
    }

    private String doTransition(String currentState) throws InterruptedException {
        String transitionState = stateMap.get(currentState);
        System.out.println("Current State: "+currentState);
        System.out.println("Transition State: "+transitionState);
        String curState = waitForState(transitionState);
        if(!curState.equals(transitionState)) {
            //state never changed so log it and return time out indicator
            RositaLogger.log(RositaLogger.forUi(),"State Machine timed out and transition state: " + transitionState + " has not been met");
            return "TIMEOUT";
        }
        return curState;
    }

    private String waitForState(String transitionState) throws InterruptedException {
        List<Map<String, Object>> result     = null;
        String value    = null;

        Long stoptime = System.currentTimeMillis() + TIMEOUT;
        Long diff = 0L;

        while(true) {
            result = this.jdbc.queryForList(this.selectStmt);
            value = result.get(0).get("state").toString();
            diff = stoptime - System.currentTimeMillis();
            RositaLogger.log(true, "STATUS|||" + transitionState + "|||" + Long.toString(diff/1000));
            if(value.equals(transitionState) || diff < 0L) {
                return value;
            }
            Thread.sleep(POLLING_INTERVAL);
        }
    }
}
