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

import edu.ucdenver.rosita.etl.Death;

public class DeathBatchPreparedStatementSetter extends EtlBatchPreparedStatementSetter<Death> implements BatchPreparedStatementSetter {
	
	public DeathBatchPreparedStatementSetter(Integer threshold, Long dataSourceId, Timestamp timestamp) {
		super(threshold, dataSourceId, timestamp);
	}

	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		Death d = items.get(i);
		//ps.setString(1, d.getId());
		ps.setString(1, d.getCauseOfDeathSourceValue());
		ps.setString(2, d.getDeathDate());
		ps.setString(3, d.getDeathTypeConceptId());
		ps.setString(4, d.getDeathTypeSourceValue());
		ps.setString(5, d.getDemographicId());
		ps.setString(6, d.getPersonSourceValue());
		ps.setLong(7, dataSourceId);
		ps.setTimestamp(8, etlDate);
		ps.setLong(9, new Long(d.getId()));
	}
	
	@Override
	public int getBatchSize() {
		return items.size();
	}

}
