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

import edu.ucdenver.rosita.etl.omop.OmopVisitOccurrence;

public class OmopVisitOccurrenceRowMapper implements RowMapper<OmopVisitOccurrence> {

	@Override
	public OmopVisitOccurrence mapRow(ResultSet rs, int rowNum) throws SQLException {
		OmopVisitOccurrence x = new OmopVisitOccurrence();
		x.setId(rs.getLong("visit_occurrence_id"));
		x.setPersonId(rs.getLong("person_id"));
		x.setVisitStartDate(rs.getDate("visit_start_date"));
		x.setVisitEndDate(rs.getDate("visit_end_date"));
		x.setPlaceOfServiceConceptId(rs.getLong("place_of_service_concept_id"));
		x.setCareSiteId(rs.getLong("care_site_id"));
		x.setPlaceOfServiceSourceValue(rs.getString("place_of_service_source_value"));
		x.setxVisitOccurrenceSourceIdentifier(rs.getString("x_visit_occurrence_source_identifier"));
		x.setxDataSourceType(rs.getString("x_data_source_type"));
		x.setxProviderId(rs.getLong("x_provider_id"));
		x.setxGridNodeId(rs.getLong("x_grid_node_id"));
		return x;
	}

}
