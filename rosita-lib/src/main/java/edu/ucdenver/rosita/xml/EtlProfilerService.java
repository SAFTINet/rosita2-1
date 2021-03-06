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
import java.util.List;

import javax.sql.DataSource;

import edu.ucdenver.rosita.utils.RositaLogger;
import edu.ucdenver.rosita.utils.SqlTemplates;

public class EtlProfilerService {
	
	DataSource ds;
	
	public EtlProfilerService(DataSource ds) {
		this.ds = ds;
	}
	
	public void profile(Long jobId) throws SQLException {
		List<String> profileStatements = new SqlTemplates().profileList;
		
		Connection conn = ds.getConnection();
		int statements = 0;
		for (String statement : profileStatements) {
			if (jobId != null && jobId > 0) {
				statement = statement.replace("-1", jobId.toString());
			}
			PreparedStatement ps = conn.prepareStatement(statement);
			ps.execute();
			ps.close();
			statements++;
			RositaLogger.log(true, "STATUS|||" + String.valueOf(statements));
		}
		conn.close();
	}

}
