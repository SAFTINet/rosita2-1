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

import edu.ucdenver.rosita.etl.omop.OmopPayerPlanPeriod;

public class OmopPayerPlanPeriodRowMapper implements RowMapper<OmopPayerPlanPeriod> {

	@Override
	public OmopPayerPlanPeriod mapRow(ResultSet rs, int rowNum) throws SQLException {
		OmopPayerPlanPeriod x = new OmopPayerPlanPeriod();
		x.setId(rs.getLong("payer_plan_period_id"));
		x.setPersonId(rs.getLong("person_id"));
		x.setPayerPlanPeriodStartDate(rs.getDate("payer_plan_period_start_date"));
		x.setPayerPlanPeriodEndDate(rs.getDate("payer_plan_period_end_date"));
		x.setPayerSourceValue(rs.getString("payer_source_value"));
		x.setPlanSourceValue(rs.getString("plan_source_value"));
		x.setFamilySourceValue(rs.getString("family_source_value"));
		x.setxGridNodeId(rs.getLong("x_grid_node_id"));
		return x;
	}

}
