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

import javax.sql.DataSource;

import edu.ucdenver.rosita.etl.omop.OmopOrganization;
import edu.ucdenver.rosita.utils.DifferenceStopwatch;

public class OrganizationOmopCache extends OmopCache<OmopOrganization> {
	
	public OrganizationOmopCache(DataSource ds, Integer threshold, Long gridNodeId) throws SQLException {
		super(ds, threshold, "OmopOrganization", gridNodeId);
	}

	public void setArguments(OmopOrganization x) throws SQLException {
		lastId = x.getId();
		//System.out.println(DifferenceStopwatch.getInstance().getTimeAndDiff() + "Loading ID " + lastId);
		ps.setLong(1, x.getId());
		ps.setString(2, x.getOrganizationSourceValue());
		ps.setString(3, x.getxDataSourceType());
		ps.setLong(4, x.getPlaceOfServiceConceptId());
		ps.setString(5, x.getPlaceOfServiceSourceValue());
		ps.setLong(6, x.getLocationId());		
		ps.setLong(7, gridNodeId);

	}
}
