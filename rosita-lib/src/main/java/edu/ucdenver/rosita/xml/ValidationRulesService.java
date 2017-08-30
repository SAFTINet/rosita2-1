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

package edu.ucdenver.rosita.xml;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import edu.ucdenver.rosita.ArgHandler;
import edu.ucdenver.rosita.utils.RositaLogger;

public class ValidationRulesService {
	
	static DataSource ds = null;
	static Map<String, Boolean> errorMap = new HashMap<String, Boolean>();
	
	public static boolean isValidationErrorAllowed(String errorType) {
		if (ds == null) {
			try {
				initialize();
			}
			catch (IOException e) {
				RositaLogger.error(e);
				System.exit(1);
			}
		}
		return errorMap.get(errorType);
	}
	
	public static void initialize() throws IOException {
		RositaLogger.getInstance();
		
		ArgHandler.initialize();
		
		String db = ArgHandler.getArg("database");
		String host = ArgHandler.getArg("host", "localhost");
		String port = ArgHandler.getArg("port", "5432");
		String user = ArgHandler.getArg("user");
		String password = ArgHandler.getArg("password");
	
		ds = new DriverManagerDataSource("jdbc:postgresql://" + host + ":" + port + "/" + db, user, password);
			
		String sqlQuery = "SELECT name, allow FROM cz.validation_error_handler;"; 
		PreparedStatement ps = null;
		Connection conn = null;
		
		try{
			conn = ds.getConnection();
			ps = conn.prepareStatement(sqlQuery);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				errorMap.put(rs.getString(1), rs.getBoolean(2));
	        }
		
		} catch (SQLException sqle) {
			RositaLogger.log(true, "ERROR|||" + sqle.getMessage());
			RositaLogger.error(sqle);
			RositaLogger.log(false, sqle.getMessage());
		} 
		finally {
			try {
				if (ps != null) { ps.close(); }
				if (conn != null) { conn.close(); }
			}
			catch (SQLException e) {
				RositaLogger.log(true, "ERROR|||" + e.getMessage());
				RositaLogger.error(e);
				RositaLogger.log(false, e.getMessage());
			}
		}
	
	}

}
