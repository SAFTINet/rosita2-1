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

package edu.ucdenver.rosita.etl.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import edu.ucdenver.rosita.etl.DrugCost;
import edu.ucdenver.rosita.etl.dao.omop.OmopCache;
import edu.ucdenver.rosita.etl.omop.DummyOmop;
import edu.ucdenver.rosita.utils.EtlCache;

public class DummyOmopRowCallbackHandler implements RowCallbackHandler {
	
	OmopCache cache = null;
	Integer threshold = 5000;
	Long printNextAt = threshold.longValue();
	Long rowsRead = 0L;
	
	public DummyOmopRowCallbackHandler(OmopCache cache, Integer threshold) {
		this.cache = cache;
		this.threshold = threshold;
		this.printNextAt = threshold.longValue();
	}

	@Override
	public void processRow(ResultSet rs) throws SQLException {
		DummyOmop d = new DummyOmop();
		d.setId(rs.getLong("omop_id"));
		d.setName(rs.getString("name"));
		d.setAge(rs.getInt("age"));
		d.setCredentials(rs.getString("credentials"));
		
		cache.add(d);
		rowsRead++;
		//if (rowsRead % 100 == 0) {
		//	printNextAt += threshold;
			System.out.println("Read " + rowsRead + " rows");
		//}
	}

}
