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

import edu.ucdenver.rosita.etl.Provider;

public class ProviderBatchPreparedStatementSetter extends EtlBatchPreparedStatementSetter<Provider> implements BatchPreparedStatementSetter {
	
	public ProviderBatchPreparedStatementSetter(Integer threshold, Long dataSourceId, Timestamp timestamp) {
		super(threshold, dataSourceId, timestamp);
	}

	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		Provider p = items.get(i);
//		ps.setString(1, p.getId());
//		ps.setString(2, p.getCareSiteId());
		ps.setString(1, p.getCareSiteSourceValue());
		ps.setString(2, p.getDea());
		ps.setString(3, p.getNpi());
		ps.setString(4, p.getProviderSourceValue());
		ps.setString(5, p.getSpecialtySourceValue());
		ps.setString(6, p.getxDataSourceType());
		ps.setString(7, p.getxOrganizationSourceValue());
		ps.setString(8, p.getxProviderFirst());
		ps.setString(9, p.getxProviderLast());
		ps.setString(10, p.getxProviderMiddle());
		ps.setLong(11, dataSourceId);
		ps.setTimestamp(12,etlDate);
		ps.setLong(13, new Long(p.getId()));
	}
	
	@Override
	public int getBatchSize() {
		return items.size();
	}

}
