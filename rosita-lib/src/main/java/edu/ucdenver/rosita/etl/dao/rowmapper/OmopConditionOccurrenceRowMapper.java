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

import edu.ucdenver.rosita.etl.omop.OmopConditionOccurrence;

public class OmopConditionOccurrenceRowMapper implements RowMapper<OmopConditionOccurrence> {

	@Override
	public OmopConditionOccurrence mapRow(ResultSet rs, int rowNum) throws SQLException {
		OmopConditionOccurrence x = new OmopConditionOccurrence();
		x.setId(rs.getLong("condition_occurrence_id"));
		x.setPersonId(rs.getLong("person_id"));
		x.setConditionConceptId(rs.getLong("condition_concept_id"));
		x.setConditionStartDate(rs.getDate("condition_start_date"));
		x.setConditionEndDate(rs.getDate("condition_end_date"));
		x.setConditionTypeConceptId(rs.getLong("condition_type_concept_id"));
		x.setStopReason(rs.getString("stop_reason"));
		x.setAssociatedProviderId(rs.getLong("associated_provider_id"));
		x.setVisitOccurrenceId(rs.getLong("visit_occurrence_id"));
		x.setConditionSourceValue(rs.getString("condition_source_value"));
		x.setxDataSourceType(rs.getString("x_data_source_type"));
		x.setxConditionSourceDesc(rs.getString("x_condition_source_desc"));
		x.setxConditionUpdateDate(rs.getDate("x_condition_update_date"));
		x.setxGridNodeId(rs.getLong("x_grid_node_id"));
		return x;
	}

}
