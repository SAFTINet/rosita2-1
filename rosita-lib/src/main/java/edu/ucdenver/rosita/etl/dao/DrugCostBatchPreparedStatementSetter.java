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

import edu.ucdenver.rosita.etl.DrugCost;

public class DrugCostBatchPreparedStatementSetter extends EtlBatchPreparedStatementSetter<DrugCost> implements BatchPreparedStatementSetter {
	
	public DrugCostBatchPreparedStatementSetter(Integer threshold, Long dataSourceId, Timestamp timestamp) {
		super(threshold, dataSourceId, timestamp);
	}

	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		DrugCost d = items.get(i);
		//ps.setString(1, d.getId());
		ps.setString(1, d.getAverageWholesalePrice());
		ps.setString(2, d.getDispensingFee());
		ps.setString(3, d.getDrugCostSourceIdentifier());
		ps.setString(4, d.getDrugExposureId());
		ps.setString(5, d.getDrugExposureSourceIdentifier());
		ps.setString(6, d.getIngredientCost());
		ps.setString(7, d.getPaidByCoordinationBenefits());
		ps.setString(8, d.getPaidByPayer());
		ps.setString(9, d.getPaidCoinsurance());
		ps.setString(10, d.getPaidCopay());
		ps.setString(11, d.getPaidTowardDeductible());
		ps.setString(12, d.getPayerPlanPeriodSourceIdentifier());
		ps.setString(13, d.getTotalOutOfPocket());
		ps.setString(14, d.getTotalPaid());
		ps.setLong(15, dataSourceId);
		ps.setTimestamp(16,etlDate);
		ps.setLong(17, new Long(d.getId()));
	}
	
	@Override
	public int getBatchSize() {
		return items.size();
	}

}
