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

import edu.ucdenver.rosita.etl.omop.OmopDeath;

public class OmopDeathRowMapper implements RowMapper<OmopDeath> {

	@Override
	public OmopDeath mapRow(ResultSet rs, int rowNum) throws SQLException {
		OmopDeath x = new OmopDeath();
		x.setPersonId(rs.getLong("person_id"));
		x.setDeathDate(rs.getDate("death_date"));
		x.setDeathTypeConceptId(rs.getLong("death_type_concept_id"));
		x.setCauseOfDeathConceptId(rs.getLong("cause_of_death_concept_id"));
		x.setCauseOfDeathSourceValue(rs.getString("cause_of_death_source_value"));
		x.setxGridNodeId(rs.getLong("x_grid_node_id"));
		return x;
	}

}
