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

import edu.ucdenver.rosita.etl.CareSite;

public class CareSiteBatchPreparedStatementSetter extends EtlBatchPreparedStatementSetter<CareSite> implements BatchPreparedStatementSetter {
	
	public CareSiteBatchPreparedStatementSetter(Integer threshold, Long dataSourceId, Timestamp timestamp) {
		super(threshold, dataSourceId, timestamp);
	}

	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		CareSite c = items.get(i);
		//ps.setString(1, c.getId());
		ps.setString(1, c.getCareSiteAddress1());
		ps.setString(2, c.getCareSiteAddress2());
		ps.setString(3, c.getCareSiteCity());
		ps.setString(4, c.getCareSiteCounty());
		ps.setString(5, c.getCareSiteSourceValue());
		ps.setString(6, c.getCareSiteState());
		ps.setString(7, c.getCareSiteZip());
		//ps.setString(8, c.getOrganizationId());
		ps.setString(8, c.getOrganizationSourceValue());
		ps.setString(9, c.getPlaceOfServiceSourceValue());
		ps.setString(10, c.getxCareSiteName());
		ps.setString(11, c.getxDataSourceType());
		ps.setLong(12, dataSourceId);
		ps.setTimestamp(13,etlDate);
		ps.setLong(14, new Long(c.getId()));
	}
	
	@Override
	public int getBatchSize() {
		return items.size();
	}

}
