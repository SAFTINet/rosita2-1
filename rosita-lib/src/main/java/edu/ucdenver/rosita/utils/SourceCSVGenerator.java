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

public class SourceCSVGenerator {
	
	//static FileWriter fw = null;
	static FileWriter fw_x_demographic = null;
	static FileWriter fw_visit_occurrence = null;
	static FileWriter fw_procedure_occurrence = null;
	static FileWriter fw_procedure_cost = null;
	static FileWriter fw_condition_occurrence = null;
	static FileWriter fw_observation = null;
	static FileWriter fw_drug_exposure = null;
	static FileWriter fw_drug_cost = null;
	static FileWriter fw_provider = null;
	static FileWriter fw_care_site = null;
	static FileWriter fw_organization = null;
	static FileWriter fw_payer_plan_period = null;
	static FileWriter fw_death = null;
	static FileWriter fw_cohort = null;
	
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
	static int VISITS_PER_PERSON = 4;
	static int CONDITIONS_PER_PERSON = 2;
	static int PROCEDURES_PER_PERSON = 3;
	static int DRUGS_PER_PERSON = 3;
	static int OBSERVATIONS_PER_PERSON = 2;
	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	public static void main(String[] args) {
		generate(args);
	}
	
	public static void generate(String[] args) {
		System.out.println("Generating...");
			
		try {
			if (args.length < 3) {
				System.out.println("Please provide on command line: directory name, demographics, care sites");
				System.exit(0);
			}
			
			boolean mkdirsResult = new File(args[1] + "/").mkdirs();
			if (!mkdirsResult) {
				System.out.println("Count not create directory: " + args[1]);
			}
			
			fw_x_demographic = new FileWriter(new File(args[1] + "/x_demographic.csv"));
			fw_visit_occurrence = new FileWriter(new File(args[1] + "/visit_occurrence.csv"));
			fw_procedure_occurrence = new FileWriter(new File(args[1] + "/procedure_occurrence.csv"));
			fw_procedure_cost = new FileWriter(new File(args[1] + "/procedure_cost.csv"));
			fw_condition_occurrence = new FileWriter(new File(args[1] + "/condition_occurrence.csv"));
			fw_observation = new FileWriter(new File(args[1] + "/observation.csv"));
			fw_drug_exposure = new FileWriter(new File(args[1] + "/drug_exposure.csv"));
			fw_drug_cost = new FileWriter(new File(args[1] + "/drug_cost.csv"));
			fw_provider = new FileWriter(new File(args[1] + "/provider.csv"));
			fw_care_site = new FileWriter(new File(args[1] + "/care_site.csv"));
			fw_organization = new FileWriter(new File(args[1] + "/organization.csv"));
			fw_payer_plan_period = new FileWriter(new File(args[1] + "/payer_plan_period.csv"));
			fw_death = new FileWriter(new File(args[1] + "/death.csv"));
			fw_cohort = new FileWriter(new File(args[1] + "/cohort.csv"));
			
			DEMOGRAPHICS_PER_ORGANIZATION = Integer.parseInt(args[2]);
			CARE_SITES_PER_ORGANIZATION = Integer.parseInt(args[3]);

			
			for (int i = 0; i < ORGANIZATIONS; i++) {
				generateOrganization();
				System.out.println("Generated organization " + (i+1) + " of " + ORGANIZATIONS + "...");
			}
			System.out.println("Clinical CSV generation complete");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				//fw.flush();
				//fw.close();
				
				fw_x_demographic.flush();
				fw_visit_occurrence.flush();
				fw_procedure_occurrence.flush();
				fw_procedure_cost.flush();
				fw_condition_occurrence.flush();
				fw_observation.flush();
				fw_drug_exposure.flush();
				fw_drug_cost.flush();
				fw_provider.flush();
				fw_care_site.flush();
				fw_organization.flush();
				fw_payer_plan_period.flush();
				fw_death.flush();
				fw_cohort.flush();
				
				fw_x_demographic.close();
				fw_visit_occurrence.close();
				fw_procedure_occurrence.close();
				fw_procedure_cost.close();
				fw_condition_occurrence.close();
				fw_observation.close();
				fw_drug_exposure.close();
				fw_drug_cost.close();
				fw_provider.close();
				fw_care_site.close();
				fw_organization.close();
				fw_payer_plan_period.close();
				fw_death.close();
				fw_cohort.close();
			}
			catch (Exception e) {
				//Not really much else we can do
			}
		}
	}
	
	public static void generateOrganization() throws Exception {
		
		organization_out(++organizationId + "|EHR|FQHC|"+ getRandom(1, 9999) + " " + gen.getName(3).toUpperCase() + " ST|" + gen.getName(4) + "|" + getRandom(cities) + "|CO|"+ getRandom(zips) + "|" + getRandom(counties));
		
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
		
	}
	
	public static void generateCareSite() throws Exception {
		
		care_site_out(++careSiteId + "|EHR|" + organizationId + "|FQHC|"+ gen.getName(4).toUpperCase() + " CLINIC|" + getRandom(1, 9999) + " " + gen.getName(2).toUpperCase() + " ST|Ste " + getRandom(1, 999) + "|" + getRandom(cities) + "|CO|" + getRandom(zips) + "|" + getRandom(counties));
		
		int numberOfProviders = MAX_PROVIDERS_PER_CARE_SITE;
		for (int i = 0; i < numberOfProviders; i++) {
			generateProvider();
		}
	}
	
	public static void generateProvider() throws Exception {
		provider_out(++providerId + "|EHR|" + getRandom(1000000000, 1999999999) + "|" + getRandom(100000, 199999) + "|" + getRandom(specialties)+ "|" + ("M".equals(getRandom(genders)) ? getRandom(maleNames) : getRandom(femaleNames)) + "|" + getRandom('A', 'Z') + "|" + getRandom(lastNames) + "|" + careSiteId + "|" + organizationId); //
	}
	
	public static void generateDemographic() throws Exception {
		
		String gender = getRandom(genders);
		int month = getRandom(1, 12);
		x_demographic_out(++personId + "|EHR|" + getRandom(1000000000, 1999999999) + "|" + getRandom(100000000, 1999999999) + "|" + getRandom(lastNames) + "|" + getRandom('A', 'Z') + "|" + getRandom(("M".equals(gender)) ? maleNames : femaleNames) + "|" + getRandom(1, 9999) + " " + gen.getName(2).toUpperCase() + " ST|APT " + getRandom(1, 999) + "|" + getRandom(cities) + "|CO|" + getRandom(zips) + "|" + getRandom(counties) + "|" + getRandom(1912, 2012) + "|" + month + "|" + getRandom(1, (month == 2 ? 28 : 30)) + "|" + getRandom(genders) + "|" + getRandom(races) + "|" + getRandom(ethnicities) + "|" + getRandom(1, providerId) + "|" + getRandom(1, careSiteId) + "|" + getRandom(1, organizationId)); //
		
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
	}
	
	public static void generateVisitOccurrence() throws Exception {
		visit_occurrence_out(++visitId + "|EHR|" + personId + "|" + format.format(visitDate) + "|" + format.format(visitDate) + "|" + getRandom(placesOfService) + "|" + getRandom(1, providerId) + "|" + getRandom(1, careSiteId)); //
		Calendar cal = Calendar.getInstance();
		cal.setTime(visitDate);
		cal.add(Calendar.DATE, 1);
		visitDate = cal.getTime();									
	}
	
	public static void generateObservation() throws Exception {
		observation_out(++observationId + "|" +
		"EHR|" +
		personId + "|" +
		getRandom(observations) + "|" +
		"CUSTOM|" +
		format.format(visitDate) + "|" +
		format.format(visitDate) + "T12:00:00Z|" +
		String.format("%4.3f", getRandom(1.0, 1000.0)) + "|" +
		getRandom(observationValues) + "|" +
		rnd.nextInt(50) + "|" +
		rnd.nextInt(50) + "|" +
		rnd.nextInt(50) + "|" +
		getRandom(observationTypes) + "|" +
		getRandom(1, providerId) + "|" +
		getRandom(personVisitId, visitId) + "|" +
		"Comment!" + gen.getName(4) + "|");
	}
	
	public static void generateDrugExposure() throws Exception {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(visitDate);
		Date startDate = cal.getTime();
		cal.add(Calendar.DATE, rnd.nextInt(20));
		Date endDate = cal.getTime();
		
		drug_exposure_out(drugId++ + "|" +
		"EHR|" +
		personId + "|" +
		getRandom(drugs) + "|" +
		"NDC|" +
		format.format(startDate) + "|" +
		format.format(endDate) + "|" +
		"Prescription|" +
		"REASON:" + maleNames[rnd.nextInt(maleNames.length)] + "|" +
		rnd.nextInt(10) + "|" +
		String.format("%3.2f", getRandom(1.0, 500.0)) + "|" +
		rnd.nextInt(30) + "|" +
		getRandom(drugNames) + "|" +
		getRandom(drugStrengths) + "|" +
		getRandom(drugSigs) + "|" +
		getRandom(1, providerId) + "|" + //
		getRandom(personVisitId, visitId) + "|"); //
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
		Calendar cal = Calendar.getInstance();
		cal.setTime(visitDate);
		Date updateDate = new Date(visitDate.getTime() + rnd.nextInt(525600000));
		Date endDate = new Date(updateDate.getTime() - rnd.nextInt(525600));
		String condition = getRandom(conditions);
		
		condition_occurrence_out(conditionId++ + "|" +
		"EHR|" +
		personId + "|" +
		condition + "|" +
		"ICD9|" +
		"Condition Source Description|" +
		format.format(visitDate) + "|" +
		format.format(updateDate) + "|" +
		format.format(endDate) + "|" +
		condition + "|" +
		"Got better|" +
		getRandom(1, providerId) + "|" +
		getRandom(personVisitId, visitId)); //
	}
	
	public static void generateProcedureOccurrence() throws Exception {
		String procedure = getRandom(procedures);	
		procedure_occurrence_out(procedureId++ +  "|" +
		"EHR|" +
		personId + "|" +
		procedure + "|" +
		(procedure.startsWith("LAB") ? "HCPCS" : "CPT") +  "|" +
		format.format(visitDate) + "|" +
		getRandom(100000, 199999) + "|" +
		getRandom(1, providerId) + "|" +
		getRandom(personVisitId, visitId) +  "|" );

		
//		int numberOfCosts = rnd.nextInt(5);
//		for (int i = 0; i < numberOfCosts; i++) {
//			generateProcedureCost();
//		}
		

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
	
	//public static void out(String out) throws Exception {
		//fw.write(out + "\n");
	//}
	
	public static void x_demographic_out(String out) throws Exception {
		fw_x_demographic.write(out + "\n");
	}
	
	public static void visit_occurrence_out(String out) throws Exception {
		fw_visit_occurrence.write(out + "\n");
	}
	public static void procedure_occurrence_out(String out) throws Exception {
		fw_procedure_occurrence.write(out + "\n");
	}
	public static void procedure_cost_out(String out) throws Exception {
		fw_procedure_cost.write(out + "\n");
	}
	public static void condition_occurrence_out(String out) throws Exception {
		fw_condition_occurrence.write(out + "\n");
	}
	public static void observation_out(String out) throws Exception {
		fw_observation.write(out + "\n");
	}
	public static void drug_exposure_out(String out) throws Exception {
		fw_drug_exposure.write(out + "\n");
	}
	public static void drug_cost_out(String out) throws Exception {
		fw_drug_cost.write(out + "\n");
	}
	public static void provider_out(String out) throws Exception {
		fw_provider.write(out + "\n");
	}
	public static void care_site_out(String out) throws Exception {
		fw_care_site.write(out + "\n");
	}
	public static void organization_out(String out) throws Exception {
		fw_organization.write(out + "\n");
	}
	public static void payer_plan_period_out(String out) throws Exception {
		fw_payer_plan_period.write(out + "\n");
	}
	public static void death_out(String out) throws Exception {
		fw_death.write(out + "\n");
	}
	public static void cohort_out(String out) throws Exception {
		fw_cohort.write(out + "\n");
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
