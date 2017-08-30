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

import edu.ucdenver.rosita.etl.omop.OmopPerson;
import edu.ucdenver.rosita.utils.DifferenceStopwatch;

public class PersonOmopCache extends OmopCache<OmopPerson> {
	
	public PersonOmopCache(DataSource ds, Integer threshold, Long gridNodeId) throws SQLException {
		super(ds, threshold, "OmopPerson", gridNodeId);
	}

	public void setArguments(OmopPerson x) throws SQLException {
		lastId = x.getId();
		//System.out.println(DifferenceStopwatch.getInstance().getTimeAndDiff() + "Loading ID " + lastId);
		ps.setLong(1, x.getId());
		ps.setString(2, x.getPersonSourceValue());
		ps.setLong(3, x.getLocationId());
		ps.setLong(4, x.getYearOfBirth());
		ps.setLong(5, x.getMonthOfBirth());
		ps.setLong(6, x.getDayOfBirth());
		ps.setLong(7, x.getGenderConceptId());
		ps.setString(8, x.getGenderSourceValue());
		ps.setLong(9, x.getRaceConceptId());
		ps.setString(10, x.getRaceSourceValue());
		ps.setLong(11, x.getEthnicityConceptId());
		ps.setString(12, x.getEthnicitySourceValue());
		
		if (x.getProviderId() == null || x.getProviderId() == 0) {
			ps.setNull(13, Types.BIGINT);
		}
		else {
			ps.setLong(13, x.getProviderId());
		}
		
		if (x.getCareSiteId() == null || x.getCareSiteId() == 0) {
			ps.setNull(14, Types.BIGINT);
		}
		else {
			ps.setLong(14, x.getCareSiteId());
		}
		
		ps.setLong(15, x.getxOrganizationId());
		ps.setLong(16, gridNodeId);

	}
}
