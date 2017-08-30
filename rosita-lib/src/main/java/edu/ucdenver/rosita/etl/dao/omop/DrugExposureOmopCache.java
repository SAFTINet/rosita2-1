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

import edu.ucdenver.rosita.etl.omop.OmopDrugExposure;
import edu.ucdenver.rosita.utils.DifferenceStopwatch;

public class DrugExposureOmopCache extends OmopCache<OmopDrugExposure> {
	
	public DrugExposureOmopCache(DataSource ds, Integer threshold, Long gridNodeId) throws SQLException {
		super(ds, threshold, "OmopDrugExposure", gridNodeId);
	}

	public void setArguments(OmopDrugExposure x) throws SQLException {
		lastId = x.getId();
		//System.out.println(DifferenceStopwatch.getInstance().getTimeAndDiff() + "Loading ID " + lastId);
		ps.setLong(1, x.getId());
		ps.setString(2, x.getxDataSourceType());
		ps.setLong(3, x.getPersonId());
		ps.setLong(4, x.getDrugConceptId());
		ps.setString(5, x.getDrugSourceValue());
		ps.setDate(6, new java.sql.Date(x.getDrugExposureStartDate().getTime()));
		if (x.getDrugExposureEndDate() != null) {
			ps.setDate(7, new java.sql.Date(x.getDrugExposureEndDate().getTime()));
		}
		else {
			ps.setDate(7, null);
		}
		ps.setLong(8, x.getDrugTypeConceptId());
		ps.setString(9, x.getStopReason());
		ps.setLong(10, x.getRefills());
		ps.setDouble(11, x.getQuantity());
		ps.setLong(12, x.getDaysSupply());
		ps.setString(13, x.getxDrugName());
		ps.setString(14, x.getxDrugStrength());
		ps.setString(15, x.getSig());
		
		if (x.getPrescribingProviderId() == null || x.getPrescribingProviderId() == 0) {
			ps.setNull(16, Types.BIGINT);
		}
		else {
			ps.setLong(16, x.getPrescribingProviderId());
		}
		
		if (x.getVisitOccurrenceId() == null || x.getVisitOccurrenceId() == 0) {
			ps.setNull(17, Types.BIGINT);
		}
		else {
			ps.setLong(17, x.getVisitOccurrenceId());
		}
		
		ps.setLong(18, x.getRelevantConditionConceptId());
		ps.setLong(19, gridNodeId);
	}
}
