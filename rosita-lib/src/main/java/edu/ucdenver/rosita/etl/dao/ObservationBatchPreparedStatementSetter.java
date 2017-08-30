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

import edu.ucdenver.rosita.etl.Observation;

public class ObservationBatchPreparedStatementSetter extends EtlBatchPreparedStatementSetter<Observation> implements BatchPreparedStatementSetter {
	
	public ObservationBatchPreparedStatementSetter(Integer threshold, Long dataSourceId, Timestamp timestamp) {
		super(threshold, dataSourceId, timestamp);
	}

	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		Observation o = items.get(i);
		//ps.setString(1, o.getId());
		ps.setString(1, o.getAssociatedProviderSourceValue());
		//ps.setString(2, o.getDemographicId());
		ps.setString(2, o.getObservationDate());
		ps.setString(3, o.getObservationSourceIdentifier());
		ps.setString(4, o.getObservationSourceValue());
		ps.setString(5, o.getObservationSourceValueVocabulary());
		ps.setString(6, o.getObservationTime());
		ps.setString(7, o.getObservationTypeSourceValue());
		ps.setString(8, o.getPersonSourceValue());
		ps.setString(9, o.getRangeHigh());
		ps.setString(10, o.getRangeLow());
		ps.setString(11, o.getRelevantConditionSourceValue());
		ps.setString(12, o.getUnitSourceValue());
		ps.setString(13, o.getValueAsNumber());
		ps.setString(14, o.getValueAsString());
		ps.setString(15, o.getxVisitOccurrenceSourceIdentifier());
		ps.setString(16, o.getxDataSourceType());
		ps.setString(17, o.getxObsComment());
		ps.setLong(18, dataSourceId);
		ps.setTimestamp(19,etlDate);
		ps.setLong(20, new Long(o.getId()));
	}
	
	@Override
	public int getBatchSize() {
		return items.size();
	}

}
