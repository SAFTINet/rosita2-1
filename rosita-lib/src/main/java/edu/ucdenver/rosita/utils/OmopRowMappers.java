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

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import edu.ucdenver.rosita.etl.dao.rowmapper.*;

public class OmopRowMappers {
	
	private Map<String, RowMapper> mappersMap = new HashMap<String, RowMapper>();
	
	public OmopRowMappers() {
		mappersMap.put("OmopCareSite", new OmopCareSiteRowMapper());
		mappersMap.put("OmopCohort", new OmopCohortRowMapper());
		mappersMap.put("OmopConditionEra", new OmopConditionEraRowMapper());
		mappersMap.put("OmopConditionOccurrence", new OmopConditionOccurrenceRowMapper());
		mappersMap.put("OmopDeath", new OmopDeathRowMapper());
		mappersMap.put("OmopDrugCost", new OmopDrugCostRowMapper());
		mappersMap.put("OmopDrugEra", new OmopDrugEraRowMapper());
		mappersMap.put("OmopDrugExposure", new OmopDrugExposureRowMapper());
		mappersMap.put("OmopLocation", new OmopLocationRowMapper());
		mappersMap.put("OmopObservationPeriod", new OmopObservationPeriodRowMapper());
		mappersMap.put("OmopObservation", new OmopObservationRowMapper());
		mappersMap.put("OmopOrganization", new OmopOrganizationRowMapper());
		mappersMap.put("OmopPayerPlanPeriod", new OmopPayerPlanPeriodRowMapper());
		mappersMap.put("OmopPerson", new OmopPersonRowMapper());
		mappersMap.put("OmopProcedureCost", new OmopProcedureCostRowMapper());
		mappersMap.put("OmopProcedureOccurrence", new OmopProcedureOccurrenceRowMapper());
		mappersMap.put("OmopProvider", new OmopProviderRowMapper());
		mappersMap.put("OmopVisitOccurrence", new OmopVisitOccurrenceRowMapper());
		mappersMap.put("OmopXCohortMetadata", new OmopXCohortMetadataRowMapper());
	}
	
	  
	public RowMapper get(String name) {
		return mappersMap.get(name);
	}

}
