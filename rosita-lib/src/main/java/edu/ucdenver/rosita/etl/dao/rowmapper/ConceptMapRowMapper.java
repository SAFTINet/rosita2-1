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

import edu.ucdenver.rosita.etl.ConceptMap;

public class ConceptMapRowMapper implements RowMapper<ConceptMap> {

	@Override
	public ConceptMap mapRow(ResultSet rs, int rowNum) throws SQLException {
		ConceptMap x = new ConceptMap();
		x.setConceptMapId(rs.getLong("concept_map_id"));
		x.setTargetTable(rs.getString("target_table"));
		x.setTargetColumn(rs.getString("target_column"));
		x.setSourceValue(rs.getString("source_value"));
		x.setSourceVocabulary(rs.getString("source_vocabulary"));
		x.setSourceDesc(rs.getString("source_desc"));
		x.setTargetConceptId(rs.getLong("target_concept_id"));
		x.setMapped(rs.getString("is_mapped"));
		x.setEmpty(rs.getString("is_empty"));
		x.setSourceCount(rs.getLong("source_count"));
		return x;
	}

}
