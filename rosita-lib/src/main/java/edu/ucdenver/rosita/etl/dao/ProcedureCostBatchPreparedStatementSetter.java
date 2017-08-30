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

import edu.ucdenver.rosita.etl.ProcedureCost;

public class ProcedureCostBatchPreparedStatementSetter extends EtlBatchPreparedStatementSetter<ProcedureCost> implements BatchPreparedStatementSetter {
	
	public ProcedureCostBatchPreparedStatementSetter(Integer threshold, Long dataSourceId, Timestamp timestamp) {
		super(threshold, dataSourceId, timestamp);
	}

	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		ProcedureCost p = items.get(i);
		ps.setString(1, p.getId());
		ps.setString(2, p.getDiseaseClassConceptId());
		ps.setString(3, p.getDiseaseClassSourceValue());
		ps.setDouble(4, p.getPaidByCoordinationBenefits());
		ps.setDouble(5, p.getPaidByPayer());
		ps.setDouble(6, p.getPaidCoinsurance());
		ps.setDouble(7, p.getPaidCopay());
		ps.setDouble(8, p.getPaidTowardDeductible());
		ps.setString(9, p.getPayerPlanPeriodSourceIdentifier());
		ps.setString(10, p.getProcedureCostSourceIdentifier());
		ps.setString(11, p.getProcedureOccurrenceId());
		ps.setString(12, p.getProcedureOccurrenceSourceIdentifier());		
		ps.setString(13, p.getRevenueCodeConceptId());
		ps.setString(14, p.getRevenueCodeSourceValue());
		ps.setDouble(15, p.getTotalOutOfPocket());
		ps.setDouble(16, p.getTotalPaid());
		ps.setLong(17, dataSourceId);
		ps.setTimestamp(18,etlDate);
		ps.setLong(19, new Long(p.getId()));
	}
	
	@Override
	public int getBatchSize() {
		return items.size();
	}

}
