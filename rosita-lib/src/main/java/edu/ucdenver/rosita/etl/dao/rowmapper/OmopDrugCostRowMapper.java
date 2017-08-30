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

package edu.ucdenver.rosita.etl.dao.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.ucdenver.rosita.etl.omop.OmopDrugCost;

public class OmopDrugCostRowMapper implements RowMapper<OmopDrugCost> {

	@Override
	public OmopDrugCost mapRow(ResultSet rs, int rowNum) throws SQLException {
		OmopDrugCost x = new OmopDrugCost();
		x.setId(rs.getLong("drug_cost_id"));
		x.setDrugExposureId(rs.getLong("drug_exposure_id"));
		x.setPaidCopay(rs.getDouble("paid_copay"));
		x.setPaidCoinsurance(rs.getDouble("paid_coinsurance"));
		x.setPaidTowardDeductible(rs.getDouble("paid_toward_deductible"));
		x.setPaidByPayer(rs.getDouble("paid_by_payer"));
		x.setPaidByCoordinationBenefits(rs.getDouble("paid_by_coordination_benefits"));
		x.setTotalOutOfPocket(rs.getDouble("total_out_of_pocket"));
		x.setTotalPaid(rs.getDouble("total_paid"));
		x.setIngredientCost(rs.getDouble("ingredient_cost"));
		x.setDispensingFee(rs.getDouble("dispensing_fee"));
		x.setAverageWholesalePrice(rs.getDouble("average_wholesale_price"));
		x.setPayerPlanPeriodId(rs.getLong("payer_plan_period_id"));
		x.setxGridNodeId(rs.getLong("x_grid_node_id"));
		return x;
	}

}
