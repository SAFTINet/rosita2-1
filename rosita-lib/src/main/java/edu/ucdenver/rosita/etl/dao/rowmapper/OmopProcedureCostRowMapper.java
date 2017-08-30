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

import edu.ucdenver.rosita.etl.omop.OmopProcedureCost;

public class OmopProcedureCostRowMapper implements RowMapper<OmopProcedureCost> {

	@Override
	public OmopProcedureCost mapRow(ResultSet rs, int rowNum) throws SQLException {
		OmopProcedureCost x = new OmopProcedureCost();
		x.setId(rs.getLong("procedure_cost_id"));
		x.setProcedureOccurrenceId(rs.getLong("procedure_occurrence_id"));
		x.setPaidCopay(rs.getDouble("paid_copay"));
		x.setPaidCoinsurance(rs.getDouble("paid_coinsurance"));
		x.setPaidTowardDeductible(rs.getDouble("paid_toward_deductible"));
		x.setPaidByPayer(rs.getDouble("paid_by_payer"));
		x.setPaidByCoordinationBenefits(rs.getDouble("paid_by_coordination_benefits"));
		x.setTotalOutOfPocket(rs.getDouble("total_out_of_pocket"));
		x.setTotalPaid(rs.getDouble("total_paid"));
		x.setDiseaseClassConceptId(rs.getLong("disease_class_concept_id"));
		x.setRevenueCodeConceptId(rs.getLong("revenue_code_concept_id"));
		x.setPayerPlanPeriodId(rs.getLong("payer_plan_period_id"));
		x.setDiseaseClassSourceValue(rs.getString("disease_class_source_value"));
		x.setRevenueCodeSourceValue(rs.getString("revenue_code_source_value"));
		x.setxGridNodeId(rs.getLong("x_grid_node_id"));
		return x;
	}

}
