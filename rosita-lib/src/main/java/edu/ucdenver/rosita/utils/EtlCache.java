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

import java.sql.Timestamp;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import edu.ucdenver.rosita.etl.CareSite;
import edu.ucdenver.rosita.etl.Cohort;
import edu.ucdenver.rosita.etl.ConditionOccurrence;
import edu.ucdenver.rosita.etl.Death;
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

public class EtlCache {
	
	int OBJECT_THRESHOLD = 5000;
	Long dataSourceId = -1L;
	
	static Stopwatch stopwatch = new Stopwatch();
	
	final CareSiteBatchPreparedStatementSetter careSites;
	final ProviderBatchPreparedStatementSetter providers;
	final DemographicBatchPreparedStatementSetter demographics;
	final VisitOccurrenceBatchPreparedStatementSetter visitOccurrences;
	final ObservationBatchPreparedStatementSetter observations;
	final DrugExposureBatchPreparedStatementSetter drugExposures;
	final DrugCostBatchPreparedStatementSetter drugCosts;
	final ConditionOccurrenceBatchPreparedStatementSetter conditionOccurrences;
	final ProcedureOccurrenceBatchPreparedStatementSetter procedureOccurrences;
	final ProcedureCostBatchPreparedStatementSetter procedureCosts;
	final CohortBatchPreparedStatementSetter cohorts;
	final DeathBatchPreparedStatementSetter deaths;
	final PayerPlanPeriodBatchPreparedStatementSetter payerPlanPeriods;
	
	SqlTemplates sqlTemplates = new SqlTemplates();
	
	private DataSource dataSource;
	private JdbcTemplate jdbc;
	private Timestamp timestamp;
	
	public EtlCache(DataSource ds, Integer threshold, Long dataSourceId) {
		this.dataSource = ds;
		this.dataSourceId = dataSourceId;
		this.jdbc = new JdbcTemplate(dataSource);
		if (threshold != null && threshold > 0) {
			OBJECT_THRESHOLD = threshold;
		}
		
		RawTableService rawTableService = new RawTableService(null);
		timestamp = rawTableService.getSqlDateTime();
		
		this.careSites = new CareSiteBatchPreparedStatementSetter(threshold, dataSourceId, timestamp);
		this.providers = new ProviderBatchPreparedStatementSetter(threshold, dataSourceId, timestamp);
		this.demographics = new DemographicBatchPreparedStatementSetter(threshold, dataSourceId, timestamp);
		this.visitOccurrences = new VisitOccurrenceBatchPreparedStatementSetter(threshold, dataSourceId, timestamp);
		this.observations = new ObservationBatchPreparedStatementSetter(threshold, dataSourceId, timestamp);
		this.drugExposures = new DrugExposureBatchPreparedStatementSetter(threshold, dataSourceId, timestamp);
		this.drugCosts = new DrugCostBatchPreparedStatementSetter(threshold, dataSourceId, timestamp);
		this.conditionOccurrences = new ConditionOccurrenceBatchPreparedStatementSetter(threshold, dataSourceId, timestamp);
		this.procedureOccurrences = new ProcedureOccurrenceBatchPreparedStatementSetter(threshold, dataSourceId, timestamp);
		this.procedureCosts = new ProcedureCostBatchPreparedStatementSetter(threshold, dataSourceId, timestamp);
		this.cohorts = new CohortBatchPreparedStatementSetter(threshold, dataSourceId, timestamp);
		this.deaths = new DeathBatchPreparedStatementSetter(threshold, dataSourceId, timestamp);
		this.payerPlanPeriods = new PayerPlanPeriodBatchPreparedStatementSetter(threshold, dataSourceId, timestamp);
		
		stopwatch.start();
	}
	
	public void add(Organization o) {
		Organization org = (Organization) o;
		jdbc.update(sqlTemplates.get("Organization"),				
			new Object[] {org.getOrganizationAddress1(), org.getOrganizationAddress2(),
				org.getOrganizationCity(), org.getOrganizationCounty(), org.getOrganizationSourceValue(),
				org.getOrganizationState(), org.getOrganizationZip(), org.getPlaceOfServiceSourceValue(),
				org.getxDataSourceType(), dataSourceId,timestamp,new Long(org.getId())
			 }
		);
	}
	
	public void add(CareSite o) {
		if (careSites.add(o)) {
			jdbc.batchUpdate(sqlTemplates.get("CareSite"), careSites);
			RositaLogger.log(false, "Saved " + careSites.getBatchSize() + " careSites - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
			careSites.clear();
		}
	}
	
	public void add(Provider o) {
		if (providers.add(o)) {
			jdbc.batchUpdate(sqlTemplates.get("Provider"), providers);
			RositaLogger.log(false, "Saved " + providers.getBatchSize() + " providers - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
			providers.clear();
		}
	}
	
	public void add(Demographic o) {
		if (demographics.add(o)) {
			jdbc.batchUpdate(sqlTemplates.get("Demographic"), demographics);
			RositaLogger.log(false, "Saved " + demographics.getBatchSize() + " demographics - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
			demographics.clear();
		}
	}
	
	public void add(VisitOccurrence o) {
		if (visitOccurrences.add(o)) {
			jdbc.batchUpdate(sqlTemplates.get("VisitOccurrence"), visitOccurrences);
			RositaLogger.log(false, "Saved " + visitOccurrences.getBatchSize() + " visitOccurrences - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
			visitOccurrences.clear();
		}
	}
	
	public void add(Observation o) {
		if (observations.add(o)) {
			jdbc.batchUpdate(sqlTemplates.get("Observation"), observations);
			RositaLogger.log(false, "Saved " + observations.getBatchSize() + " observations - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
			observations.clear();
		}
	}
	
	public void add(DrugExposure o) {
		if (drugExposures.add(o)) {
			jdbc.batchUpdate(sqlTemplates.get("DrugExposure"), drugExposures);
			RositaLogger.log(false, "Saved " + drugExposures.getBatchSize() + " drugExposures - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
			drugExposures.clear();
		}
	}
	
	public void add(DrugCost o) {
		if (drugCosts.add(o)) {
			jdbc.batchUpdate(sqlTemplates.get("DrugCost"), drugCosts);
			RositaLogger.log(false, "Saved " + drugCosts.getBatchSize() + " drugCosts - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
			drugCosts.clear();
		}
	}
	
	public void add(ConditionOccurrence o) {
		if (conditionOccurrences.add(o)) {
			jdbc.batchUpdate(sqlTemplates.get("ConditionOccurrence"), conditionOccurrences);
			RositaLogger.log(false, "Saved " + conditionOccurrences.getBatchSize() + " conditionOccurrences - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
			conditionOccurrences.clear();
		}
	}
	
	public void add(ProcedureOccurrence o) {
		if (procedureOccurrences.add(o)) {
			jdbc.batchUpdate(sqlTemplates.get("ProcedureOccurrence"), procedureOccurrences);
			RositaLogger.log(false, "Saved " + procedureOccurrences.getBatchSize() + " procedureOccurrences - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
			procedureOccurrences.clear();
		}
	}
	
	public void add(ProcedureCost o) {
		if (procedureCosts.add(o)) {
			jdbc.batchUpdate(sqlTemplates.get("ProcedureCost"), procedureCosts);
			RositaLogger.log(false, "Saved " + procedureCosts.getBatchSize() + " procedureCosts - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
			procedureCosts.clear();
		}
	}
	
	public void add(Cohort o) {
		if (cohorts.add(o)) {
			jdbc.batchUpdate(sqlTemplates.get("Cohort"), cohorts);
			RositaLogger.log(false, "Saved " + cohorts.getBatchSize() + " cohorts - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
			cohorts.clear();
		}
	}
	
	public void add(Death o) {
		if (deaths.add(o)) {
			jdbc.batchUpdate(sqlTemplates.get("Death"), deaths);
			RositaLogger.log(false, "Saved " + deaths.getBatchSize() + " deaths - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
			deaths.clear();
		}
	}
	
	public void add(PayerPlanPeriod o) {
		if (payerPlanPeriods.add(o)) {
			jdbc.batchUpdate(sqlTemplates.get("PayerPlanPeriod"), payerPlanPeriods);
			RositaLogger.log(false, "Saved " + payerPlanPeriods.getBatchSize() + " payerPlanPeriods - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
			payerPlanPeriods.clear();
		}
	}
	
	public void saveAll() {
		jdbc.batchUpdate(sqlTemplates.get("CareSite"), careSites);
		jdbc.batchUpdate(sqlTemplates.get("Provider"), providers);
		jdbc.batchUpdate(sqlTemplates.get("Demographic"), demographics);
		jdbc.batchUpdate(sqlTemplates.get("VisitOccurrence"), visitOccurrences);
		jdbc.batchUpdate(sqlTemplates.get("Observation"), observations);
		jdbc.batchUpdate(sqlTemplates.get("DrugExposure"), drugExposures);
		jdbc.batchUpdate(sqlTemplates.get("DrugCost"), drugCosts);
		jdbc.batchUpdate(sqlTemplates.get("ConditionOccurrence"), conditionOccurrences);
		jdbc.batchUpdate(sqlTemplates.get("ProcedureOccurrence"), procedureOccurrences);
		jdbc.batchUpdate(sqlTemplates.get("ProcedureCost"), procedureCosts);
		jdbc.batchUpdate(sqlTemplates.get("Cohort"), cohorts);
		jdbc.batchUpdate(sqlTemplates.get("Death"), deaths);
		jdbc.batchUpdate(sqlTemplates.get("PayerPlanPeriod"), payerPlanPeriods);
		RositaLogger.log(false, "Saved everything - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
	}
}
