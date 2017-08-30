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

import edu.ucdenver.rosita.etl.omop.OmopConditionOccurrence;
import edu.ucdenver.rosita.utils.DifferenceStopwatch;

public class ConditionOccurrenceOmopCache extends OmopCache<OmopConditionOccurrence> {
	
	public ConditionOccurrenceOmopCache(DataSource ds, Integer threshold, Long gridNodeId) throws SQLException {
		super(ds, threshold, "OmopConditionOccurrence", gridNodeId);
	}

	public void setArguments(OmopConditionOccurrence x) throws SQLException {
		lastId = x.getId();
		//System.out.println(DifferenceStopwatch.getInstance().getTimeAndDiff() + "Loading ID " + lastId);
		ps.setLong(1, x.getId());
		ps.setString(2, x.getxDataSourceType());
		ps.setLong(3, x.getPersonId());
		ps.setLong(4, x.getConditionConceptId());
		ps.setString(5, x.getConditionSourceValue());
		ps.setString(6, x.getxConditionSourceDesc());
		ps.setDate(7, new java.sql.Date(x.getConditionStartDate().getTime()));
		if (x.getxConditionUpdateDate() != null) {
			ps.setDate(8, new java.sql.Date(x.getxConditionUpdateDate().getTime()));
		}
		else {
			ps.setDate(8, null);
		}
		
		if (x.getConditionEndDate() != null) {
			ps.setDate(9, new java.sql.Date(x.getConditionEndDate().getTime()));
		}
		else {
			ps.setDate(9, null);
		}
		
		ps.setLong(10, x.getConditionTypeConceptId());
		ps.setString(11, x.getStopReason());

		if (x.getAssociatedProviderId() == null || x.getAssociatedProviderId() == 0) {
			ps.setNull(12, Types.BIGINT);
		}
		else {
			ps.setLong(12, x.getAssociatedProviderId());
		}

		if (x.getVisitOccurrenceId() == null || x.getVisitOccurrenceId() == 0) {
			ps.setNull(13, Types.BIGINT);
		}
		else {
			ps.setLong(13, x.getVisitOccurrenceId());
		}

		ps.setLong(14, gridNodeId);

	}
}
