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

import edu.ucdenver.rosita.etl.omop.OmopObservation;

public class OmopObservationRowMapper implements RowMapper<OmopObservation> {

	@Override
	public OmopObservation mapRow(ResultSet rs, int rowNum) throws SQLException {
		OmopObservation x = new OmopObservation();
		x.setId(rs.getLong("observation_id"));
		x.setPersonId(rs.getLong("person_id"));
		x.setObservationConceptId(rs.getLong("observation_concept_id"));
		x.setObservationDate(rs.getDate("observation_date"));
		x.setObservationTime(rs.getDate("observation_time"));
		x.setValueAsNumber(rs.getDouble("value_as_number"));
		x.setValueAsString(rs.getString("value_as_string"));
		x.setValueAsConceptId(rs.getLong("value_as_concept_id"));
		x.setUnitConceptId(rs.getLong("unit_concept_id"));
		x.setRangeLow(rs.getDouble("range_low"));
		x.setRangeHigh(rs.getDouble("range_high"));
		x.setObservationTypeConceptId(rs.getLong("observation_type_concept_id"));
		x.setAssociatedProviderId(rs.getLong("associated_provider_id"));
		x.setVisitOccurrenceId(rs.getLong("visit_occurrence_id"));
		x.setRelevantConditionConceptId(rs.getLong("relevant_condition_concept_id"));
		x.setObservationSourceValue(rs.getString("observation_source_value"));
		x.setUnitSourceValue(rs.getString("unit_source_value"));
		x.setxDataSourceType(rs.getString("x_data_source_type"));
		x.setxObsComment(rs.getString("x_obs_comment"));
		x.setxGridNodeId(rs.getLong("x_grid_node_id"));
		return x;
	}

}
