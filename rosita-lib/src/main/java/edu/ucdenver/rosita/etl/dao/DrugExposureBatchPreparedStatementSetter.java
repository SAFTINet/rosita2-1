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

import edu.ucdenver.rosita.etl.DrugExposure;

public class DrugExposureBatchPreparedStatementSetter extends EtlBatchPreparedStatementSetter<DrugExposure> implements BatchPreparedStatementSetter {
	
	public DrugExposureBatchPreparedStatementSetter(Integer threshold, Long dataSourceId, Timestamp timestamp) {
		super(threshold, dataSourceId, timestamp);
	}

	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		DrugExposure d = items.get(i);
		//ps.setString(1, d.getId());
		ps.setString(1, d.getDaysSupply());
		//ps.setString(2, d.getDemographicId());
		ps.setString(2, d.getDrugExposureEndDate());
		ps.setString(3, d.getDrugExposureSourceIdentifier());
		ps.setString(4, d.getDrugExposureStartDate());
		ps.setString(5, d.getDrugSourceValue());
		ps.setString(6, d.getDrugSourceValueVocabulary());
		ps.setString(7, d.getDrugTypeSourceValue());
		ps.setString(8, d.getPersonSourceValue());
		ps.setString(9, d.getProviderSourceValue());
		ps.setString(10, d.getQuantity());
		ps.setString(11, d.getRefills());
		ps.setString(12, d.getRelevantConditionSourceValue());
		ps.setString(13, d.getSig());
		ps.setString(14, d.getStopReason());
		ps.setString(15, d.getxVisitOccurrenceSourceIdentifier());
		ps.setString(16, d.getxDataSourceType());
		ps.setString(17, d.getxDrugName());
		ps.setString(18, d.getxDrugStrength());
		ps.setLong(19, dataSourceId);
		ps.setTimestamp(20,etlDate);
		ps.setLong(21, new Long(d.getId()));
	}
	
	@Override
	public int getBatchSize() {
		return items.size();
	}

}
