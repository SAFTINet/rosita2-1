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

package edu.ucdenver.rosita.etl.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import edu.ucdenver.rosita.etl.ValidationError;
import edu.ucdenver.rosita.etl.VocabularyRow;

public class VocabularyRowBatchPreparedStatementSetter extends EtlBatchPreparedStatementSetter<VocabularyRow> implements BatchPreparedStatementSetter {

	public VocabularyRowBatchPreparedStatementSetter(Integer threshold) {
		super(threshold);
	}

	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		VocabularyRow v = items.get(i);
		ps.setString(1, v.getSourceCode());
		ps.setInt(2, v.getSourceVocabularyId());
		ps.setString(3, v.getSourceCodeDescription());
		ps.setInt(4, v.getTargetConceptId());
		ps.setInt(5, v.getTargetVocabularyId());
		ps.setString(6, v.getMappingType());
		ps.setString(7, v.getPrimaryMap());
		ps.setDate(8, new java.sql.Date(v.getValidStartDate().getTime()));
		ps.setDate(9, new java.sql.Date(v.getValidEndDate().getTime()));
		ps.setString(10, v.getInvalidReason());
		ps.setString(11, v.getMapSource());
		ps.setLong(12, v.getDataSourceId());
	}
	
	@Override
	public int getBatchSize() {
		return items.size();
	}

}
