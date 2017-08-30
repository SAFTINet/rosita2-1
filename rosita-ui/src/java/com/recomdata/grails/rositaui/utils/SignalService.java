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

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class SignalService implements InitializingBean {
	
	String dbUrl;
	String dbUsername;
	String dbPassword;
	DataSource ds;
	PreparedStatement ps = null;
	PreparedStatement consolePs = null;
	
	static SignalService me;
	
	public String getDbUrl() {
		return dbUrl;
	}
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	public String getDbUsername() {
		return dbUsername;
	}
	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}
	public String getDbPassword() {
		return dbPassword;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	
	public SignalService() {}
	
	public static SignalService getInstance() {
		return me;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		ds = new DriverManagerDataSource(dbUrl, dbUsername, dbPassword);
		ps = ds.getConnection().prepareStatement("INSERT INTO cz.signal(step_id, signal_date, pending, success, message) VALUES (?, ?, ?, ?, ?);");
		consolePs = ds.getConnection().prepareStatement("INSERT INTO cz.log(job_id, step_id, message_type, message, error_code, log_date) VALUES (?,?,?,?,?,?)");
		me = this;
	}
	
	public void sendSignal(Long stepId, boolean success) {
		sendSignal(stepId, success, "");
	}
		
	public void sendSignal(Long stepId, boolean success, String message) {
		try {
			ps.setLong(1, stepId);
			ps.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
			ps.setBoolean(3, true);
			ps.setBoolean(4, success);
			ps.setString(5, message);
			ps.execute();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void sendConsole(Long jobId, Long stepId, String message) {
		sendConsole(jobId, stepId, message, "STATUS");
	}
	
	public void sendConsole(Long jobId, Long stepId, String message, String status) {
		try {
			consolePs.setLong(1, jobId);
			consolePs.setLong(2, stepId);
			consolePs.setString(3, status);
			consolePs.setString(4, message);
			consolePs.setString(5, "");
			consolePs.setTimestamp(6, new Timestamp(new java.util.Date().getTime()));
			
			consolePs.execute();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
