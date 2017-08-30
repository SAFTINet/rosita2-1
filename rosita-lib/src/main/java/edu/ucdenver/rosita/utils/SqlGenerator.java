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
import java.util.Date;
import java.util.Random;

public class SqlGenerator {
	
	static FileWriter fw = null;
	static Random rnd = new Random();
	static Long idSeed = 0L;
	static NameGenerator gen = new NameGenerator();
	
	static String[] cities1 = {"Spark", "Chip", "Mud", "Mad", "Aber", "Bas", "Carl", "Dalt", "Even", "Findl", "Gork", "Hat", "Igloo", "James", "Keen", "Lough", "Mert", "Narm", "Os", "Park", "Ross", "Rub", "Rex", "Tend", "Tart"};
	static String[] cities2 = {"ing", "er", "for", "ough"};
	static String[] cities3 = {"ville", "ton", " City", "borough", "burgh", "deen", "don", "gow", "gough", "bridge", "river"};
	
	static int ORGANIZATIONS = 1;
	static int DEMOGRAPHICS_PER_ORGANIZATION = 200000;
	static int CARE_SITES_PER_ORGANIZATION = 10;
	static int MAX_PROVIDERS_PER_CARE_SITE = 10;
	
	public static void main(String[] args) {
		System.out.println("Give us a minute...");
		
		try {
			if (args.length == 0) {
				System.out.println("I need a filename!");
				System.exit(0);
			}
			fw = new FileWriter(new File(args[0]));
			
			out("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			
			for (int i = 0; i < 1000; i++) {
				generateCareSite();
			}
			for (int i = 0; i < 1000; i++) {
				generateVisitOccurrence();
			}
			
			System.out.println("All done - good luck!");
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
		
		out("<organization>");
		
		out("	<organization_source_value>" + idSeed++ + "</organization_source_value>");
		out("	<x_data_source_type>GEN</x_data_source_type>");
		out("	<place_of_service_source_value>Other Place of Service</place_of_service_source_value>");
		out("	<organization_address_1>" + rnd.nextInt(999) + " " + gen.getName(3) + " St</organization_address_1>");
		out("	<organization_address_2>Ste " + rnd.nextInt(999) + "</organization_address_2>");
		out("	<organization_city>The Moon</organization_city>");
		out("	<organization_state>ZZ</organization_state>");
		out("	<organization_zip>" + rnd.nextInt(99999) + "</organization_zip>");
		out("	<organization_county>County</organization_county>");
		
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
		
		out("INSERT INTO lz.lz_src_care_site (care_site_id, care_site_address_1, care_site_address_2, care_site_city, care_site_county, care_site_source_value, care_site_state, care_site_zip, organization_id, organization_source_value, place_of_service_source_value, x_care_site_name, x_data_source_type)" +
				" VALUES (" + idSeed++ + ","
				+ "'" + rnd.nextInt(999) + " " + gen.getName(2) + " St',"
				+ "'Ste " + rnd.nextInt(999) + "',"
				+ "'" + getCityName() + "',"
				+ "'" + getCityName() + "',"
				+ "'1',"
				+ "'ZZ',"
				+ "'" + rnd.nextInt(99999) + "',"
				+ "'1',"
				+ "'1',"
				+ "'Inpatient Hospital',"
				+ "'" + gen.getName(4) + " Medical Center',"
				+ "'GEN');");
	}
	
	public static void generateProvider() throws Exception {
		out("		<provider>");
		out("			<provider_source_value>" + idSeed++ + "</provider_source_value>");
		out("			<x_data_source_type>GEN</x_data_source_type>");
		out("			<npi>" + rnd.nextInt(99999999) + "</npi>");
		out("			<dea>" + rnd.nextInt(99999999) + "</dea>");
		out("			<specialty_source_value>Internal Medicine</specialty_source_value>");
		out("			<x_provider_first>" + gen.getName(3) + "</x_provider_first>");
		out("			<x_provider_middle>" + gen.getName(1) + "</x_provider_middle>");
		out("			<x_provider_last>" + gen.getName(5) + "</x_provider_last>");
		out("			<care_site_source_value>" + idSeed++ + "</care_site_source_value>"); //
		out("			<x_organization_source_value>" + idSeed++ + "</x_organization_source_value>"); //
		out("		</provider>");
	}
	
	public static void generateDemographic() throws Exception {
		out("	<x_demographic>");
		out("		<person_source_value>" + idSeed++ + "</person_source_value>");
		out("		<x_data_source_type>GEN</x_data_source_type>");
		out("		<medicaid_id_number>" + rnd.nextInt(99999999) + "</medicaid_id_number>");
		out("		<ssn>" + rnd.nextInt(99999999) + "</ssn>");
		out("		<last>" + gen.getName(5) + "</last>");
		out("		<middle>" + gen.getName(1) + "</middle>");
		out("		<first>" + gen.getName(3) + "</first>");
		out("		<address_1>" + rnd.nextInt(999) + " " + gen.getName(2) + " St</address_1>");
		out("		<address_2>Apt " + rnd.nextInt(9) + "</address_2>");
		out("		<city>" + gen.getName(3) + "</city>");
		out("		<state>ZZ</state>");
		out("		<zip>" + rnd.nextInt(99999) + "</zip>");
		out("		<county>County</county>");
		out("		<year_of_birth>" + (1900 + rnd.nextInt(112)) + "</year_of_birth>");
		out("		<month_of_birth>" + (rnd.nextInt(12) + 1) + "</month_of_birth>");
		out("		<day_of_birth>" + (rnd.nextInt(28) + 1) + "</day_of_birth>");
		out("		<gender_source_value> " + getGenderLetter() + "</gender_source_value>");
		out("		<race_source_value>X</race_source_value>");
		out("		<ethnicity_source_value>X</ethnicity_source_value>");
		out("		<provider_source_value>" + idSeed++ + "</provider_source_value>"); //
		out("		<care_site_source_value>" + idSeed++ + "</care_site_source_value>"); //
		out("		<x_organization_source_value>" + idSeed++ + "</x_organization_source_value>"); //
		
		int max = rnd.nextInt(10);
		for (int i = 0; i < max; i++) {
			generateVisitOccurrence();
		}
		
		max = rnd.nextInt(10);
		for (int i = 0; i < max; i++) {
			generateObservation();
		}
		
		max = rnd.nextInt(10);
		for (int i = 0; i < max; i++) {
			generateDrugExposure();
		}
		
		max = rnd.nextInt(10);
		for (int i = 0; i < max; i++) {
			generateConditionOccurrence();
		}
		
		max = rnd.nextInt(10);
		for (int i = 0; i < max; i++) {
			generateProcedureOccurrence();
		}
		
		max = rnd.nextInt(10);
		for (int i = 0; i < max; i++) {
			generateCohort();
		}
		
		max = rnd.nextInt(1);
		for (int i = 0; i < max; i++) {
			generateDeath();
		}
		
		max = rnd.nextInt(10);
		for (int i = 0; i < max; i++) {
			generatePayerPlanPeriod();
		}
		
		out("	</x_demographic>");
	}
	
	public static void generateVisitOccurrence() throws Exception {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date now = new Date();
		long dateLong = now.getTime();
		long startDateLong = dateLong - ((long) rnd.nextInt(Integer.MAX_VALUE))*1000;
		long endDateLong = startDateLong + ((long) rnd.nextInt(Integer.MAX_VALUE))*1000;
		
		out("INSERT INTO lz.lz_src_visit_occurrence(visit_occurrence_id, care_site_source_value, x_demographic_id, person_source_value, place_of_service_source_value, visit_end_date, visit_occurrence_source_identifier, visit_start_date, x_data_source_type, x_provider_source_value) VALUES (" +
				 + idSeed++ + ","
				 + "'" + rnd.nextInt(99999) + "'" + ","
				 + "'" + rnd.nextInt(99999) + "'" + ","
				 + "'" + rnd.nextInt(99999) + "'" + ","
				 + "'" + rnd.nextInt(99999) + "'" + ","
				 + "'" + sdf.format(new Date(endDateLong)) + "'" + ","
				 + "'" + rnd.nextInt(99999) + "'" + ","
				 + "'" + sdf.format(new Date(startDateLong)) + "'" + ","
				 + "'" + rnd.nextInt(99999) + "'" + ","
				 + "'" + rnd.nextInt(99999) + "'"
				 + ");"
				 );
	}
	
	public static void generateObservation() throws Exception {
		out("		<observation>");
		out("			<observation_source_identifier>" + idSeed++ + "</observation_source_identifier>");
		out("			<x_data_source_type>GEN</x_data_source_type>");
		out("			<person_source_value>900001001</person_source_value>");
		out("			<observation_source_value>2345-7</observation_source_value>");
		out("			<observation_source_value_vocabulary>LOINC</observation_source_value_vocabulary>");
		out("			<observation_date>2012-01-02</observation_date>");
		out("			<observation_time>12:00</observation_time>");
		out("			<value_as_number>100.000</value_as_number>");
		out("			<value_as_string>100.000</value_as_string>");
		out("			<units_source_value>mmol/L</units_source_value>");
		out("			<range_low>70</range_low>");
		out("			<range_high>125</range_high>");
		out("			<observation_type_source_value>Lab observation numeric result</observation_type_source_value>");
		out("			<associated_provider_source_value>" + idSeed++ + "</associated_provider_source_value>"); //
		out("			<visit_occurrence_source_value>" + idSeed++ + "</visit_occurrence_source_value>"); //
		out("			<relevant_condition_source_value>" + idSeed++ + "</relevant_condition_source_value>"); //
		out("			<x_obs_comment>Glucose</x_obs_comment>");
		out("		</observation>");
	}
	
	public static void generateDrugExposure() throws Exception {
		out("		<drug_exposure>");
		out("			<drug_exposure_source_identifier>" + idSeed++ + "</drug_exposure_source_identifier>");
		out("			<x_data_source_type>GEN</x_data_source_type>");
		out("			<person_source_value>900001001</person_source_value>");
		out("			<drug_source_value>50580-451-03</drug_source_value>");
		out("			<drug_source_value_vocabulary>NDC</drug_source_value_vocabulary>");
		out("			<drug_exposure_start_date>2012-01-02</drug_exposure_start_date>");
		out("			<drug_exposure_end_date>2012-01-03</drug_exposure_end_date>");
		out("			<drug_type_source_value>Inpatient administration</drug_type_source_value>");
		out("			<stop_reason>Regimen completed</stop_reason>");
		out("			<refills>0</refills>");
		out("			<quantity>2</quantity>");
		out("			<days_supply>1</days_supply>");
		out("			<x_drug_name>TYLENOL EXTRA STRENGTH acetaminophen tablet, coated</x_drug_name>");
		out("			<x_drug_strength>500mg</x_drug_strength>");
		out("			<sig>ad</sig>");
		out("			<provider_source_value>" + idSeed++ + "</provider_source_value>"); //
		out("			<visit_occurrence_source_identifier>" + idSeed++ + "</visit_occurrence_source_identifier>"); //
		out("			<relevant_condition_source_value>" + idSeed++ + "</relevant_condition_source_value>"); //
		
		int numberOfCosts = rnd.nextInt(5);
		for (int i = 0; i < numberOfCosts; i++) {
			generateDrugCost();
		}
		
		out("		</drug_exposure>");
	}
	
	public static void generateDrugCost() throws Exception {
		out("			<drug_cost>");
		out("				<drug_cost_source_identifier>" + idSeed++ + "</drug_cost_source_identifier>");
		out("				<drug_exposure_source_identifier>" + idSeed++ + "</drug_exposure_source_identifier>"); //
		out("				<paid_copay>0.50</paid_copay>");
		out("				<paid_coinsurance>0.00</paid_coinsurance>");
		out("				<paid_toward_deductible>0.00</paid_toward_deductible>");
		out("				<paid_by_payer>0.50</paid_by_payer>");
		out("				<paid_by_coordination_benefits>0.00</paid_by_coordination_benefits>");
		out("				<total_out_of_pocket>0.00</total_out_of_pocket>");
		out("				<total_paid>0.50</total_paid>");
		out("				<ingredient_cost>0.01</ingredient_cost>");
		out("				<dispensing_fee>0.49</dispensing_fee>");
		out("				<average_wholesale_price>0.01</average_wholesale_price>");
		out("				<payer_plan_period_source_identifier>" + idSeed++ + "</payer_plan_period_source_identifier>"); //
		out("			</drug_cost>");
	}
	
	public static void generateConditionOccurrence() throws Exception {
		out("		<condition_occurrence>");
		out("			<condition_occurrence_source_identifier>" + idSeed++ + "</condition_occurrence_source_identifier>"); //
		out("			<x_data_source_type>GEN</x_data_source_type>");
		out("			<person_source_value>" + idSeed++ + "</person_source_value>"); //
		out("			<condition_source_value>339</condition_source_value>");
		out("			<condition_source_value_vocabulary>ICD-9</condition_source_value_vocabulary>");
		out("			<x_condition_source_desc>Other headache syndromes</x_condition_source_desc>");
		out("			<condition_start_date>2012-01-02</condition_start_date>");
		out("			<x_condition_update_date>2012-01-03</x_condition_update_date>");
		out("			<condition_end_date>2012-01-03</condition_end_date>");
		out("			<condition_type_source_value>Primary</condition_type_source_value>");
		out("			<stop_reason>Deceased</stop_reason>");
		out("			<associated_provider_source_value>" + idSeed++ + "</associated_provider_source_value>"); //
		out("			<visit_occurrence_source_value>" + idSeed++ + "</visit_occurrence_source_value>"); //
		out("		</condition_occurrence>");
	}
	
	public static void generateProcedureOccurrence() throws Exception {
		out("		<procedure_occurrence>");
		out("			<procedure_occurrence_source_identifier>" + idSeed++ + "</procedure_occurrence_source_identifier>");
		out("			<x_data_source_type>CDW</x_data_source_type>");
		out("			<person_source_value>" + idSeed++ + "</person_source_value>"); //
		out("			<procedure_source_value>87.17</procedure_source_value>");
		out("			<procedure_type_source_vocabulary>ICD-9</procedure_type_source_vocabulary>");
		out("			<procedure_date>2012-01-02</procedure_date>");
		out("			<procedure_type_source_value>Primary</procedure_type_source_value>");
		out("			<provider_record_source_value>" + idSeed++ + "</provider_record_source_value>"); //
		out("			<visit_occurrence_source_value>" + idSeed++ + "</visit_occurrence_source_value>"); //
		out("			<relevant_condition_source_value>" + idSeed++ + "</relevant_condition_source_value>"); //
		
		int numberOfCosts = rnd.nextInt(5);
		for (int i = 0; i < numberOfCosts; i++) {
			generateProcedureCost();
		}
		
		out("		</procedure_occurrence>");
	}
	
	public static void generateProcedureCost() throws Exception {
		out("			<procedure_cost>");
		out("				<procedure_cost_source_identifier>" + idSeed++ + "</procedure_cost_source_identifier>"); //
		out("				<procedure_occurrence_source_identifier>" + idSeed++ + "</procedure_occurrence_source_identifier>"); //
		out("				<paid_copay>50.00</paid_copay>");
		out("				<paid_coinsurance>0.00</paid_coinsurance>");
		out("				<paid_toward_deductible>0.00</paid_toward_deductible>");
		out("				<paid_by_payer>200.00</paid_by_payer>");
		out("				<paid_by_coordination_benefits>0.00</paid_by_coordination_benefits>");
		out("				<total_out_of_pocket>0.00</total_out_of_pocket>");
		out("				<total_paid>250.00</total_paid>");
		out("				<disease_class_concept_id>0</disease_class_concept_id>");
		out("				<disease_class_source_value>" + idSeed++ + "</disease_class_source_value>"); //
		out("				<revenue_code_concept_id>0</revenue_code_concept_id>");
		out("				<revenue_code_source_value>A</revenue_code_source_value>");
		out("				<payer_plan_period_source_identifier>" + idSeed++ + "</payer_plan_period_source_identifier>"); //
		out("			</procedure_cost>");
	}
	
	public static void generateCohort() throws Exception {
		out("		<cohort>");
		out("			<cohort_source_identifier>" + idSeed++ + "</cohort_source_identifier>"); //
		out("			<cohort_source_value>Hospitalization</cohort_source_value>");
		out("			<cohort_start_date>2012-01-02</cohort_start_date>");
		out("			<cohort_end_date>2012-01-02</cohort_end_date>");
		out("			<subject_source_identifier>" + idSeed++ + "</subject_source_identifier>"); //
		out("			<stop_reason>Deceased</stop_reason>");
		out("		</cohort>");
	}
	
	public static void generateDeath() throws Exception {
		out("		<death>");
		out("			<person_source_value>" + idSeed++ + "</person_source_value>"); //
		out("			<death_date>2012-01-03</death_date>");
		out("			<death_type_concept_id>0</death_type_concept_id>");
		out("			<death_type_source_value>Deceased</death_type_source_value>");
		out("			<cause_of_death_source_value>E907</cause_of_death_source_value>");
		out("		</death>");
	}
	
	public static void generatePayerPlanPeriod() throws Exception {
		out("		<payer_plan_period>");
		out("			<payer_plan_period_source_identifier>" + idSeed++ + "</payer_plan_period_source_identifier>"); //
		out("			<person_source_value>" + idSeed++ + "</person_source_value>"); //
		out("			<payer_plan_period_start_date>2012-01-01</payer_plan_period_start_date>");
		out("			<payer_plan_period_end_date>2012-12-31</payer_plan_period_end_date>");
		out("			<payer_source_value>BCBS</payer_source_value>");
		out("			<plan_source_value>HMO</plan_source_value>");
		out("			<family_source_value>A</family_source_value>");
		out("		</payer_plan_period>");
	}
	
	public static void out(String out) throws Exception {
		fw.write(out + "\n");
	}
	
	public static String getGenderLetter() throws Exception {
		if (rnd.nextInt(2) == 1) {
			return "M";
		}
		return "F";
	}
	
	public static String getCityName() throws Exception {
		return cities1[rnd.nextInt(cities1.length)] + cities2[rnd.nextInt(cities2.length)] + cities3[rnd.nextInt(cities3.length)];
	}

}
