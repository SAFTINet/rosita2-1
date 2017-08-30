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

package edu.ucdenver.rosita.xml;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import edu.ucdenver.rosita.etl.dao.EtlBatchPreparedStatementSetter;
import edu.ucdenver.rosita.etl.dao.omop.*;
import edu.ucdenver.rosita.etl.omop.*;
import edu.ucdenver.rosita.utils.DifferenceStopwatch;
import edu.ucdenver.rosita.utils.OmopRowMappers;
import edu.ucdenver.rosita.utils.RositaLogger;
import edu.ucdenver.rosita.utils.SqlTemplates;

public class OmopExporterService extends EtlBatchPreparedStatementSetter<DummyOmop> implements BatchPreparedStatementSetter {
	
	public DataSource ds = null;
	public DataSource targetDs = null;
	SqlTemplates sqlTemplates = new SqlTemplates();
	OmopRowMappers rowMappers = new OmopRowMappers();
	JdbcTemplate jdbc = null;
	Long gridNodeId = 0L;
	Integer tablesDone = 0;
	Long objectsDone = 0L;

	public OmopExporterService(Integer threshold, DataSource ds, DataSource targetDs, Long gridNodeId) {
		super(threshold, -1L, null); //No data source ID needed here - it is not transferred to GRID.
		this.ds = ds;
		this.targetDs = targetDs;
		jdbc = new JdbcTemplate(ds);
		this.gridNodeId = gridNodeId;
	}
	
	public boolean export() throws SQLException {
		
		DifferenceStopwatch.getInstance().start();
		RositaLogger.log(false, DifferenceStopwatch.getInstance().getTimeAndDiff() + " Starting task");
		
		try {
			transferData(new LocationOmopCache(targetDs, threshold, gridNodeId));
			transferData(new OrganizationOmopCache(targetDs, threshold, gridNodeId));
			transferData(new CareSiteOmopCache(targetDs, threshold, gridNodeId));
			transferData(new ProviderOmopCache(targetDs, threshold, gridNodeId));
			transferData(new PersonOmopCache(targetDs, threshold, gridNodeId));
			transferData(new VisitOccurrenceOmopCache(targetDs, threshold, gridNodeId));
			transferData(new ConditionOccurrenceOmopCache(targetDs, threshold, gridNodeId));
			transferData(new DrugExposureOmopCache(targetDs, threshold, gridNodeId));
			transferData(new DrugCostOmopCache(targetDs, threshold, gridNodeId));
			transferData(new ProcedureOccurrenceOmopCache(targetDs, threshold, gridNodeId));
			transferData(new ObservationOmopCache(targetDs, threshold, gridNodeId));
			transferData(new PayerPlanPeriodOmopCache(targetDs, threshold, gridNodeId));
			transferData(new DeathOmopCache(targetDs, threshold, gridNodeId));
			transferData(new CohortOmopCache(targetDs, threshold, gridNodeId));
			transferData(new ProcedureCostOmopCache(targetDs, threshold, gridNodeId));
			RositaLogger.log(true, "SUCCESS|||" + tablesDone);
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return false;
		}
		return true;
	}
	
	public void transferData(OmopCache cache) throws SQLException {
		
		Long startId = -1L;
		RowMapper mapper = rowMappers.get(cache.getClassName());
		while (true) {
			RositaLogger.log(false, DifferenceStopwatch.getInstance().getTimeAndDiff() + " " + cache.getClassName() + " fetching from ID " + startId);
			List items = jdbc.query(sqlTemplates.get("Import" + cache.getClassName()), new Object[] {startId, this.threshold}, mapper);
			
			if (items.size() == 0) {
				RositaLogger.log(false, DifferenceStopwatch.getInstance().getTimeAndDiff() + " " + cache.getClassName() + " had no more in database.");
				break;
			}
			RositaLogger.log(false, DifferenceStopwatch.getInstance().getTimeAndDiff() + " " + "Fetched " + items.size() + " " + cache.getClassName() + " to transfer");
			for (int i = 0; i < items.size(); i++) {
				cache.add(items.get(i));
			}
			startId = cache.saveAndClearCache();
			objectsDone += items.size();
			RositaLogger.log(true, "STATUS|||" + tablesDone + "|||" + objectsDone);
			RositaLogger.log(false, DifferenceStopwatch.getInstance().getTimeAndDiff() + " " + cache.getClassName() + " transferred, last ID is " + startId);
		}
		
		RositaLogger.log(false, DifferenceStopwatch.getInstance().getTimeAndDiff() + "---------------------------------------------------------");
		tablesDone++;
		RositaLogger.log(true, "STATUS|||" + tablesDone + "|||" + objectsDone);
	}

}
