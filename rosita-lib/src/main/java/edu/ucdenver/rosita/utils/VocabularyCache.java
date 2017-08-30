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

package edu.ucdenver.rosita.utils;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import edu.ucdenver.rosita.etl.VocabularyRow;
import edu.ucdenver.rosita.etl.dao.VocabularyRowBatchPreparedStatementSetter;

public class VocabularyCache {
	
	int OBJECT_THRESHOLD = 5000;
	
	final VocabularyRowBatchPreparedStatementSetter vocabularyRows;
	
	SqlTemplates sqlTemplates = new SqlTemplates();
	
	private DataSource dataSource;
	private JdbcTemplate jdbc;
	private Long objectsLoaded = 0L;
	
	public VocabularyCache(DataSource ds, Integer threshold) {
		this.dataSource = ds;
		if (dataSource != null) {
			this.jdbc = new JdbcTemplate(dataSource);
		}
		if (threshold != null && threshold > 0) {
			OBJECT_THRESHOLD = threshold;
		}

		this.vocabularyRows = new VocabularyRowBatchPreparedStatementSetter(threshold);

	}
	
	public void add(VocabularyRow o) {
		if (dataSource != null) {
			if (vocabularyRows.add(o)) {
				jdbc.batchUpdate(sqlTemplates.get("VocabularyRow"), vocabularyRows);
				objectsLoaded += vocabularyRows.getBatchSize();
				vocabularyRows.clear();
				RositaLogger.log(false, "Imported " + objectsLoaded + " rows...");
				RositaLogger.log(true, "STATUS|||" + objectsLoaded);
			}
		}
	}
	
	public void saveAll() {
		if (dataSource != null) {
			jdbc.batchUpdate(sqlTemplates.get("VocabularyRow"), vocabularyRows);
			objectsLoaded += vocabularyRows.getBatchSize();
			RositaLogger.log(false, "Saved all remaining vocabulary rows: " + objectsLoaded);
			RositaLogger.log(true, "STATUS|||" + objectsLoaded);
		}
	}
}
