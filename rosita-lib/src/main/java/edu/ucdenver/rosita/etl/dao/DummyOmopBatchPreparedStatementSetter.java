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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import edu.ucdenver.rosita.etl.omop.DummyOmop;

public class DummyOmopBatchPreparedStatementSetter extends EtlBatchPreparedStatementSetter<DummyOmop> implements BatchPreparedStatementSetter {

	public DummyOmopBatchPreparedStatementSetter(Integer threshold, Long dataSourceId, Timestamp timestamp) {
		super(threshold, dataSourceId, timestamp);
	}

	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		DummyOmop d = items.get(i);
		ps.setString(1, d.getName());
		ps.setInt(2, d.getAge());
		ps.setString(3, d.getCredentials());
		ps.setLong(4, dataSourceId);
	}
	
	@Override
	public int getBatchSize() {
		return items.size();
	}

}
