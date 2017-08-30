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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import edu.ucdenver.rosita.etl.DelimitedColumn;
import edu.ucdenver.rosita.etl.DelimitedObject;
import edu.ucdenver.rosita.utils.ParsingService;

public class DelimitedObjectBatchPreparedStatementSetter extends EtlBatchPreparedStatementSetter<DelimitedObject> implements BatchPreparedStatementSetter {
	
	List<DelimitedColumn> columnData = new ArrayList<DelimitedColumn>();
	String tableName;
	Map<Integer, Integer> columnMap = new HashMap<Integer, Integer>();

	public DelimitedObjectBatchPreparedStatementSetter(Integer threshold, Long dataSourceId, Timestamp etlDate) {
		super(threshold, dataSourceId, etlDate);
	}

	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		DelimitedObject d = items.get(i);
		List<String> columns = d.getColumns();
		int columnNumber = 1;
		for (int c = 0; c < columnData.size(); c++) {
			//Need to look up the target columns for data from the column map
			Integer targetColumnIndex = columnMap.get(c);
			String value = "";
			if (targetColumnIndex < columns.size()) {
				value = columns.get(targetColumnIndex);
			}
			ps.setString(columnNumber++, value);
		}
		ps.setTimestamp(columnNumber++, etlDate);
		ps.setLong(columnNumber++, dataSourceId);
		ps.setLong(columnNumber++, i);
	}
	
	@Override
	public int getBatchSize() {
		return items.size();
	}
	
	public String generateSqlTemplate() {
		return "INSERT INTO \"raw\"." + tableName + "(" + commaSeparate(columnData) + ") VALUES (" + commaSeparateQuestionMarks(columnData.size()+3) + ");";
	}
	
	private String commaSeparate(List<DelimitedColumn> columnData) {
		StringBuilder sb = new StringBuilder();
		if (columnData.size() > 0) {
			sb.append(ParsingService.getDatabaseSafeName(columnData.get(0).getName()));
		}
		for (int i = 1; i < columnData.size(); i++) {
			sb.append(", " + ParsingService.getDatabaseSafeName(columnData.get(i).getName()));
		}
		
		sb.append(", x_etl_date, x_data_source_id, x_record_num");
		return sb.toString();
	}
	
	private String commaSeparateQuestionMarks(int number) {
		StringBuilder sb = new StringBuilder();
		if (number > 0) {
			sb.append("?");
		}
		for (int i = 1; i < number; i++) {
			sb.append(",?");
		}
		
		return sb.toString();
	}

	public java.sql.Timestamp getEtlDate() {
		return etlDate;
	}

	public List<DelimitedColumn> getColumnData() {
		return columnData;
	}

	public String getTableName() {
		return tableName;
	}

	public void setEtlDate(java.sql.Timestamp etlDate) {
		this.etlDate = etlDate;
	}

	public void setColumnData(List<DelimitedColumn> columnData) {
		this.columnData = columnData;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public Map<Integer, Integer> getColumnMap() {
		return columnMap;
	}

	public void setColumnMap(Map<Integer, Integer> columnMap) {
		this.columnMap = columnMap;
	}

}
