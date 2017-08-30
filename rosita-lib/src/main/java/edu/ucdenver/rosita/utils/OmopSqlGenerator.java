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

import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class OmopSqlGenerator {
	
	private static Map<String, String> sqlMap = new HashMap<String, String>();
	private static Map<String, PreparedStatement> psMap = new HashMap<String, PreparedStatement>();
	
	static FileWriter fw = null;
	static Random rnd = new Random();
	static Long idSeed = 0L;
	static NameGenerator gen = new NameGenerator();
	static String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	static DataSource ds = null;
	
	static String[] cities1 = {"Borp", "Spark", "Chip", "Mud", "Mad", "Aber", "Bas", "Carl", "Dalt", "Even", "Findl", "Gork", "Hat", "Igloo", "James", "Keen", "Lough", "Mert", "Narm", "Os", "Park", "Ross", "Rub", "Rex", "Tend", "Tart"};
	static String[] cities2 = {"ing", "er", "for", "ough", "ney", "ert", "ford", "bert"};
	static String[] cities3 = {"ville", "ton", " City", "borough", "burgh", "deen", "don", "gow", "gee", "gough", "bridge", "river"};

	static int LOCATIONS = 50;
	static int ORGANIZATIONS = 10;
	static int CARE_SITES = 2;
	static int PROVIDERS = 2;
	static int PERSONS = 5;
	static int VISIT_OCCURRENCES = 5;
	static int CONDITION_OCCURRENCES = 50;
	//static int COHORTS = 5;
	static int DRUG_EXPOSURES = 50;
	//static int DRUG_COSTS = 1;
	static int OBSERVATIONS = 5;
	static int PROCEDURE_OCCURRENCES = 5;
	static int PAYER_PLAN_PERIODS = 5;
	static int PROCEDURE_COSTS = 5;
	//static int DEATHS = 1;
	
	static OmopChildValues currentLocation = null;
	static OmopChildValues currentOrganization = null;
	static OmopChildValues currentCareSite = null;
	static OmopChildValues currentProvider = null;
	static OmopChildValues currentPerson = null;
	static OmopChildValues currentPayerPlanPeriod = null;
	static OmopChildValues currentVisitOccurrence = null;
	static OmopChildValues currentConditionOccurrence = null;
	static OmopChildValues currentDrugExposure = null;
	static OmopChildValues currentProcedureOccurrence = null;
	
	public static void main(String[] args) throws Exception {
		
		populateTemplates();
		setupConnection();
		
		PreparedStatement locps = psMap.get("OmopLocation");
		PreparedStatement orgps = psMap.get("OmopOrganization");
		PreparedStatement carps = psMap.get("OmopCareSite");
		PreparedStatement props = psMap.get("OmopProvider");
		PreparedStatement perps = psMap.get("OmopPerson");
		PreparedStatement deaps = psMap.get("OmopDeath");
		PreparedStatement cohps = psMap.get("OmopCohort");
		PreparedStatement visps = psMap.get("OmopVisitOccurrence");
		PreparedStatement obsps = psMap.get("OmopObservation");
		PreparedStatement conps = psMap.get("OmopConditionOccurrence");
		PreparedStatement drups = psMap.get("OmopDrugExposure");
		PreparedStatement drcps = psMap.get("OmopDrugCost");
		PreparedStatement prcps = psMap.get("OmopProcedureOccurrence");
		PreparedStatement pppps = psMap.get("OmopPayerPlanPeriod");
		PreparedStatement pccps = psMap.get("OmopProcedureCost");
		PreparedStatement pxcmd = psMap.get("OmopXCohortMetadata");
		
		/**
		 * Please accept my apologies - this is awful.
		 */
		for (int a = 0; a < LOCATIONS; a++) {
			
			currentLocation = prepareLocation(locps);
			locps.execute();
			System.out.println("Generated a location");
			
			for (int b = 0; b < ORGANIZATIONS; b++) {
				
				currentOrganization = prepareOrganization(orgps, currentLocation);
				orgps.execute();
				System.out.println("Generated an organization");
				
				for (int c = 0; c < CARE_SITES; c++) {
					
					currentCareSite = prepareCareSite(carps, currentLocation, currentOrganization);
					carps.execute();
					
					for (int d = 0; d < PROVIDERS; d++) {
						
						currentProvider = prepareProvider(props, currentOrganization, currentCareSite);
						props.execute();
						
						for (int e = 0; e < PERSONS; e++) {
							
							currentPerson = preparePerson(perps, currentLocation, currentOrganization, currentCareSite, currentProvider);
							perps.execute();
							prepareDeath(deaps, currentPerson);
							deaps.execute();
							prepareCohort(cohps, currentPerson);
							cohps.execute();
							currentPayerPlanPeriod = preparePayerPlanPeriod(pppps, currentPerson);
							pppps.execute();
							
							for (int f = 0; f < VISIT_OCCURRENCES; f++) {
								
								currentVisitOccurrence = prepareVisitOccurrence(visps, currentCareSite, currentProvider, currentPerson);
								visps.execute();
								
								for (int g = 0; g < OBSERVATIONS; g++) {
									
									prepareObservation(obsps, currentProvider, currentPerson, currentVisitOccurrence);
									obsps.execute();
									
								}
								
								for (int h = 0; h < CONDITION_OCCURRENCES; h++) {
									
									currentConditionOccurrence = prepareConditionOccurrence(conps, currentProvider, currentPerson, currentVisitOccurrence);
									conps.execute();
									
								}
								
								for (int i = 0; i < PROCEDURE_OCCURRENCES; i++) {
									
									currentProcedureOccurrence = prepareProcedureOccurrence(prcps, currentProvider, currentPerson, currentVisitOccurrence);
									prcps.execute();
									prepareProcedureCost(pccps, currentProcedureOccurrence, currentPayerPlanPeriod);
									pccps.execute();
									
								}
								
								for (int j = 0; j < DRUG_EXPOSURES; j++) {
									
									currentDrugExposure = prepareDrugExposure(drups, currentProvider, currentPerson, currentVisitOccurrence);
									drups.execute();
									prepareDrugCost(drcps, currentDrugExposure, currentPayerPlanPeriod);
									drcps.execute();
								}
								
							}
						}
					}
				}
			}
			
		}

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
	
	public static String getCountyName() throws Exception {
		return cities1[rnd.nextInt(cities1.length)] + cities2[rnd.nextInt(cities2.length)] + " County";
	}
	
	public static String getAddress1() {
		return rnd.nextInt(999) + " " + gen.getName(3) + " St";
	}
	
	public static String getAddress2() {
		return "Suite " + rnd.nextInt(999);
	}
	
	public static String getState() {
		return String.valueOf(letters.charAt(rnd.nextInt(letters.length())) + String.valueOf(letters.charAt(rnd.nextInt(letters.length()))));
	}
	
	public static String getZipcode() {
		String base = "00000" + rnd.nextInt(10000);
		return base.substring(base.length()-5);
	}
	
	public static Long getGridNodeId() {
		return 94L;
	}
	
	static String[] deathintros = {"Squashed flat by ", "Bitten by ", "Eaten by ", "Spiked by a marauding ", "Freak accident with ", "Swallowed ", "Lost fight with ", "Hit on head by ", "Turned into "};
	static String[] deathobjects = {"viper", "bus conductor", "fridge", "anvil", "lion", "wheelbarrow", "unicycle", "kettle", "toaster", "bicycle", "ironing board", "washing machine", "television", "partridge", "pudding", "grue"};
	public static String getCauseOfDeath() {
		return deathintros[rnd.nextInt(deathintros.length)] + deathobjects[rnd.nextInt(deathobjects.length)];
	}
	
	static String[] races = {"Orc", "Hobbit", "Werewolf", "Dwarf", "Silicoid", "Martian", "Rabbit", "Tribble", "Coyote"};
	static String[] ethnicities = {"Pink", "Orange", "Blue", "Purple", "Spotted", "Striped", "Green"};
	public static String getRace() {
		return races[rnd.nextInt(races.length)];
	}
	public static String getEthnicity() {
		return ethnicities[rnd.nextInt(ethnicities.length)];
	}
	
	public static void populateTemplates() {
		sqlMap.put("OmopCareSite", "INSERT INTO omop.care_site (care_site_id,care_site_source_value,x_data_source_type,location_id,organization_id,place_of_service_concept_id,place_of_service_source_value,x_care_site_name,x_grid_node_id) VALUES (?,?,?,?,?,?,?,?,?);");
		sqlMap.put("OmopCohort", "INSERT INTO omop.cohort (cohort_id,cohort_concept_id,cohort_start_date,cohort_end_date,subject_id,stop_reason,x_grid_node_id) VALUES (?,?,?,?,?,?,?);");
		sqlMap.put("OmopConditionOccurrence", "INSERT INTO omop.condition_occurrence(condition_occurrence_id,x_data_source_type,person_id,condition_concept_id,condition_source_value,x_condition_source_desc,condition_start_date,x_condition_update_date,condition_end_date,condition_type_concept_id,stop_reason,associated_provider_id,visit_occurrence_id,x_grid_node_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
		sqlMap.put("OmopDeath", "INSERT INTO omop.death(person_id,death_date,death_type_concept_id,cause_of_death_concept_id,cause_of_death_source_value,x_grid_node_id) VALUES (?,?,?,?,?,?);");
		sqlMap.put("OmopDrugCost", "INSERT INTO omop.drug_cost(drug_cost_id,drug_exposure_id,paid_copay,paid_coinsurance,paid_toward_deductible,paid_by_payer,paid_by_coordination_benefits,total_out_of_pocket,total_paid,ingredient_cost,dispensing_fee,average_wholesale_price,payer_plan_period_id,x_grid_node_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		sqlMap.put("OmopDrugExposure", "INSERT INTO omop.drug_exposure(drug_exposure_id,x_data_source_type,person_id,drug_concept_id,drug_source_value,drug_exposure_start_date,drug_exposure_end_date,drug_type_concept_id,stop_reason,refills,quantity,days_supply,x_drug_name,x_drug_strength,sig,prescribing_provider_id,visit_occurrence_id,relevant_condition_concept_id,x_grid_node_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
		sqlMap.put("OmopLocation", "INSERT INTO omop.location(location_id,location_source_value,x_data_source_type,address_1,address_2,city,state,zip,county,x_zip_deidentified,x_location_type,x_grid_node_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);");
		sqlMap.put("OmopObservation", "INSERT INTO omop.observation(observation_id,x_data_source_type,person_id,observation_concept_id,observation_source_value,observation_date,observation_time,value_as_number,value_as_string,value_as_concept_id,unit_concept_id,unit_source_value,range_low,range_high,observation_type_concept_id,associated_provider_id,visit_occurrence_id,relevant_condition_concept_id,x_obs_comment,x_grid_node_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
		sqlMap.put("OmopOrganization", "INSERT INTO omop.organization(organization_id,organization_source_value,x_data_source_type,place_of_service_concept_id,place_of_service_source_value,location_id,x_grid_node_id) VALUES (?,?,?,?,?,?,?);");
		sqlMap.put("OmopPayerPlanPeriod", "INSERT INTO omop.payer_plan_period(payer_plan_period_id,person_id,payer_plan_period_start_date,payer_plan_period_end_date,payer_source_value,plan_source_value,family_source_value,x_grid_node_id) VALUES (?,?,?,?,?,?,?,?);");
		sqlMap.put("OmopPerson", "INSERT INTO omop.person(person_id,person_source_value,location_id,year_of_birth,month_of_birth,day_of_birth,gender_concept_id,gender_source_value,race_concept_id,race_source_value,ethnicity_concept_id,ethnicity_source_value,provider_id,care_site_id,x_organization_id,x_grid_node_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
		sqlMap.put("OmopProcedureCost", "INSERT INTO omop.procedure_cost(procedure_cost_id,procedure_occurrence_id,paid_copay,paid_coinsurance,paid_toward_deductible,paid_by_payer,paid_by_coordination_benefits,total_out_of_pocket,total_paid,disease_class_concept_id,disease_class_source_value,revenue_code_concept_id,revenue_code_source_value,payer_plan_period_id,x_grid_node_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
		sqlMap.put("OmopProcedureOccurrence", "INSERT INTO omop.procedure_occurrence(procedure_occurrence_id,x_data_source_type,person_id,procedure_concept_id,procedure_source_value,procedure_date,procedure_type_concept_id,associated_provider_id,visit_occurrence_id,relevant_condition_concept_id,x_grid_node_id) VALUES (?,?,?,?,?,?,?,?,?,?,?);");
		sqlMap.put("OmopProvider", "INSERT INTO omop.provider(provider_id,provider_source_value,x_data_source_type,npi,dea,specialty_concept_id,specialty_source_value,x_provider_first,x_provider_middle,x_provider_last,care_site_id,x_organization_id,x_grid_node_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);");
		sqlMap.put("OmopVisitOccurrence", "INSERT INTO omop.visit_occurrence(visit_occurrence_id,x_visit_occurrence_source_identifier,x_data_source_type,person_id,visit_start_date,visit_end_date,place_of_service_concept_id,place_of_service_source_value,x_provider_id,care_site_id,x_grid_node_id) VALUES (?,?,?,?,?,?,?,?,?,?,?);");
		sqlMap.put("OmopXCohortMetadata", "INSERT INTO omop.x_cohort_metadata(cohort_name,cohort_description,cohort_creator_name,cohort_creator_contact,is_cohort_shared,cohort_query,service_url,original_cohort_count,last_cohort_count,original_cohort_date,last_cohort_date,cohort_phi_notes,cohort_other_notes,cohort_expiration_date,grid_node_id)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
	}
	
	public static void setupConnection() throws SQLException {
		String[] classNames = {"OmopCareSite", "OmopCohort", "OmopConditionOccurrence", "OmopDeath", "OmopDrugCost", "OmopDrugExposure", "OmopLocation", "OmopObservation", "OmopOrganization", "OmopPayerPlanPeriod", "OmopPerson", "OmopProcedureCost", "OmopProcedureOccurrence", "OmopProvider", "OmopVisitOccurrence","OmopXCohortMetadata"};
		ds = new DriverManagerDataSource("jdbc:postgresql://localhost:5432/rosita", "groovy", "gr00vyewJ");
		for (String className : classNames) {
			psMap.put(className, ds.getConnection().prepareStatement(sqlMap.get(className)));
		}
	}
	
	public static OmopChildValues prepareLocation(PreparedStatement ps) throws Exception {
		
		String zip = getZipcode();
		Long id = idSeed++;
		String sourceValue = "LOCSV" + String.valueOf(idSeed++);
		
		ps.setLong(1, id);
		ps.setString(2, sourceValue);
		ps.setString(3, "TEST");
		ps.setString(4, getAddress1());
		ps.setString(5, getAddress2());
		ps.setString(6, getCityName());
		ps.setString(7, getState());
		ps.setString(8, zip);
		ps.setString(9, getCountyName());
		ps.setString(10, zip.substring(0, 3));
		ps.setString(11, "A location");
		ps.setLong(12, getGridNodeId());
		
		return new OmopChildValues(id, sourceValue);
	}
	
	public static OmopChildValues prepareOrganization(PreparedStatement ps, OmopChildValues loc) throws Exception {
		
		Long id = idSeed++;
		String sourceValue = "ORGSV" + String.valueOf(idSeed++);
		
		ps.setLong(1, id);
		ps.setString(2, sourceValue);
		ps.setString(3, "TEST");
		ps.setLong(4, rnd.nextInt(10000));
		ps.setString(5, "POSSV" + rnd.nextInt(10000));
		ps.setLong(6, loc.id);		
		ps.setLong(7, getGridNodeId());
		
		return new OmopChildValues(id, sourceValue);
	}
	
	public static OmopChildValues prepareCareSite(PreparedStatement ps, OmopChildValues loc, OmopChildValues org) throws Exception {
		
		Long id = idSeed++;
		String sourceValue = "CARSV" + String.valueOf(idSeed++);
		
		ps.setLong(1, id);
		ps.setString(2, sourceValue);
		ps.setString(3, "TEST");
		ps.setLong(4, loc.id);
		ps.setLong(5, org.id);
		ps.setLong(6, rnd.nextInt(10000));
		ps.setString(7, "POSSV" + rnd.nextInt(10000));
		ps.setString(8, getCityName() + " Hospital");
		ps.setLong(9, getGridNodeId());

		
		return new OmopChildValues(id, sourceValue);
	}
	
	public static OmopChildValues prepareProvider(PreparedStatement ps, OmopChildValues org, OmopChildValues car) throws Exception {
		
		Long id = idSeed++;
		String sourceValue = "PROSV" + String.valueOf(idSeed++);
		
		ps.setLong(1, id);
		ps.setString(2, sourceValue);
		ps.setString(3, "TEST");
		ps.setString(4, "NPI" + rnd.nextInt(10000));
		ps.setString(5, "DEA" + rnd.nextInt(10000));
		ps.setLong(6, rnd.nextInt(10000));
		ps.setString(7, "SPECSV" + rnd.nextInt(10000));
		ps.setString(8, gen.getName(3));
		ps.setString(9, gen.getName(1));
		ps.setString(10, gen.getName(5));
		ps.setLong(11, car.id);
		ps.setLong(12, org.id);
		ps.setLong(13, getGridNodeId());
		
		return new OmopChildValues(id, sourceValue);
	}
	
	public static OmopChildValues preparePerson(PreparedStatement ps, OmopChildValues loc, OmopChildValues org, OmopChildValues car, OmopChildValues pro) throws Exception {
		
		Long id = idSeed++;
		String sourceValue = "PERSV" + String.valueOf(idSeed++);
		String gender = getGenderLetter();
		
		ps.setLong(1, id);
		ps.setString(2, sourceValue);
		ps.setLong(3, loc.id);
		ps.setLong(4, rnd.nextInt(100) + 1900);
		ps.setLong(5, rnd.nextInt(12) + 1);
		ps.setLong(6, rnd.nextInt(28) + 1);
		ps.setLong(7, gender.charAt(0));
		ps.setString(8, gender);
		ps.setLong(9, rnd.nextInt(10));
		ps.setString(10, getRace());
		ps.setLong(11, rnd.nextInt(10));
		ps.setString(12, getEthnicity());
		ps.setLong(13, pro.id);
		ps.setLong(14, car.id);
		ps.setLong(15, org.id);
		ps.setLong(16, getGridNodeId());
		
		return new OmopChildValues(id, sourceValue);
	}
	
	public static OmopChildValues prepareDeath(PreparedStatement ps, OmopChildValues per) throws Exception {
		
		Long id = idSeed++;
		String sourceValue = "DEASV" + String.valueOf(idSeed++);
		
		ps.setLong(1, per.id);
		ps.setDate(2, new java.sql.Date(new Date().getTime() - rnd.nextInt(1000000)));
		ps.setLong(3, rnd.nextInt(10));
		ps.setLong(4, rnd.nextInt(10));
		ps.setString(5, getCauseOfDeath());
		ps.setLong(6, getGridNodeId());
		
		return new OmopChildValues(id, sourceValue);
	}
	
	public static OmopChildValues prepareCohort(PreparedStatement ps, OmopChildValues per) throws Exception {
		
		Long id = idSeed++;
		String sourceValue = "COHSV" + String.valueOf(idSeed++);
		
		ps.setLong(1, id);
		ps.setLong(2, rnd.nextInt(50));
		ps.setDate(3, new java.sql.Date(new Date().getTime() - 1000000 - rnd.nextInt(1000000)));
		ps.setDate(4, new java.sql.Date(new Date().getTime() - rnd.nextInt(1000000)));
		ps.setLong(5, per.id);
		ps.setString(6, "Just stopped");
		ps.setLong(7, getGridNodeId());
		
		return new OmopChildValues(id, sourceValue);
	}
	
	public static OmopChildValues prepareVisitOccurrence(PreparedStatement ps, OmopChildValues car, OmopChildValues pro, OmopChildValues per) throws Exception {
		
		Long id = idSeed++;
		String sourceValue = "VISSV" + String.valueOf(idSeed++);
		
		ps.setLong(1, id);
		ps.setString(2, sourceValue);
		ps.setString(3, "TEST");
		ps.setLong(4, per.id);
		ps.setDate(5, new java.sql.Date(new Date().getTime() - 1000000 - rnd.nextInt(1000000)));
		ps.setDate(6, new java.sql.Date(new Date().getTime() - rnd.nextInt(1000000)));
		ps.setLong(7, rnd.nextInt(10));
		ps.setString(8, "PSSV" + idSeed++);
		ps.setLong(9, pro.id);
		ps.setLong(10, car.id);
		ps.setLong(11, getGridNodeId());
		
		return new OmopChildValues(id, sourceValue);
	}
	
	public static OmopChildValues prepareObservation(PreparedStatement ps, OmopChildValues pro, OmopChildValues per, OmopChildValues vis) throws Exception {
		
		Long id = idSeed++;
		String sourceValue = "COHSV" + String.valueOf(idSeed++);
		
		ps.setLong(1, id);
		ps.setString(2, sourceValue);
		ps.setLong(3, per.id);
		ps.setLong(4, idSeed++);
		ps.setString(5, "Observation");
		ps.setDate(6, new java.sql.Date(new Date().getTime() - 1000000 - rnd.nextInt(1000000)));
		ps.setTime(7, new java.sql.Time(new Date().getTime() - rnd.nextInt(1000000)));
		ps.setDouble(8, rnd.nextDouble());
		ps.setString(9, "A string: " + rnd.nextDouble());
		ps.setLong(10, rnd.nextInt(10000));
		ps.setLong(11, rnd.nextInt(10000));
		ps.setString(12, "UNSV" + rnd.nextInt(10000));
		ps.setDouble(13, rnd.nextInt(10));
		ps.setDouble(14, rnd.nextInt(10000) + 20);
		ps.setLong(15, rnd.nextInt(10000));
		ps.setLong(16, pro.id);
		ps.setLong(17, vis.id);
		ps.setLong(18, rnd.nextInt(10000));
		ps.setString(19, gen.getName(3) + " " + gen.getName(5) + " " + gen.getName(4) + " " + gen.getName(6) + " " + gen.getName(2));
		ps.setLong(20, getGridNodeId());
		
		return new OmopChildValues(id, sourceValue);
	}
	
	public static OmopChildValues prepareConditionOccurrence(PreparedStatement ps, OmopChildValues pro, OmopChildValues per, OmopChildValues vis) throws Exception {
		
		Long id = idSeed++;
		String sourceValue = "OBSSV" + String.valueOf(idSeed++);
		
		ps.setLong(1, id);
		ps.setString(2, sourceValue);
		ps.setLong(3, per.id);
		ps.setLong(4, rnd.nextInt(10000));
		ps.setString(5, "CONSV" + rnd.nextInt(10000));
		ps.setString(6, "Condition description " + gen.getName(3) + " " + gen.getName(5) + " " + gen.getName(4));
		ps.setDate(7, new java.sql.Date(new Date().getTime() - 1000000 - rnd.nextInt(1000000)));
		ps.setDate(8, new java.sql.Date(new Date().getTime() - rnd.nextInt(500000)));
		ps.setDate(9, new java.sql.Date(new Date().getTime() - rnd.nextInt(1000000)));
		ps.setLong(10, rnd.nextInt(10000));
		ps.setString(11, "Just stopped");
		ps.setLong(12, pro.id);
		ps.setLong(13, vis.id);
		ps.setLong(14, getGridNodeId());
		
		return new OmopChildValues(id, sourceValue);
	}
	
	public static OmopChildValues prepareDrugExposure(PreparedStatement ps, OmopChildValues pro, OmopChildValues per, OmopChildValues vis) throws Exception {
		
		Long id = idSeed++;
		String sourceValue = "DEXSV" + String.valueOf(idSeed++);
		
		ps.setLong(1, id);
		ps.setString(2, "TEST");
		ps.setLong(3, per.id);
		ps.setLong(4, rnd.nextInt(1000));
		ps.setString(5, sourceValue);
		ps.setDate(6, new java.sql.Date(new Date().getTime() - 1000000 - rnd.nextInt(1000000)));
		ps.setDate(7, new java.sql.Date(new Date().getTime() - rnd.nextInt(500000)));
		ps.setLong(8, rnd.nextInt(1000));
		ps.setString(9, "Completed");
		ps.setLong(10, rnd.nextInt(10));
		ps.setDouble(11, rnd.nextDouble()*10);
		ps.setLong(12, (rnd.nextInt(3)+1)*30);
		ps.setString(13, gen.getName(10));
		ps.setString(14, "Medium");
		ps.setString(15, "Sig sig sig sig " + gen.getName(10));
		ps.setLong(16, pro.id);
		ps.setLong(17, vis.id);
		ps.setLong(18, rnd.nextInt(10000));
		ps.setLong(19, getGridNodeId());
		
		return new OmopChildValues(id, sourceValue);
	}
	
	public static OmopChildValues prepareDrugCost(PreparedStatement ps, OmopChildValues exp, OmopChildValues ppp) throws Exception {
		
		Long id = idSeed++;
		String sourceValue = "DCOSV" + String.valueOf(idSeed++);
		
		ps.setLong(1, id);
		ps.setLong(2, exp.id);
		ps.setDouble(3, rnd.nextDouble()*100);
		ps.setDouble(4, rnd.nextDouble()*100);
		ps.setDouble(5, rnd.nextDouble()*100);
		ps.setDouble(6, rnd.nextDouble()*100);
		ps.setDouble(7, rnd.nextDouble()*100);
		ps.setDouble(8, rnd.nextDouble()*100);
		ps.setDouble(9, rnd.nextDouble()*100);
		ps.setDouble(10, rnd.nextDouble()*100);
		ps.setDouble(11, rnd.nextDouble()*100);
		ps.setDouble(12, rnd.nextDouble()*100);
		ps.setLong(13, ppp.id);
		ps.setLong(14, getGridNodeId());
		
		return new OmopChildValues(id, sourceValue);
	}
	
	public static OmopChildValues preparePayerPlanPeriod(PreparedStatement ps, OmopChildValues per) throws Exception {
		
		Long id = idSeed++;
		String sourceValue = "PPPSV" + String.valueOf(idSeed++);
		
		ps.setLong(1, id);
		ps.setLong(2, per.id);
		ps.setDate(3, new java.sql.Date(new Date().getTime() - 1000000 - rnd.nextInt(1000000)));
		ps.setDate(4, new java.sql.Date(new Date().getTime() - rnd.nextInt(500000)));
		ps.setString(5, gen.getName(10));
		ps.setString(6, gen.getName(10));
		ps.setString(7, gen.getName(10));
		ps.setLong(8, getGridNodeId());
		
		return new OmopChildValues(id, sourceValue);
	}

	public static OmopChildValues prepareProcedureOccurrence(PreparedStatement ps, OmopChildValues pro, OmopChildValues per, OmopChildValues vis) throws Exception {
		
		Long id = idSeed++;
		String sourceValue = "PRCSV" + String.valueOf(idSeed++);
		
		ps.setLong(1, id);
		ps.setString(2, "TEST");
		ps.setLong(3, per.id);
		ps.setLong(4, rnd.nextInt(10000));
		ps.setString(5, gen.getName(10) + gen.getName(10));
		ps.setDate(6, new java.sql.Date(new Date().getTime() - 1000000 - rnd.nextInt(1000000)));
		ps.setLong(7, rnd.nextInt(10000));
		ps.setLong(8, pro.id);
		ps.setLong(9, vis.id);
		ps.setLong(10, rnd.nextInt(10000));
		ps.setLong(11, getGridNodeId());
		
		return new OmopChildValues(id, sourceValue);
	}
	
	public static OmopChildValues prepareProcedureCost(PreparedStatement ps, OmopChildValues prc, OmopChildValues ppp) throws Exception {
		
		Long id = idSeed++;
		String sourceValue = "PCCSV" + String.valueOf(idSeed++);
		
		ps.setLong(1, id);
		ps.setLong(2, prc.id);
		ps.setDouble(3, rnd.nextDouble()*100);
		ps.setDouble(4, rnd.nextDouble()*100);
		ps.setDouble(5, rnd.nextDouble()*100);
		ps.setDouble(6, rnd.nextDouble()*100);
		ps.setDouble(7, rnd.nextDouble()*100);
		ps.setDouble(8, rnd.nextDouble()*100);
		ps.setDouble(9, rnd.nextDouble()*100);
		ps.setLong(10, rnd.nextInt(10000));
		ps.setString(11, gen.getName(10));
		ps.setLong(12, rnd.nextInt(10000));
		ps.setString(13, gen.getName(10));
		ps.setLong(14, ppp.id);
		ps.setLong(15, getGridNodeId());
		
		return new OmopChildValues(id, sourceValue);
	}

}
