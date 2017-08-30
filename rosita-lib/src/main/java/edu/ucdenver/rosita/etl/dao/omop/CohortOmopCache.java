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
import java.util.Date;

import javax.sql.DataSource;

import edu.ucdenver.rosita.etl.omop.OmopCohort;
import edu.ucdenver.rosita.etl.omop.OmopPerson;
import edu.ucdenver.rosita.etl.omop.OmopXCohortMetadata;
import edu.ucdenver.rosita.utils.DifferenceStopwatch;

public class CohortOmopCache extends OmopCache<OmopCohort> {
	
	public CohortOmopCache(DataSource ds, Integer threshold, Long gridNodeId) throws SQLException {
		super(ds, threshold, "OmopCohort", gridNodeId);
	}

	public void setArguments(OmopCohort x) throws SQLException {
		lastId = x.getId();
		//System.out.println(DifferenceStopwatch.getInstance().getTimeAndDiff() + "Loading ID " + lastId);
		ps.setLong(1, x.getId());
		ps.setString(2, x.getxDataSource());
		ps.setLong(3, x.getCohortConceptId());
		ps.setDate(4, new java.sql.Date(x.getCohortStartDate().getTime()));
		ps.setDate(5, new java.sql.Date(x.getCohortEndDate().getTime()));
		ps.setLong(6, x.getPersonId());
		ps.setString(7, x.getStopReason());
		ps.setLong(8, x.getxCohortMetadataId());
		ps.setLong(9, gridNodeId);
	}
}
