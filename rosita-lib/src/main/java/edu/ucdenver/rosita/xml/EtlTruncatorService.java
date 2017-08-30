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

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import java.sql.Connection;

import edu.ucdenver.rosita.ArgHandler;
import edu.ucdenver.rosita.utils.ParsingService;
import edu.ucdenver.rosita.utils.RositaLogger;
import edu.ucdenver.rosita.utils.SqlTemplates;
import edu.ucdenver.rosita.utils.types.SchemaColumn;

public class EtlTruncatorService {
	
	DataSource ds;
	
	public EtlTruncatorService(DataSource ds) {
		this.ds = ds;
	}
	
//	public void truncate() throws SQLException {
//		List<String> truncateStatements = new ArrayList<String>(); //TODO Create truncate statements from schema
//		
//		Connection conn = ds.getConnection();
//		int statements = 0;
//		for (String statement : truncateStatements) {
//			PreparedStatement ps = conn.prepareStatement(statement);
//			ps.execute();
//			ps.close();
//			statements++;
//			RositaLogger.log(true, "STATUS|||TRUNCATE|||" + String.valueOf(statements) + "|||1|||1|||1");
//		}
//		conn.close();
//	}
	
	public void truncate(MultiClinic clinic, Integer clinicNum, Integer maxClinics, boolean cleanup) throws FileNotFoundException, Exception {
		List<String> deleteStatements = new ArrayList<String>();
		if (clinic.getSchemaPath() != null) {
			Map<String, ArrayList<SchemaColumn>> schema = null;
			if ("xml".equals(clinic.getFileType()) || clinic.getSchemaPath().endsWith(".xsd")) { //Necessary this is - null may the file type be
				schema = ParsingService.getXmlDefaultSchemaLayout();
			}
			else {
				try {
					schema = ParsingService.getSchemaLayoutFromPath(ArgHandler.getArg("folder.schemas") + clinic.getSchemaPath(), false);
				}	
				catch (FileNotFoundException e) {
					if (cleanup) {
						//Notify cleanup didn't happen - just a normal message
						RositaLogger.log(true, "Did not clean up disabled clinic " + clinic.getDataSourceDirectory() + ": " + e.getMessage());
						return;
					}
					else {
						throw e;
					}
				}
			}
			
			for (String table : schema.keySet()) {
				deleteStatements.add("DELETE FROM raw." + table.toLowerCase().replace(" ", "_") + " WHERE x_data_source_id = " + clinic.getDataSourceId() + ";");
			}
			
			String task = "TRUNCATE";
			if (cleanup) {
				task = "CLEANUP";
			}
			
			Connection conn = ds.getConnection();
			PreparedStatement ps = null;
			try {
				
				int statements = 0;
				for (String statement : deleteStatements) {
					RositaLogger.log(true, statement);
					ps = conn.prepareStatement(statement.replace("_datasourceid_", clinic.getDataSourceId().toString()));
					ps.execute();
					ps.close();
					statements++;
					RositaLogger.log(true, "STATUS|||" + task + "|||" + String.valueOf(statements) + "|||" + clinic.getDataSourceId() + "|||" + clinicNum + "|||" + maxClinics);
				}
				conn.close();
			}
			catch (Exception e) {
				if (cleanup) {
					//Notify cleanup didn't happen - just a normal message
					RositaLogger.log(true, "Did not clean up disabled clinic " + clinic.getDataSourceDirectory() + ": " + e.getMessage());
					return;
				}
				else {
					throw e;
				}
			}
			finally {
				ps.close();
				conn.close();
			}
		}
	}

}
