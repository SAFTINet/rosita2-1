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

import edu.ucdenver.rosita.etl.Demographic;

public class DemographicBatchPreparedStatementSetter extends EtlBatchPreparedStatementSetter<Demographic> implements BatchPreparedStatementSetter {
	
	public DemographicBatchPreparedStatementSetter(Integer threshold, Long dataSourceId, Timestamp timestamp) {
		super(threshold, dataSourceId, timestamp);
	}

	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		Demographic d = items.get(i);
		//ps.setString(1, d.getId());
		ps.setString(1, d.getAddress1());
		ps.setString(2, d.getAddress2());
		ps.setString(3, d.getCareSiteSourceValue());
		ps.setString(4, d.getCity());
		ps.setString(5, d.getCounty());
		ps.setString(6, d.getDayOfBirth());
		ps.setString(7, d.getEthnicitySourceValue());
		ps.setString(8, d.getFirst());
		ps.setString(9, d.getGenderSourceValue());
		ps.setString(10, d.getLast());
		ps.setString(11, d.getMedicaidIdNumber());
		ps.setString(12, d.getMiddle());
		ps.setString(13, d.getMonthOfBirth());
//		ps.setString(14, d.getOrganizationId());
		ps.setString(14, d.getPersonSourceValue());
		ps.setString(15, d.getProviderSourceValue());
		ps.setString(16, d.getRaceSourceValue());
		ps.setString(17, d.getSsn());
		ps.setString(18, d.getState());
		ps.setString(19, d.getxDataSourceType());
		ps.setString(20, d.getxOrganizationSourceValue());
		ps.setString(21, d.getYearOfBirth());
		ps.setString(22, d.getZip());
		ps.setLong(23, dataSourceId);
		ps.setTimestamp(24,etlDate);
		ps.setLong(25, new Long(d.getId()));
	}
	
	@Override
	public int getBatchSize() {
		return items.size();
	}

}
