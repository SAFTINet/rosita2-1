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

import edu.ucdenver.rosita.etl.omop.OmopProcedureCost;
import edu.ucdenver.rosita.utils.DifferenceStopwatch;

public class ProcedureCostOmopCache extends OmopCache<OmopProcedureCost> {
	
	public ProcedureCostOmopCache(DataSource ds, Integer threshold, Long gridNodeId) throws SQLException {
		super(ds, threshold, "OmopProcedureCost", gridNodeId);
	}

	public void setArguments(OmopProcedureCost x) throws SQLException {
		lastId = x.getId();
		//System.out.println(DifferenceStopwatch.getInstance().getTimeAndDiff() + "Loading ID " + lastId);
		ps.setLong(1, x.getId());
		ps.setLong(2, x.getProcedureOccurrenceId());
		ps.setDouble(3, x.getPaidCopay());
		ps.setDouble(4, x.getPaidCoinsurance());
		ps.setDouble(5, x.getPaidTowardDeductible());
		ps.setDouble(6, x.getPaidByPayer());
		ps.setDouble(7, x.getPaidByCoordinationBenefits());
		ps.setDouble(8, x.getTotalOutOfPocket());
		ps.setDouble(9, x.getTotalPaid());
		ps.setLong(10, x.getDiseaseClassConceptId());
		ps.setString(11, x.getDiseaseClassSourceValue());
		ps.setLong(12, x.getRevenueCodeConceptId());
		ps.setString(13, x.getRevenueCodeSourceValue());

		if (x.getPayerPlanPeriodId() == null || x.getPayerPlanPeriodId() == 0) {
			ps.setNull(14, Types.BIGINT);
		}
		else {
			ps.setLong(14, x.getPayerPlanPeriodId());
		}

		ps.setLong(15, gridNodeId);

	}
}
