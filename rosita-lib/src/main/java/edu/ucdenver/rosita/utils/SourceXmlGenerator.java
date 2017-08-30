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

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class SourceXmlGenerator {
	
	static FileWriter fw = null;
	static Random rnd = new Random();
	static NameGenerator gen = new NameGenerator();
	static int organizationId = 0;
	static int careSiteId = 0;
	static int providerId = 0;
	static int personId = 0;
	static int visitId = 0;
	static int personVisitId = 0;
	static int conditionId = 0;
	static int procedureId = 0;
	static int drugId = 0;
	static int observationId = 0;
	static Date visitDate = null;
		
	static String[] cities = {"DENVER", "ARVADA", "LITTLETON", "GOLDEN", "WESTMINSTER", "PARKER", "BROOMFIELD", "COMMERCE CITY", "BAILEY", "MORRISON"};
	static String[] counties = {"ARAPAHOE","JEFFERSON","ADAMS","DENVER","DOUGLAS","PARK","UNKNOWN","BROOMFIELD","BOULDER","WELD"};
	static String[] zips = {"80010","80214","80017","80226","80215","80110","80003","80247","80120","80227"};
	static String[] specialties = {"Doctor", "Provider"};
	static String[] maleNames = {"JAMES","JOHN","ROBERT","MICHAEL","WILLIAM","DAVID","RICHARD","CHARLES","JOSEPH","THOMAS",
		"CHRISTOPHER","DANIEL","PAUL","MARK","DONALD","GEORGE","KENNETH","STEVEN","EDWARD","BRIAN"};
	static String[] femaleNames = {"MARY","PATRICIA","LINDA","BARBARA","ELIZABETH","JENNIFER","MARIA","SUSAN","MARGARET","DORTHY",
		"LISA","NANCY","KAREN","BETTY","HELEN","SANDRA","DONNA","CAROL","RUTH","SHARON"};
	static String[] lastNames = {"SMITH","JOHNSON","WILLIAMS","JONES","BROWN","DAVIS","MILLER","WILSON","MOORE","TAYLOR",
		"ANDERSON","THOMAS","JACKSON","WHITE","HARRIS","MARTIN","THOMPSON","GARCIA","MARTINEZ","ROBINSON",};
	static String[] genders = {"M", "F", "U"};
	static String[] races = {"White/Caucasian","","Black/African American","Unreported/Refused to Report","More than one race","Asian","American Indian/Alaskan Native","Other Pacific Islander","Native Hawaiian"};
	static String[] ethnicities = {"Hispanic/Latino","Non-Hispanic (Other)","","Not Reported"};
	static String[] placesOfService = {"","Chronic Care Est","Open Access","Short/OB Established","New Patient","Short/Lab","Long","Triage","Long/OB (US or PP)","Dental New Patient Child"};

	// cpt, lab out is hcpcs
	static String[] procedures = {"99213","529","99214","99000","90471","80053","LAB OUT","36415","90472","84443"};
	
	// icd9
	static String[] conditions = {"V20.2 ","V68.1 ","V70.0 ","401.9 ","250.00","272.4 ","V04.81","V06.1 ","V06.9 ","V03.82"};

	// ndc
	static String[] drugs = {"00172375860","66336017494","62584073633","54569028904","66116023130","51079044401","68180051503","58016063300","54868326501","67544040860"};
	static String[] drugNames = {"METFORMIN HCL","LEVOTHYROXINE SODIUM","AMOXICILLIN TRIHYDRATE","ALBUTEROL SULFATE","CYCLOBENZAPRINE HCL","FLUOXETINE HCL","TRAZODONE HCL","BLOOD SUGAR DIAGNOSTIC","ASPIRIN","NORGESTIMATE-ETHINYL ESTRADIOL"};
	static String[] drugStrengths = {"20 MG","10 MG","PSUNK","50 MCG","600 MG","800 MG","50000 UNIT","2.5 MG","25 MCG","100 MCG"};
	static String[] drugSigs = {"0.5mL in inhaled every 4 hours","0.5 ml once daily","0.5 ml    as needed every 4 ho","-","0.5 teaspoon po every day at b","0.4 ml    as needed every 4 ho","0.6 ml    as needed every 4 ho","0.125 mg once daily 2-3 hours","0.4 ml every 4 hours as needed","0.5 ml  as needed every 4 hour"};

	// loinc
	static String[] observations = {"potassium, serum", "calcium, serum", "sodium, serum", "carbon dioxide, serum, total", "alanine aminotransferase (SGPT), serum", "bilirubin, serum, total", "alkaline phosphatase, serum", "globulins, serum, total", "red blood cell distribution width", "leukocyte count, blood"};
	static String[] observationValues = {"2.1 RATIO","MANY","&lt;3 U/mL","476 CELLS/MCL","313 CELLS/MCL","84.2","2.53","203 X10E3/UL","450 CELLS/MCL","22.030"};
	static String[] observationTypes = {"LABRESULTS","VITALSIGNSFLOWSHEET","PATIENTSOCIALHISTORY","TOBACCOFLOWSHEET","FLOWSHEET","PAINFLOWSHEET","ASTHMAFLOWSHEET"};

	static int ORGANIZATIONS = 1;
	static int CARE_SITES_PER_ORGANIZATION = 10;
	static int MAX_PROVIDERS_PER_CARE_SITE = 10;
	static int DEMOGRAPHICS_PER_ORGANIZATION = 100;
	static int VISITS_PER_PERSON = 2;
	static int CONDITIONS_PER_PERSON = 4;
	static int PROCEDURES_PER_PERSON = 2;
	static int DRUGS_PER_PERSON = 3;
	static int OBSERVATIONS_PER_PERSON = 3;
	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	public static void main(String[] args) {
		generate(args);
	}
	
	public static void generate(String[] args) {
		System.out.println("Give us a minute...");
			
		try {
			if (args.length < 4) {
				System.out.println("Please provide on command line: filename, demographics, care sites");
				System.exit(0);
			} else {
                System.out.println("file = "+args[1]+" dpo = "+args[2]+" cspo = "+args[3]);
            }
			fw = new FileWriter(new File(args[1]));
			DEMOGRAPHICS_PER_ORGANIZATION = Integer.parseInt(args[2]);
			CARE_SITES_PER_ORGANIZATION = Integer.parseInt(args[3]);

			out("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			
			for (int i = 0; i < ORGANIZATIONS; i++) {
				generateOrganization();
				System.out.println("Generated organization " + (i+1) + " of " + ORGANIZATIONS + "...");
			}
			System.out.println("Clinical XML generation complete");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				fw.flush();
				fw.close();
			}
			catch (Exception e) {
				//Not really much else we can do
			}
		}
	}
	
	public static void generateOrganization() throws Exception {
		
		out("<organization xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
		
		out("	<organization_source_value>" + ++organizationId + "</organization_source_value>");
		out("	<x_data_source_type>EHR</x_data_source_type>");
		out("	<place_of_service_source_value>FQHC</place_of_service_source_value>");
		out("	<organization_address_1>" + getRandom(1, 9999) + " " + gen.getName(3).toUpperCase() + " ST</organization_address_1>");
		out("	<organization_address_2></organization_address_2>");
		out("	<organization_city>" + getRandom(cities) + "</organization_city>");
		out("	<organization_state>CO</organization_state>");
		out("	<organization_zip>" + getRandom(zips) + "</organization_zip>");
		out("	<organization_county>" + getRandom(counties) + "</organization_county>");
		
		int numberOfCareSites = CARE_SITES_PER_ORGANIZATION;
		for (int i = 0; i < numberOfCareSites; i++) {
			System.out.println("Generated care site " + (i+1) + " of " + numberOfCareSites + "...");
			generateCareSite();
		}
		
		int numberOfDemographics = DEMOGRAPHICS_PER_ORGANIZATION;
		for (int i = 0; i < numberOfDemographics; i++) {
			System.out.println("Generated demographic " + (i+1) + " of " + numberOfDemographics + "...");
			generateDemographic();
		}
		
		out("</organization>");
	}
	
	public static void generateCareSite() throws Exception {
		
		out("	<care_site>");
		out("		<care_site_source_value>" + ++careSiteId + "</care_site_source_value>");
		out("		<x_data_source_type>EHR</x_data_source_type>");
		out("		<organization_source_value>" + organizationId + "</organization_source_value>"); //
		out("		<place_of_service_source_value>FQHC</place_of_service_source_value>");
		out("		<x_care_site_name>" + gen.getName(4).toUpperCase() + " CLINIC</x_care_site_name>");
		out("		<care_site_address_1>" + getRandom(1, 9999) + " " + gen.getName(2).toUpperCase() + " ST</care_site_address_1>");
		out("		<care_site_address_2>Ste " + getRandom(1, 999) + "</care_site_address_2>");
		out("		<care_site_city>" + getRandom(cities) + "</care_site_city>");
		out("		<care_site_state>CO</care_site_state>");
		out("		<care_site_zip>" + getRandom(zips) + "</care_site_zip>");
		out("		<care_site_county>" + getRandom(counties) + "</care_site_county>");
		
		int numberOfProviders = MAX_PROVIDERS_PER_CARE_SITE;
		for (int i = 0; i < numberOfProviders; i++) {
			generateProvider();
		}
		
		out("	</care_site>");
	}
	
	public static void generateProvider() throws Exception {
		out("		<provider>");
		out("			<provider_source_value>" + ++providerId + "</provider_source_value>");
		out("			<x_data_source_type>EHR</x_data_source_type>");
		out("			<npi>" + getRandom(1000000000, 1999999999) + "</npi>");
		out("			<dea></dea>");
		out("			<specialty_source_value>" + getRandom(specialties)+ "</specialty_source_value>");
		out("			<x_provider_first>" + ("M".equals(getRandom(genders)) ? getRandom(maleNames) : getRandom(femaleNames)) + "</x_provider_first>");
		out("			<x_provider_middle>" + getRandom('A', 'Z') + "</x_provider_middle>");
		out("			<x_provider_last>" + getRandom(lastNames) + "</x_provider_last>");
		out("			<care_site_source_value>" + careSiteId + "</care_site_source_value>"); //
		out("			<x_organization_source_value>" + organizationId + "</x_organization_source_value>"); //
		out("		</provider>");
	}
	
	public static void generateDemographic() throws Exception {
		
		String gender = getRandom(genders);
		int month = getRandom(1, 12);
		out("	<x_demographic>");
		out("		<person_source_value>" + ++personId + "</person_source_value>");
		out("		<x_data_source_type>EHR</x_data_source_type>");
		out("		<medicaid_id_number></medicaid_id_number>");
		out("		<ssn>" + getRandom(100000000, 1999999999) + "</ssn>");
		out("		<last>" + getRandom(lastNames) + "</last>");
		out("		<middle>" + getRandom('A', 'Z') + "</middle>");
		out("		<first>" + getRandom(("M".equals(gender)) ? maleNames : femaleNames) + "</first>");
		out("		<address_1>" + getRandom(1, 9999) + " " + gen.getName(2).toUpperCase() + " ST</address_1>");
		out("		<address_2>APT " + getRandom(1, 999) + "</address_2>");
		out("		<city>" + getRandom(cities) + "</city>");
		out("		<state>CO</state>");
		out("		<zip>" + getRandom(zips) + "</zip>");
		out("		<county>" + getRandom(counties) + "</county>");
		out("		<year_of_birth>" + getRandom(1912, 2012) + "</year_of_birth>");
		out("		<month_of_birth>" + month + "</month_of_birth>");
		out("		<day_of_birth>" + getRandom(1, (month == 2 ? 28 : 30)) + "</day_of_birth>");
		out("		<gender_source_value>" + getRandom(genders) + "</gender_source_value>");
		out("		<race_source_value>" + getRandom(races) + "</race_source_value>");
		out("		<ethnicity_source_value>" + getRandom(ethnicities) + "</ethnicity_source_value>");
		out("		<provider_source_value>" + getRandom(1, providerId) + "</provider_source_value>"); //
		out("		<care_site_source_value>" + getRandom(1, careSiteId) + "</care_site_source_value>"); //
		out("		<x_organization_source_value>" + getRandom(1, organizationId) + "</x_organization_source_value>"); //
		
		visitDate = format.parse("2012-01-01");
		personVisitId = visitId + 1;
		int max = getRandom(1, VISITS_PER_PERSON);
		for (int i = 0; i < max; i++) {
			generateVisitOccurrence();
		}
		
		max = getRandom(1, OBSERVATIONS_PER_PERSON);
		for (int i = 0; i < max; i++) {
			generateObservation();
		}
		
		max = getRandom(1, DRUGS_PER_PERSON);
		for (int i = 0; i < max; i++) {
			generateDrugExposure();
		}
		
		max = getRandom(1, CONDITIONS_PER_PERSON);
		for (int i = 0; i < max; i++) {
			generateConditionOccurrence();
		}
		
		max = getRandom(1, PROCEDURES_PER_PERSON);
		for (int i = 0; i < max; i++) {
			generateProcedureOccurrence();
		}
		
//		max = rnd.nextInt(5);
//		for (int i = 0; i < max; i++) {
//			generateCohort();
//		}
//		
//		max = rnd.nextInt(5);
//		for (int i = 0; i < max; i++) {
//			generateDeath();
//		}
//		
//		max = rnd.nextInt(5);
//		for (int i = 0; i < max; i++) {
//			generatePayerPlanPeriod();
//		}
		
		out("	</x_demographic>");
	}
	
	public static void generateVisitOccurrence() throws Exception {
		out("		<visit_occurrence>");
		out("			<x_visit_occurrence_source_identifier>" + ++visitId + "</x_visit_occurrence_source_identifier>");
		out("			<x_data_source_type>EHR</x_data_source_type>");
		out("			<person_source_value>" + personId + "</person_source_value>");
		out("			<visit_start_date>" + format.format(visitDate) + "</visit_start_date>");
		out("			<visit_end_date>" + format.format(visitDate) + "</visit_end_date>");
		out("			<place_of_service_source_value>" + getRandom(placesOfService) + "</place_of_service_source_value>");
		out("			<x_provider_source_value>" + getRandom(1, providerId) + "</x_provider_source_value>"); //
		out("			<care_site_source_value>" + getRandom(1, careSiteId) + "</care_site_source_value>"); //
		out("		</visit_occurrence>");
		Calendar cal = Calendar.getInstance();
		cal.setTime(visitDate);
		cal.add(Calendar.DATE, 1);
		visitDate = cal.getTime();									
	}
	
	public static void generateObservation() throws Exception {
		out("		<observation>");
		out("			<observation_source_identifier>" + ++observationId + "</observation_source_identifier>");
		out("			<x_data_source_type>EHR</x_data_source_type>");
		out("			<person_source_value>" + personId + "</person_source_value>");
		out("			<observation_source_value>" + getRandom(observations) + "</observation_source_value>");
		out("			<observation_source_value_vocabulary>CUSTOM</observation_source_value_vocabulary>");
		out("			<observation_date>" + format.format(visitDate) + "</observation_date>");
		out("			<observation_time>" + format.format(visitDate) + "T12:00:00Z</observation_time>");
		out("			<value_as_number>" + String.format("%4.3f", getRandom(1.0, 1000.0)) + "</value_as_number>");
		out("			<value_as_string>" + getRandom(observationValues) + "</value_as_string>");
		out("			<unit_source_value></unit_source_value>");
		out("			<range_low xsi:nil=\"true\"/>");
		out("			<range_high xsi:nil=\"true\"/>");
		out("			<observation_type_source_value>" + getRandom(observationTypes) + "</observation_type_source_value>");
		out("			<associated_provider_source_value>" + getRandom(1, providerId) + "</associated_provider_source_value>"); //
		out("			<x_visit_occurrence_source_identifier>" + getRandom(personVisitId, visitId) + "</x_visit_occurrence_source_identifier>"); //
		out("			<relevant_condition_source_value></relevant_condition_source_value>"); //
		out("			<x_obs_comment></x_obs_comment>");
		out("		</observation>");
	}
	
	public static void generateDrugExposure() throws Exception {
		out("		<drug_exposure>");
		out("			<drug_exposure_source_identifier>" + drugId++ + "</drug_exposure_source_identifier>");
		out("			<x_data_source_type>EHR</x_data_source_type>");
		out("			<person_source_value>" + personId + "</person_source_value>");
		out("			<drug_source_value>" + getRandom(drugs) + "</drug_source_value>");
		out("			<drug_source_value_vocabulary>NDC</drug_source_value_vocabulary>");
		out("			<drug_exposure_start_date>" + format.format(visitDate) + "</drug_exposure_start_date>");
		out("			<drug_exposure_end_date xsi:nil=\"true\"/>");
		out("			<drug_type_source_value>Prescription</drug_type_source_value>");
		out("			<stop_reason></stop_reason>");
		out("			<refills>" + rnd.nextInt(10) + "</refills>");
		out("			<quantity>" + String.format("%3.2f", getRandom(1.0, 500.0)) + "</quantity>");
		out("			<days_supply xsi:nil=\"true\"/>");
		out("			<x_drug_name>" + getRandom(drugNames) + "</x_drug_name>");
		out("			<x_drug_strength>" + getRandom(drugStrengths) + "</x_drug_strength>");
		out("			<sig>" + getRandom(drugSigs) + "</sig>");
		out("			<provider_source_value>" + getRandom(1, providerId) + "</provider_source_value>"); //
		out("			<x_visit_occurrence_source_identifier>" + getRandom(personVisitId, visitId) + "</x_visit_occurrence_source_identifier>"); //
		out("			<relevant_condition_source_value></relevant_condition_source_value>"); //
		
//		int numberOfCosts = rnd.nextInt(5);
//		for (int i = 0; i < numberOfCosts; i++) {
//			generateDrugCost();
//		}
		
		out("		</drug_exposure>");
	}
	
//	public static void generateDrugCost() throws Exception {
//		out("			<drug_cost>");
//		out("				<drug_cost_source_identifier>" + idSeed++ + "</drug_cost_source_identifier>");
//		out("				<drug_exposure_source_identifier>" + idSeed++ + "</drug_exposure_source_identifier>"); //
//		out("				<paid_copay>0.50</paid_copay>");
//		out("				<paid_coinsurance>0.10</paid_coinsurance>");
//		out("				<paid_toward_deductible>0.20</paid_toward_deductible>");
//		out("				<paid_by_payer>0.50</paid_by_payer>");
//		out("				<paid_by_coordination_benefits>0.30</paid_by_coordination_benefits>");
//		out("				<total_out_of_pocket>0.40</total_out_of_pocket>");
//		out("				<total_paid>0.50</total_paid>");
//		out("				<ingredient_cost>0.01</ingredient_cost>");
//		out("				<dispensing_fee>0.99</dispensing_fee>");
//		out("				<average_wholesale_price>0.01</average_wholesale_price>");
//		out("				<payer_plan_period_source_identifier>" + idSeed++ + "</payer_plan_period_source_identifier>"); //
//		out("			</drug_cost>");
//	}
	
	public static void generateConditionOccurrence() throws Exception {
		out("		<condition_occurrence>");
		out("			<condition_occurrence_source_identifier>" + conditionId++ + "</condition_occurrence_source_identifier>"); //
		out("			<x_data_source_type>EHR</x_data_source_type>");
		out("			<person_source_value>" + personId + "</person_source_value>"); //
		out("			<condition_source_value>" + getRandom(conditions) + "</condition_source_value>");
		out("			<condition_source_value_vocabulary>ICD9</condition_source_value_vocabulary>");
		out("			<x_condition_source_desc></x_condition_source_desc>");
		out("			<condition_start_date>" + format.format(visitDate) + "</condition_start_date>");
		out("			<x_condition_update_date xsi:nil=\"true\"/>");
		out("			<condition_end_date xsi:nil=\"true\"/>");
		out("			<condition_type_source_value></condition_type_source_value>");
		out("			<stop_reason></stop_reason>");
		out("			<associated_provider_source_value>" + getRandom(1, providerId) + "</associated_provider_source_value>"); //
		out("			<x_visit_occurrence_source_identifier>" + getRandom(personVisitId, visitId) + "</x_visit_occurrence_source_identifier>"); //
		out("		</condition_occurrence>");
	}
	
	public static void generateProcedureOccurrence() throws Exception {
		out("		<procedure_occurrence>");
		out("			<procedure_occurrence_source_identifier>" + procedureId++ + "</procedure_occurrence_source_identifier>");
		out("			<x_data_source_type>EHR</x_data_source_type>");
		out("			<person_source_value>" + personId + "</person_source_value>"); //
		String procedure = getRandom(procedures);
		out("			<procedure_source_value>" + procedure + "</procedure_source_value>");
		out("			<procedure_source_value_vocabulary>" + (procedure.startsWith("LAB") ? "HCPCS" : "CPT") + "</procedure_source_value_vocabulary>");
		out("			<procedure_date>" + format.format(visitDate) + "</procedure_date>");
		out("			<procedure_type_source_value></procedure_type_source_value>");
		out("			<provider_record_source_value>" + getRandom(1, providerId) + "</provider_record_source_value>"); //
		out("			<x_visit_occurrence_source_identifier>" + getRandom(personVisitId, visitId) + "</x_visit_occurrence_source_identifier>"); //
		out("			<relevant_condition_source_value></relevant_condition_source_value>"); //
		
//		int numberOfCosts = rnd.nextInt(5);
//		for (int i = 0; i < numberOfCosts; i++) {
//			generateProcedureCost();
//		}
		
		out("		</procedure_occurrence>");
	}
	
//	public static void generateProcedureCost() throws Exception {
//		out("			<procedure_cost>");
//		out("				<procedure_cost_source_identifier>" + idSeed++ + "</procedure_cost_source_identifier>"); //
//		out("				<procedure_occurrence_source_identifier>" + idSeed++ + "</procedure_occurrence_source_identifier>"); //
//		out("				<paid_copay>50.00</paid_copay>");
//		out("				<paid_coinsurance>0.10</paid_coinsurance>");
//		out("				<paid_toward_deductible>0.20</paid_toward_deductible>");
//		out("				<paid_by_payer>200.00</paid_by_payer>");
//		out("				<paid_by_coordination_benefits>0.30</paid_by_coordination_benefits>");
//		out("				<total_out_of_pocket>0.40</total_out_of_pocket>");
//		out("				<total_paid>250.00</total_paid>");
//		out("				<disease_class_concept_id>0</disease_class_concept_id>");
//		out("				<disease_class_source_value>" + idSeed++ + "</disease_class_source_value>"); //
//		out("				<revenue_code_concept_id>0</revenue_code_concept_id>");
//		out("				<revenue_code_source_value>A</revenue_code_source_value>");
//		out("				<payer_plan_period_source_identifier>" + idSeed++ + "</payer_plan_period_source_identifier>"); //
//		out("			</procedure_cost>");
//	}
//	
//	public static void generateCohort() throws Exception {
//		out("		<cohort>");
//		out("			<cohort_source_identifier>" + idSeed++ + "</cohort_source_identifier>"); //
//		out("			<cohort_source_value>Hospitalization</cohort_source_value>");
//		out("			<cohort_start_date>2012-01-02</cohort_start_date>");
//		out("			<cohort_end_date>2012-01-02</cohort_end_date>");
//		out("			<subject_source_identifier>" + idSeed++ + "</subject_source_identifier>"); //
//		out("			<stop_reason>Stopped</stop_reason>");
//		out("		</cohort>");
//	}
//	
//	public static void generateDeath() throws Exception {
//		out("		<death>");
//		out("			<person_source_value>" + idSeed++ + "</person_source_value>"); //
//		out("			<death_date>2012-01-03</death_date>");
//		out("			<death_type_concept_id>0</death_type_concept_id>");
//		out("			<death_type_source_value>Deceased</death_type_source_value>");
//		out("			<cause_of_death_source_value>DEAD</cause_of_death_source_value>");
//		out("		</death>");
//	}
//	
//	public static void generatePayerPlanPeriod() throws Exception {
//		out("		<payer_plan_period>");
//		out("			<payer_plan_period_source_identifier>" + idSeed++ + "</payer_plan_period_source_identifier>"); //
//		out("			<person_source_value>" + idSeed++ + "</person_source_value>"); //
//		out("			<payer_plan_period_start_date>2012-01-01</payer_plan_period_start_date>");
//		out("			<payer_plan_period_end_date>2012-12-31</payer_plan_period_end_date>");
//		out("			<payer_source_value>Progressive Insurance</payer_source_value>");
//		out("			<plan_source_value>XNA</plan_source_value>");
//		out("			<family_source_value>A</family_source_value>");
//		out("		</payer_plan_period>");
//	}
	
	public static void out(String out) throws Exception {
		fw.write(out + "\n");
	}
	
	public static String getGenderLetter() throws Exception {
		if (rnd.nextInt(2) == 1) {
			return "M";
		}
		return "F";
	}
	
	public static int getRandom(int start, int end) {
		
		if (start == end) {
			return start;
		}
		return start + rnd.nextInt(end - start);
		
	}
	
	public static double getRandom(double start, double end) {
		
		if (start == end) {
			return start;
		}
		return start + rnd.nextDouble() * (end - start);
		
	}

	public static char getRandom(char start, char end) {
		
		if (start == end) {
			return start;
		}
		return (char) getRandom((int) start, (int) end);
		
	}
	
	public static String getRandom(String[] values) throws Exception {
		
		return values[rnd.nextInt(values.length)];
		
	}

	
}
