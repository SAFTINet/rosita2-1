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

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import edu.ucdenver.rosita.ArgHandler;
import edu.ucdenver.rosita.etl.ValidationError;

public class ValidationErrorBatchPreparedStatementSetter extends EtlBatchPreparedStatementSetter<ValidationError> implements BatchPreparedStatementSetter {

	public ValidationErrorBatchPreparedStatementSetter(Integer threshold, Long dataSourceId, Timestamp timestamp) {
		super(threshold, dataSourceId, timestamp);
	}
	
	public ValidationErrorBatchPreparedStatementSetter(Integer threshold, Long dataSourceId) {
		super(threshold, dataSourceId);
	}


	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		ValidationError v = items.get(i);
		
		ps.setLong(1, ArgHandler.getLong("jobid"));
		ps.setLong(2, ArgHandler.getLong("stepid"));
		ps.setString(3, v.isAllowed() ? "WARNING" : "ERROR");
		ps.setString(4, v.getFilename() + " line " + v.getLineNumber() + ": " + v.getMessage());
		ps.setString(5, v.getType());
		ps.setTimestamp(6, new Timestamp(v.getDate().getTime()));
	}
	
	@Override
	public int getBatchSize() {
		return items.size();
	}

}
