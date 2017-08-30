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

import edu.ucdenver.rosita.etl.omop.OmopProcedureOccurrence;

public class OmopProcedureOccurrenceRowMapper implements RowMapper<OmopProcedureOccurrence> {

	@Override
	public OmopProcedureOccurrence mapRow(ResultSet rs, int rowNum) throws SQLException {
		OmopProcedureOccurrence x = new OmopProcedureOccurrence();
		x.setId(rs.getLong("procedure_occurrence_id"));
		x.setPersonId(rs.getLong("person_id"));
		x.setProcedureConceptId(rs.getLong("procedure_concept_id"));
		x.setProcedureDate(rs.getDate("procedure_date"));
		x.setProcedureTypeConceptId(rs.getLong("procedure_type_concept_id"));
		x.setAssociatedProviderId(rs.getLong("associated_provider_id"));
		x.setVisitOccurrenceId(rs.getLong("visit_occurrence_id"));
		x.setRelevantConditionConceptId(rs.getLong("relevant_condition_concept_id"));
		x.setProcedureSourceValue(rs.getString("procedure_source_value"));
		x.setxDataSourceType(rs.getString("x_data_source_type"));
		x.setxGridNodeId(rs.getLong("x_grid_node_id"));
		return x;
	}

}
