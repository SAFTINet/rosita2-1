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

import edu.ucdenver.rosita.etl.Cohort;

public class CohortBatchPreparedStatementSetter extends EtlBatchPreparedStatementSetter<Cohort> implements BatchPreparedStatementSetter {
	
	public CohortBatchPreparedStatementSetter(Integer threshold, Long dataSourceId, Timestamp timestamp) {
		super(threshold, dataSourceId, timestamp);
	}

	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		Cohort c = items.get(i);
		//ps.setString(1, c.getId());
		ps.setString(1, c.getCohortEndDate());
		ps.setString(2, c.getCohortSourceIdentifier());
		ps.setString(3, c.getCohortSourceValue());
		ps.setString(4, c.getCohortStartDate());
		ps.setString(5, c.getDemographicId());
		ps.setString(6, c.getStopReason());
		ps.setString(7, c.getSubjectSourceIdentifier());
		ps.setLong(8, dataSourceId);
		ps.setTimestamp(9, etlDate);
		ps.setLong(10, new Long(c.getId()));
		
	}
	
	@Override
	public int getBatchSize() {
		return items.size();
	}

}
