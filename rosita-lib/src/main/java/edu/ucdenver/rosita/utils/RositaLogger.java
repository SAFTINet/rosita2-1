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

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.sql.DataSource;

import edu.ucdenver.rosita.ArgHandler;
import edu.ucdenver.rosita.xml.ValidationRulesService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;

public class RositaLogger {
	
	//A bit of a hasty singleton class - log or suppress messages depending on whether running from command line or UI.
	private static RositaLogger me = null;
	private boolean forUi = false;
	private DataSource ds;

	private static String ERROR_SQL = "INSERT INTO cz.log(job_id, step_id, message_type, message, error_code, subtask_num, record_num, log_date, time_elapsed_secs) VALUES (?,?,?,?,?,?,?,?,?)";
	
	public static String MSG_TYPE_ERROR = "ERROR";
	public static String MSG_TYPE_WARNING = "WARNING";
	
	private RositaLogger(boolean forUi) {
		this.forUi = forUi;
	}
	
	public static RositaLogger initialize(boolean forUi, DataSource ds) throws Exception {
		me = new RositaLogger(forUi);
		me.ds = ds;
		return me;
	}
	
	public static RositaLogger getInstance() {
		if (me == null) {
			me = new RositaLogger(false);
		}
		return me;
	}
	
	public static boolean forUi() {
		return me.forUi;
	}
	
	public static void log(boolean forUi, String string) {
		if (me.forUi == forUi) {
			System.out.println(string);
		}
	}
	
	public static void error(Exception e) {
		SQLException sqle = null;
		if (e instanceof SQLException) {
			sqle = (SQLException) e;
		}
		else if (e instanceof BadSqlGrammarException) {
			BadSqlGrammarException bsge = (BadSqlGrammarException) e;
			sqle = bsge.getSQLException();
		}
		else if (e.getCause() instanceof BatchUpdateException) {
			BatchUpdateException bue = (BatchUpdateException) e.getCause();
			e = bue.getNextException();
		}
		
		if (sqle != null && sqle.getNextException() != null) {
			e = sqle.getNextException();
		}

		if (e.getMessage() != null) {
			error(e.getMessage(), MSG_TYPE_ERROR);
		}
		else {
			error(e.getClass().getCanonicalName(), MSG_TYPE_ERROR);
		}
		
	}
	
	public static boolean validationError(String message, String msgCategory) {
		boolean isAllowed = ValidationRulesService.isValidationErrorAllowed(msgCategory);
		error(message, isAllowed ? "WARNING" : "ERROR", msgCategory);
		return isAllowed;
	}
	
	public static void error(String message, String msgType) {
		error(message, msgType, "APP");
	}
	
	public static void error(String message, String msgType, String msgCategory) {
		if (me.forUi) {
			Connection conn = null;
			PreparedStatement ps = null;
			try {
				conn = me.ds.getConnection();
				ps = conn.prepareStatement(ERROR_SQL);
				
				ps.setLong(1, ArgHandler.getLong("jobid"));
				ps.setLong(2, ArgHandler.getLong("stepid"));
				ps.setString(3, msgType);
				ps.setString(4, message);
				ps.setString(5, msgCategory);
				ps.setLong(6, 1L);
				ps.setLong(7, 1L);
				ps.setTimestamp(8, new Timestamp(new java.util.Date().getTime()));
				ps.setDouble(9, 0.0);
				
				ps.execute();
				ps.close();
			} catch (SQLException ex) {
				System.out.println(msgType + ": " + message);
				System.out.println("Exception while logging - database cannot be reached!");
				ex.printStackTrace();
			}
			finally {
				try {
					ps.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
		else {
			System.out.println(msgType + ": " + message);
		}
	}

}
