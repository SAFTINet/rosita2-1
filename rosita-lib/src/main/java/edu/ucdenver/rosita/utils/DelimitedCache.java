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

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import edu.ucdenver.rosita.etl.CareSite;
import edu.ucdenver.rosita.etl.Cohort;
import edu.ucdenver.rosita.etl.ConditionOccurrence;
import edu.ucdenver.rosita.etl.Death;
import edu.ucdenver.rosita.etl.DelimitedColumn;
import edu.ucdenver.rosita.etl.DelimitedObject;
import edu.ucdenver.rosita.etl.Demographic;
import edu.ucdenver.rosita.etl.DrugCost;
import edu.ucdenver.rosita.etl.DrugExposure;
import edu.ucdenver.rosita.etl.Observation;
import edu.ucdenver.rosita.etl.Organization;
import edu.ucdenver.rosita.etl.PayerPlanPeriod;
import edu.ucdenver.rosita.etl.ProcedureCost;
import edu.ucdenver.rosita.etl.ProcedureOccurrence;
import edu.ucdenver.rosita.etl.Provider;
import edu.ucdenver.rosita.etl.VisitOccurrence;
import edu.ucdenver.rosita.etl.dao.*;
import edu.ucdenver.rosita.xml.RawTableService;

public class DelimitedCache {
	
	int OBJECT_THRESHOLD = 5000;
	Long dataSourceId = -1L;
	
	static Stopwatch stopwatch = new Stopwatch();
	
	final DelimitedObjectBatchPreparedStatementSetter delimitedObjects;
	
	SqlTemplates sqlTemplates = new SqlTemplates();
	
	private DataSource dataSource;
	private JdbcTemplate jdbc;
	
	public DelimitedCache(DataSource ds, Integer threshold, Long dataSourceId) {
		RawTableService rawTableService = new RawTableService(null);
		this.dataSource = ds;
		this.dataSourceId = dataSourceId;
		this.jdbc = new JdbcTemplate(dataSource);
		if (threshold != null && threshold > 0) {
			OBJECT_THRESHOLD = threshold;
		}
		
		this.delimitedObjects = new DelimitedObjectBatchPreparedStatementSetter(threshold, dataSourceId,rawTableService.getSqlDateTime()); //TODO Get this date from the database
		
		stopwatch.start();
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}
	
	public void add(DelimitedObject o) {
		if (delimitedObjects.add(o)) {
			jdbc.batchUpdate(delimitedObjects.generateSqlTemplate(), delimitedObjects);
			RositaLogger.log(false, "Saved " + delimitedObjects.getBatchSize() + " objects - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
			delimitedObjects.clear();
		}
	}
	
	public void saveAll() {
		jdbc.batchUpdate(delimitedObjects.generateSqlTemplate(), delimitedObjects);
		delimitedObjects.clear();
		RositaLogger.log(false, "Saved everything - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
	}
	
	public void setupCache(String tableName, List<DelimitedColumn> columnData, Map<Integer, Integer> columnMap) {
		this.delimitedObjects.setTableName(tableName);
		this.delimitedObjects.setColumnData(columnData);
		this.delimitedObjects.setColumnMap(columnMap);
	}
}
