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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

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

public class UnmarshallerPool {
	
	public Unmarshaller organizationUnmarshaller = null;
	public Unmarshaller careSiteUnmarshaller = null;
	public Unmarshaller demographicUnmarshaller = null;
	public Unmarshaller drugExposureUnmarshaller = null;
	public Unmarshaller procedureOccurrenceUnmarshaller = null;
	
	private Map<String, Unmarshaller> unmarshallers = new HashMap<String, Unmarshaller>();
	
	public UnmarshallerPool() throws JAXBException {
		
    	organizationUnmarshaller = prepareUnmarshaller(Organization.class);    	
    	careSiteUnmarshaller = prepareUnmarshaller(CareSite.class);
    	demographicUnmarshaller = prepareUnmarshaller(Demographic.class);
    	drugExposureUnmarshaller = prepareUnmarshaller(DrugExposure.class);
    	procedureOccurrenceUnmarshaller = prepareUnmarshaller(ProcedureOccurrence.class);
    	
    	unmarshallers.put("provider", prepareUnmarshaller(Provider.class));
    	unmarshallers.put("visit_occurrence", prepareUnmarshaller(VisitOccurrence.class));
    	unmarshallers.put("observation", prepareUnmarshaller(Observation.class));
    	unmarshallers.put("drug_cost", prepareUnmarshaller(DrugCost.class));
    	unmarshallers.put("condition_occurrence", prepareUnmarshaller(ConditionOccurrence.class));
    	unmarshallers.put("procedure_cost", prepareUnmarshaller(ProcedureCost.class));
    	unmarshallers.put("cohort", prepareUnmarshaller(Cohort.class));
    	unmarshallers.put("death", prepareUnmarshaller(Death.class));
    	unmarshallers.put("payer_plan_period", prepareUnmarshaller(PayerPlanPeriod.class));
		
	}
	
	public Unmarshaller prepareUnmarshaller(Class c) throws JAXBException {
		Unmarshaller u = JAXBContext.newInstance(c).createUnmarshaller();
		return u;
	}
	
	public Unmarshaller get(String name) {
		return unmarshallers.get(name);
	}

}
