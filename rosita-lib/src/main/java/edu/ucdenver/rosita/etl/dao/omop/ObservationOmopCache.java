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

import edu.ucdenver.rosita.etl.omop.OmopObservation;
import edu.ucdenver.rosita.utils.DifferenceStopwatch;
import edu.ucdenver.rosita.utils.RositaLogger;

public class ObservationOmopCache extends OmopCache<OmopObservation> {
	
	public ObservationOmopCache(DataSource ds, Integer threshold, Long gridNodeId) throws SQLException {
		super(ds, threshold, "OmopObservation", gridNodeId);
	}

	public void setArguments(OmopObservation x) throws SQLException {
		lastId = x.getId();
		//System.out.println(DifferenceStopwatch.getInstance().getTimeAndDiff() + "Loading ID " + lastId);
		ps.setLong(1, x.getId());
		ps.setString(2, x.getxDataSourceType());
		ps.setLong(3, x.getPersonId());
		ps.setLong(4, x.getObservationConceptId());
		ps.setString(5, x.getObservationSourceValue());
		if (x.getObservationDate() != null) {
			ps.setDate(6, new java.sql.Date(x.getObservationDate().getTime()));
		}
		else {
			ps.setDate(6, null);
		}
		if (x.getObservationTime() != null) {
			ps.setDate(7, new java.sql.Date(x.getObservationTime().getTime()));
		}
		else {
			ps.setDate(7, null);
		}
		ps.setDouble(8, x.getValueAsNumber());
		ps.setString(9, x.getValueAsString());
		ps.setLong(10, x.getValueAsConceptId());
		ps.setLong(11, x.getUnitConceptId());
		ps.setString(12, x.getUnitSourceValue());
		ps.setDouble(13, x.getRangeLow());
		ps.setDouble(14, x.getRangeHigh());
		ps.setLong(15, x.getObservationTypeConceptId());

		// UCDR-7: domain object stores null long values as 0, so need to set as null parameter to avoid FK constraint issues. 
		if (x.getAssociatedProviderId() == null || x.getAssociatedProviderId() == 0) {
			ps.setNull(16, Types.BIGINT);
		}
		else {
			ps.setLong(16, x.getAssociatedProviderId());
		}

		if (x.getVisitOccurrenceId() == null || x.getVisitOccurrenceId() == 0) {
			ps.setNull(17, Types.BIGINT);
		}
		else {
			ps.setLong(17, x.getVisitOccurrenceId());
		}

		ps.setLong(18, x.getRelevantConditionConceptId());
		ps.setString(19, x.getxObsComment());
		ps.setLong(20, gridNodeId);
	}
}
