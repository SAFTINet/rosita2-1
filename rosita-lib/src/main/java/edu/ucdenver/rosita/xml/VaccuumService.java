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

public class VaccuumService {
	
	DataSource ds;
	
	public VaccuumService(DataSource ds) {
		this.ds = ds;
	}
	
	public void vaccuumSchema(String schema) throws SQLException, IOException {
		
		PreparedStatement ps = null;
		Connection conn = null;
		
		try {
			initialize();
			
			List<String> tables = new ArrayList<String>();
			
			String retrieveTablesStatement = "select table_schema || '.' || table_name from information_schema.tables where table_schema = ? and table_type = 'BASE TABLE' order by table_schema;";
			String vaccuumStatement = "VACUUM VERBOSE ANALYZE ";
			conn = ds.getConnection();
			ps = conn.prepareStatement(retrieveTablesStatement);
			ps.setString(1, schema);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				tables.add(rs.getString(1));
	        }
			
			rs.close();
			ps.close();
			
			int i = 0;
			for (String table : tables) {
				RositaLogger.log(true, "STATUS|||VACCUUM|||" + i + "|||" + tables.size() + "|||" + table);
				ps = conn.prepareStatement(vaccuumStatement + table);
				ps.executeUpdate();
				i++;
			}
		}
		finally {
			ps.close();
			conn.close();
		}
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
}
