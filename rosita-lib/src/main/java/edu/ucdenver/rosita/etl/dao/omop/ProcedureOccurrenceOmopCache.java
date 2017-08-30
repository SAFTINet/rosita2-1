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

import edu.ucdenver.rosita.etl.omop.OmopProcedureOccurrence;
import edu.ucdenver.rosita.utils.DifferenceStopwatch;

public class ProcedureOccurrenceOmopCache extends OmopCache<OmopProcedureOccurrence> {
	
	public ProcedureOccurrenceOmopCache(DataSource ds, Integer threshold, Long gridNodeId) throws SQLException {
		super(ds, threshold, "OmopProcedureOccurrence", gridNodeId);
	}

	public void setArguments(OmopProcedureOccurrence x) throws SQLException {
		lastId = x.getId();
		//System.out.println(DifferenceStopwatch.getInstance().getTimeAndDiff() + "Loading ID " + lastId);
		ps.setLong(1, x.getId());
		ps.setString(2, x.getxDataSourceType());
		ps.setLong(3, x.getPersonId());
		ps.setLong(4, x.getProcedureConceptId());
		ps.setString(5, x.getProcedureSourceValue());
		ps.setDate(6, new java.sql.Date(x.getProcedureDate().getTime()));
		ps.setLong(7, x.getProcedureTypeConceptId());

		if (x.getAssociatedProviderId() == null || x.getAssociatedProviderId() == 0) {
			ps.setNull(8, Types.BIGINT);
		}
		else {
			ps.setLong(8, x.getAssociatedProviderId());
		}
		
		if (x.getVisitOccurrenceId() == null || x.getVisitOccurrenceId() == 0) {
			ps.setNull(9, Types.BIGINT);
		}
		else {
			ps.setLong(9, x.getVisitOccurrenceId());
		}
		
		ps.setLong(10, x.getRelevantConditionConceptId());
		ps.setLong(11, gridNodeId);
	}
}
