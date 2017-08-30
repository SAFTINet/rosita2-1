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

package edu.ucdenver.rosita.etl.dao.omop;

import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import edu.ucdenver.rosita.etl.omop.OmopProvider;
import edu.ucdenver.rosita.utils.DifferenceStopwatch;

public class ProviderOmopCache extends OmopCache<OmopProvider> {
	
	public ProviderOmopCache(DataSource ds, Integer threshold, Long gridNodeId) throws SQLException {
		super(ds, threshold, "OmopProvider", gridNodeId);
	}

	public void setArguments(OmopProvider x) throws SQLException {
		lastId = x.getId();
		//System.out.println(DifferenceStopwatch.getInstance().getTimeAndDiff() + "Loading ID " + lastId);
		ps.setLong(1, x.getId());
		ps.setString(2, x.getProviderSourceValue());
		ps.setString(3, x.getxDataSourceType());
		ps.setString(4, x.getNpi());
		ps.setString(5, x.getDea());
		ps.setLong(6, x.getSpecialtyConceptId());
		ps.setString(7, x.getSpecialtySourceValue());
		ps.setString(8, x.getxProviderFirst());
		ps.setString(9, x.getxProviderMiddle());
		ps.setString(10, x.getxProviderLast());

		if (x.getCareSiteId() == null || x.getCareSiteId() == 0) {
			ps.setNull(11, Types.BIGINT);
		}
		else {
			ps.setLong(11, x.getCareSiteId());
		}

		ps.setLong(12, x.getxOrganizationId());
		ps.setLong(13, gridNodeId);
	}
}
