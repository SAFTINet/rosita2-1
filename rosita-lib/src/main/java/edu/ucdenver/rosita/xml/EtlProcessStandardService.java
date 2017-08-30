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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

import javax.sql.DataSource;

import edu.ucdenver.rosita.utils.RositaLogger;
import edu.ucdenver.rosita.utils.SqlTemplates;

public class EtlProcessStandardService {
	
	DataSource ds;
	
	public EtlProcessStandardService(DataSource ds) {
		this.ds = ds;
	}
	
	public void process(Long jobId) throws SQLException {
		List<String> processStatements = new ArrayList<String>();
		ArrayList<String> StartMessages = new ArrayList<String>();
		ArrayList<String> EndMessages = new ArrayList<String>();
		
        processStatements.add("select cz.czx_etl_std_rule_update(-1);".replace("-1", String.valueOf(jobId)));
        StartMessages.add("Create INSERT and SELECT statements for ETL rules");
        EndMessages.add("Done creating INSERT and SELECT statements");
        
        processStatements.add("select std.stdx_truncate_std(-1);".replace("-1", String.valueOf(jobId)));
        StartMessages.add("Truncate STD tables");
        EndMessages.add("Done truncating STD tables");
       
        Connection conn = ds.getConnection();

        
        //Retrieve the ETL rules from the database
        Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select * from cz.etl_rule where target_schema='std' order by rule_order");
		
		//Generate SQL statements and message to load each rule
		while (rs.next()){
			StartMessages.add("START: Processing rule_order: "+rs.getString("rule_order")+". Rule description: "+rs.getString("rule_description"));
			EndMessages.add("END. rule_order: "+rs.getString("rule_order"));
			processStatements.add("select std.stdx_load_std_by_rule(-1,"+rs.getString("etl_rule_id")+");".replace("-1", String.valueOf(jobId)));
		}
		
		int statements = 0;
		long t1, t2;
		
		for (String processStatement : processStatements) {
			RositaLogger.log(true, StartMessages.get(statements));
			t1 = System.currentTimeMillis();
			PreparedStatement ps = conn.prepareStatement(processStatement);
			ps.execute();
			ps.close();
			t2 = System.currentTimeMillis();
			RositaLogger.log(true, EndMessages.get(statements)+". Elapsed time = "+((t2-t1))+"ms");
			statements++;
		}
		RositaLogger.log(true, "STATUS|||" + String.valueOf(statements));

		conn.close();
	}
}
