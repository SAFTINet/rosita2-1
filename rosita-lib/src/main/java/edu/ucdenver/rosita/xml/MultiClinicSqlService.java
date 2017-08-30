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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;

import edu.ucdenver.rosita.utils.RositaLogger;
import edu.ucdenver.rosita.utils.SqlTemplates;

public class MultiClinicSqlService {
	
	JdbcTemplate jdbc = null;
	static String mcFields = "x_data_source_id, data_source_directory, data_source_name, file_type, schema_path, first_row_type, delimiter, quote_character, incremental, etl_rules_file, data_source_type, linkage_field, linkage_type";
	
	public MultiClinicSqlService(DataSource ds) {
		this.jdbc = new JdbcTemplate(ds);
	}
	
	public List<MultiClinic> getActiveMultiClinics() {
		String statement = "SELECT " + mcFields + " FROM cz.cz_data_source WHERE active = true";
		List<MultiClinic> mcs = null;
		try {
			mcs = jdbc.query(statement, new Object[0], new MultiClinicRowMapper());
		}
		catch (Exception e) {
			RositaLogger.error(e);
			e.printStackTrace(System.out);
		}
		return mcs;
	}
	
	public MultiClinic getMultiClinic(Long id) {
		String statement = "SELECT " + mcFields + " FROM cz.cz_data_source WHERE x_data_source_id = ? AND active = true";
		List<MultiClinic> mcs = null;
		try {
			mcs = jdbc.query(statement, new Object[] {id}, new MultiClinicRowMapper());
		}
		catch (Exception e) {
			RositaLogger.error(e);
			e.printStackTrace(System.out);
		}
		if (mcs != null && mcs.size() > 0) {
			return mcs.get(0);
		}
		else {
			return null;
		}
	}
	
	public List<MultiClinic> getDisabledClinics() {
		String statement = "SELECT " + mcFields + " FROM cz.cz_data_source WHERE active = false";
		List<MultiClinic> mcs = null;
		try {
			mcs = jdbc.query(statement, new Object[0], new MultiClinicRowMapper());
		}
		catch (Exception e) {
			RositaLogger.error(e);
			e.printStackTrace(System.out);
		}
		return mcs;
	}
	
	public List<MultiClinic> getSuccessfulClinics(Long jobId) {
		return getSuccessfulClinics(jobId, null);
	}
	
	public List<MultiClinic> getSuccessfulClinics(Long jobId, Long stepNumber) {
		String sql = "SELECT step_id, job_id, step_number FROM cz.step LEFT JOIN cz.step_description ON cz.step.step_description_id = cz.step_description.step_description_id WHERE job_id = ? ORDER BY step_id DESC";
		
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = jdbc.getDataSource().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, jobId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			int workflowStep = rs.getInt(3);
			//workflowStep is now the type of workflow step most recently executed (the current one). Step along until we find the previously executed one - or the one with the workflow step we're targeting.
			if (stepNumber == null) {
				while (rs.getInt(3) == workflowStep) {
					rs.next();
				}
			}
			else {
				while (rs.getInt(3) != stepNumber) {
					rs.next();
				}
			}
			//Now we're looking at the row with the right stepId. Query the multiclinic status table with this step ID, and retrieve the successful ones.
			Long stepId = rs.getLong(1);
			
			String mcSql = "SELECT cz.cz_data_source.x_data_source_id, data_source_directory, data_source_name, file_type, schema_path, first_row_type, delimiter, quote_character, incremental, etl_rules_file, data_source_type, linkage_field, linkage_type FROM cz.cz_clinic_status LEFT JOIN cz.cz_data_source ON cz.cz_clinic_status.x_data_source_id = cz.cz_data_source.x_data_source_id WHERE cz.cz_clinic_status.workflow_step_id = ? AND cz.cz_clinic_status.success = true";
			List<MultiClinic> mcs = jdbc.query(mcSql, new Object[] {stepId}, new MultiClinicRowMapper());
			
			return mcs;
		}
		catch (SQLException e) {
			RositaLogger.log(true, "ERROR|||" + e.getMessage());
			RositaLogger.error(e);
			RositaLogger.log(false, e.getMessage());
			System.exit(1);
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
		return new ArrayList<MultiClinic>();
	}
	
	public void saveMultiClinicStatus(Long jobId, Long stepId, Long dataSourceId, boolean success) {
		String sql = "INSERT INTO cz.cz_clinic_status(job_id, workflow_step_id, x_data_source_id, success) VALUES (?,?,?,?)";
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = jdbc.getDataSource().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, jobId);
			ps.setLong(2, stepId);
			ps.setLong(3, dataSourceId);
			ps.setBoolean(4, success);
			ps.execute();
		}
		catch (SQLException e) {
			RositaLogger.log(true, "ERROR|||" + e.getMessage());
			RositaLogger.error(e);
			RositaLogger.log(false, e.getMessage());
			System.exit(1);
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
