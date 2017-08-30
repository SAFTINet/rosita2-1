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

import edu.ucdenver.rosita.etl.VisitOccurrence;

public class VisitOccurrenceBatchPreparedStatementSetter extends EtlBatchPreparedStatementSetter<VisitOccurrence> implements BatchPreparedStatementSetter {

	public VisitOccurrenceBatchPreparedStatementSetter(Integer threshold, Long dataSourceId, Timestamp timestamp) {
		super(threshold, dataSourceId, timestamp);
	}

	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		VisitOccurrence v = items.get(i);
		//ps.setString(1, v.getId());
		ps.setString(1, v.getCareSiteSourceValue());
		//ps.setString(3, v.getDemographicId());
		ps.setString(2, v.getPersonSourceValue());
		ps.setString(3, v.getPlaceOfServiceSourceValue());
		ps.setString(4, v.getVisitEndDate());
		ps.setString(5, v.getxVisitOccurrenceSourceIdentifier());
		ps.setString(6, v.getVisitStartDate());
		ps.setString(7, v.getxDataSourceType());
		ps.setString(8, v.getxProviderSourceValue());
		ps.setLong(9, dataSourceId);
		ps.setTimestamp(10,etlDate);
		ps.setLong(11, new Long(v.getId()));
	}
	
	@Override
	public int getBatchSize() {
		return items.size();
	}

}
