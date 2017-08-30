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

import edu.ucdenver.rosita.etl.omop.OmopDrugExposure;

public class OmopDrugExposureRowMapper implements RowMapper<OmopDrugExposure> {

	@Override
	public OmopDrugExposure mapRow(ResultSet rs, int rowNum) throws SQLException {
		OmopDrugExposure x = new OmopDrugExposure();
		x.setId(rs.getLong("drug_exposure_id"));
		x.setPersonId(rs.getLong("person_id"));
		x.setDrugConceptId(rs.getLong("drug_concept_id"));
		x.setDrugExposureStartDate(rs.getDate("drug_exposure_start_date"));
		x.setDrugExposureEndDate(rs.getDate("drug_exposure_end_date"));
		x.setDrugTypeConceptId(rs.getLong("drug_type_concept_id"));
		x.setStopReason(rs.getString("stop_reason"));
		x.setRefills(rs.getLong("refills"));
		x.setQuantity(rs.getDouble("quantity"));
		x.setDaysSupply(rs.getLong("days_supply"));
		x.setSig(rs.getString("sig"));
		x.setPrescribingProviderId(rs.getLong("prescribing_provider_id"));
		x.setVisitOccurrenceId(rs.getLong("visit_occurrence_id"));
		x.setRelevantConditionConceptId(rs.getLong("relevant_condition_concept_id"));
		x.setxDataSourceType(rs.getString("x_data_source_type"));
		x.setDrugSourceValue(rs.getString("drug_source_value"));
		x.setxDrugName(rs.getString("x_drug_name"));
		x.setxDrugStrength(rs.getString("x_drug_strength"));
		x.setxGridNodeId(rs.getLong("x_grid_node_id"));
		return x;
	}

}
