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
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

public class EtlBatchPreparedStatementSetter<T> implements BatchPreparedStatementSetter {
	
	protected final List<T> items = new ArrayList<T>();
	protected int threshold = 5000;
	protected Long dataSourceId = -1L;
	protected Timestamp etlDate = null;
	
	public EtlBatchPreparedStatementSetter(Integer threshold, Long dataSourceId, Timestamp timestamp) {
		if (threshold != null && threshold > 0) {
			this.threshold = threshold;
		}
		if (dataSourceId != null && dataSourceId > 0) {
			this.dataSourceId = dataSourceId;
		}
		this.etlDate = timestamp;
	}
	
	public EtlBatchPreparedStatementSetter(Integer threshold) {
		if (threshold != null && threshold > 0) {
			this.threshold = threshold;
		}
	}
	
	public EtlBatchPreparedStatementSetter(Integer threshold, Long dataSourceId ) {
		if (threshold != null && threshold > 0) {
			this.threshold = threshold;
		}
		if (dataSourceId != null && dataSourceId > 0) {
			this.dataSourceId = dataSourceId;
		}
	}
	
	public boolean add(T item) {
		items.add(item);
		if (items.size() >= threshold) {
			return true;
		}
		return false;
	}
	
	public void clear() {
		items.clear();
	}

	public int getBatchSize() {
		//Stub
		return 0;
	}

	public void setValues(PreparedStatement arg0, int arg1) throws SQLException {
		//Stub
	}

}
