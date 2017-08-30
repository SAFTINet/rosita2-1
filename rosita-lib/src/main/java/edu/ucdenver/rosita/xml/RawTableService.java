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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import edu.ucdenver.rosita.ArgHandler;
import edu.ucdenver.rosita.utils.ParsingService;
import edu.ucdenver.rosita.utils.RositaLogger;
import edu.ucdenver.rosita.utils.types.PgStatActivity;
import edu.ucdenver.rosita.utils.types.SchemaColumn;

public class RawTableService {
	
	DataSource ds;
	
	public RawTableService(DataSource ds) {
		this.ds = ds;
	}
	
	public List<PgStatActivity> getDatabaseProcesses() throws SQLException, IOException {
		
		PreparedStatement ps = null;
		Connection conn = null;
		
		try {
			initialize();
			
			List<PgStatActivity> pgsList = new ArrayList<PgStatActivity>();
			
			String processStatement = "SELECT procpid, datname, query_start, waiting, '' as state, current_query FROM pg_stat_activity ORDER BY procpid";
			if (ArgHandler.getInt("postgresqlversion") == 9) {
				processStatement = "SELECT pid, datname, query_start, waiting, state, query FROM pg_stat_activity ORDER BY pid";
			}
			
			conn = ds.getConnection();
			ps = conn.prepareStatement(processStatement);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				PgStatActivity pgs = new PgStatActivity();
				pgs.setPid(rs.getInt(1));
				pgs.setDatname(rs.getString(2));
				pgs.setQueryStart(rs.getTimestamp(3));
				pgs.setWaiting(rs.getBoolean(4));
				pgs.setState(rs.getString(5));
				pgs.setQuery(rs.getString(6));
				
				pgsList.add(pgs);
	        }
			
			return pgsList;
		}
		finally {
			ps.close();
			conn.close();
		}
	}
	
	public boolean terminateProcess(Integer pid) throws SQLException, IOException {
		
		PreparedStatement ps = null;
		Connection conn = null;
		
		try {
			initialize();
			
			String processStatement = "select pg_cancel_backend(?)";
			
			conn = ds.getConnection();
			ps = conn.prepareStatement(processStatement);
			ps.setInt(1, pid);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getBoolean(1);
			}
			return false;
		}
		finally {
			ps.close();
			conn.close();
		}
	}
	
	
	
	public void dropTablesInSchema(Map<String, ArrayList<SchemaColumn>> schema) throws SQLException, IOException {
		initialize();
		
		String dropStatement = "DROP TABLE IF EXISTS ";
		
		Connection conn = ds.getConnection();
		int statements = 0;
		Set<String> keys = schema.keySet();
		for (String table : keys) {
			String statement = dropStatement + "raw." + table + ";";
			PreparedStatement ps = conn.prepareStatement(statement);
			ps.execute();
			ps.close();
			statements++;
			RositaLogger.log(true, "STATUS|||DROP|||" + String.valueOf(statements) + "|||" + String.valueOf(keys.size()));
			RositaLogger.log(false, statement + ": " + String.valueOf(statements) + " of " + String.valueOf(keys.size()));
		}
		conn.close();
	}
	
	public void createTablesInSchema(Map<String, ArrayList<SchemaColumn>> schema) throws SQLException, IOException {
		initialize();
		
		String createStatement = "CREATE TABLE ";
		
		Connection conn = ds.getConnection();
		int statements = 0;
		
		Set<String> keys = schema.keySet();
		for (String table : keys) {
			String statement = createStatement + "raw." + table + " (" + getColumnsDdl(schema.get(table), true) + ")";
			System.out.println(statement);
			PreparedStatement ps = conn.prepareStatement(statement);
			ps.execute();
			ps.close();
			statements++;
			RositaLogger.log(true, "STATUS|||CREATE|||" + String.valueOf(statements) + "|||" + String.valueOf(keys.size()));
			RositaLogger.log(false, statement + ": " + String.valueOf(statements) + " of " + String.valueOf(keys.size()));
		}
		conn.close();
	}
	
	public String getColumnsDdl(List<SchemaColumn> columns, boolean addDefaultColumns) {
		StringBuilder sb = new StringBuilder();
		if (columns.size() > 0) {
			sb.append(getColumnDdl(columns.get(0)));
		}
		for (int i = 1; i < columns.size(); i++) {
			sb.append(", " + getColumnDdl(columns.get(i)));
		}
		if (addDefaultColumns) {
			sb.append(", x_etl_date timestamp without time zone, x_data_source_id bigint, x_record_num bigint");
		}
		return sb.toString();
	}
	
	public String getColumnDdl(SchemaColumn column) {
		String attributes = "";
		if (true) {//TODO Converting all columns to varchar just now
			attributes = column.getLength();
			int length = 0;
			try { length = Integer.parseInt(attributes); }
			catch (Exception e) {};
			if (length < 1) {
				attributes = "500";
			}
		}
		else if (column.getType().equals("decimal")) {
			attributes = column.getPrecision() + "," + column.getScale();
		}
		return ParsingService.getDatabaseSafeName(column.getName()) + " " + translateColumnType(column.getType()) + "(" + attributes + ")" + (column.isRequired()?" NOT NULL" : " NULL");		
	}
	
	public String translateColumnType(String type) {
		if (true) { //TODO Converting all columns to varchar just now
			return "character varying";
		}
		else {
			return type;
		}
	}
	
	public Map<String, ArrayList<SchemaColumn>> getRawSchemaExistence(Map<String, ArrayList<SchemaColumn>> schema) {

		try {
			initialize();
			
			//Loop through the schema, check each table and get column existence
			
			String tableStatementTemplate = "select column_name from information_schema.columns where table_schema='raw' and table_name='";
			
			Connection conn = ds.getConnection();
			int statements = 0;
			Set<String> keys = schema.keySet();
			for (String table : keys) {
				ArrayList<SchemaColumn> columnsInSchema = schema.get(table);
				String tableStatement = tableStatementTemplate + table + "'";
				PreparedStatement ps = conn.prepareStatement(tableStatement);
				ps.executeQuery();
				
				ResultSet rs = ps.getResultSet();
				List<String> columnsFromDatabase = new ArrayList<String>();
				while (rs.next()) {
					columnsFromDatabase.add(rs.getString("column_name"));
				}
				rs.close();
				ps.close();
				
				for (int i = 0; i < columnsInSchema.size(); i++) {
					boolean tableExists = columnsFromDatabase.size() > 0;
					boolean columnExists = columnsFromDatabase.contains(ParsingService.getDatabaseSafeName(columnsInSchema.get(i).getName()));
					columnsInSchema.get(i).setTableExists(tableExists);
					columnsInSchema.get(i).setExists(columnExists);
				}
				
				schema.put(table, columnsInSchema);
				
				statements++;
				RositaLogger.log(true, "STATUS|||CHECK|||" + String.valueOf(statements) + "|||" + String.valueOf(keys.size()));
				RositaLogger.log(false, tableStatement + ": " + String.valueOf(statements) + " of " + String.valueOf(keys.size()));
			}
			conn.close();
			//select column_name from information_schema.columns where table_schema='raw' and table_name='people';
			//If no column_names, table is missing entirely
			return schema;
		}
		catch (Exception e) {
			RositaLogger.error(e);
			e.printStackTrace();
		}
		
		return schema;
	}
	
	public void setJobProperty(String key, String value) throws Exception {
		try {
			initialize();
			
			String checkStatement = "SELECT job_property_id FROM cz.job_property WHERE job_id=? AND key=?";
			
			Connection conn = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement(checkStatement);
			
			boolean alreadyExists = false;
			try {
				ps.setLong(1, ArgHandler.getLong("jobid"));
				ps.setString(2, key);
				ResultSet rs = ps.executeQuery();
				rs.next();
				if (rs.next()) { //If we have a first row
					alreadyExists = true;
				}
			}
			finally {
				ps.close();
			}
			
			String updateStatement = "UPDATE cz.job_property SET value=? WHERE job_id=? AND key=?";
			String insertStatement = "INSERT INTO cz.job_property(job_id, key, value) VALUES (?,?,?)";
			
			try {
				if (alreadyExists) {
					ps = conn.prepareStatement(updateStatement);
					ps.setString(1, value);
					ps.setLong(2, ArgHandler.getLong("jobid"));
					ps.setString(3, key);
					ps.execute();
				}
				else {
					ps = conn.prepareStatement(insertStatement);
					ps.setLong(1, ArgHandler.getLong("jobid"));
					ps.setString(2, key);
					ps.setString(3, value);
					ps.execute();
				}
			}
				finally {
					ps.close();
					conn.close();
				}
		}
		catch (Exception e) {
			RositaLogger.error(e);
			throw e;
		}
	}
	
	public String getJobProperty(String key) throws Exception {
		initialize();
		
		String checkStatement = "SELECT value FROM cz.job_property WHERE job_id=? AND key=? ORDER BY job_property_id DESC";
		
		Connection conn = ds.getConnection();
		PreparedStatement ps = conn.prepareStatement(checkStatement);
		
		try {
			ps.setLong(1, ArgHandler.getLong("jobid"));
			ps.setString(2, key);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) { //If we have a first row
				String result = rs.getString(1);
				return(result);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ps.close();
			conn.close();
		}
		return "";
	}
	
	public void initialize() throws IOException {
		RositaLogger.getInstance();
		if (ds == null) {
			ArgHandler.initialize();
			
			String db = ArgHandler.getArg("database");
			String host = ArgHandler.getArg("host", "localhost");
			String port = ArgHandler.getArg("port", "5432");
			String user = ArgHandler.getArg("user");
			String password = ArgHandler.getArg("password");
		
			ds = new DriverManagerDataSource("jdbc:postgresql://" + host + ":" + port + "/" + db, user, password);
		}
	}
	
	public Timestamp getSqlDateTime(){ 
		String sqlQuery = "SELECT now()"; 
		PreparedStatement ps = null;
		Timestamp dateTime = null;
		Connection conn = null;
		
		try{
			initialize();
			conn = ds.getConnection();
			ps = conn.prepareStatement(sqlQuery);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				dateTime = rs.getTimestamp(1);
				System.out.println(dateTime);
	        }
		
		} catch (IOException ioe) {
			RositaLogger.log(true, "ERROR|||" + ioe.getMessage());
			RositaLogger.error(ioe);
			RositaLogger.log(false, ioe.getMessage());
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
		return dateTime;
	}

}
