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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlTemplates {
	
	private Map<String, String> sqlMap = new HashMap<String, String>();
	public List<String> truncateList = new ArrayList<String>();
	public List<String> gridTruncateList = new ArrayList<String>();
	public List<String> profileList = new ArrayList<String>();
	public List<String> omopProfileList = new ArrayList<String>();
	public List<String> deleteFromSourceList = new ArrayList<String>();
	
	public SqlTemplates() {
		sqlMap.put("Organization", "INSERT INTO raw.organization(organization_address_1, organization_address_2, organization_city, organization_county, organization_source_value, organization_state, organization_zip, place_of_service_source_value, x_data_source_type, x_data_source_id,x_etl_date,x_record_num) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
		sqlMap.put("CareSite", "INSERT INTO raw.care_site(care_site_address_1, care_site_address_2, care_site_city, care_site_county, care_site_source_value, care_site_state, care_site_zip, organization_source_value, place_of_service_source_value, x_care_site_name, x_data_source_type, x_data_source_id,x_etl_date,x_record_num) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		sqlMap.put("Provider", "INSERT INTO raw.provider(care_site_source_value, dea, npi, provider_source_value, specialty_source_value, x_data_source_type, x_organization_source_value, x_provider_first, x_provider_last, x_provider_middle, x_data_source_id,x_etl_date,x_record_num) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
		sqlMap.put("Demographic", "INSERT INTO raw.x_demographic(address_1, address_2, care_site_source_value, city, county, day_of_birth, ethnicity_source_value, first, gender_source_value, last, medicaid_id_number, middle, month_of_birth, person_source_value, provider_source_value, race_source_value, ssn, state, x_data_source_type, x_organization_source_value, year_of_birth, zip, x_data_source_id,x_etl_date,x_record_num) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		sqlMap.put("VisitOccurrence", "INSERT INTO raw.visit_occurrence(care_site_source_value, person_source_value, place_of_service_source_value, visit_end_date, x_visit_occurrence_source_identifier, visit_start_date, x_data_source_type, x_provider_source_value, x_data_source_id,x_etl_date,x_record_num) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
		sqlMap.put("Observation", "INSERT INTO raw.observation(associated_provider_source_value, observation_date, observation_source_identifier, observation_source_value, observation_source_value_vocabulary, observation_time, observation_type_source_value, person_source_value, range_high, range_low, relevant_condition_source_value, unit_source_value, value_as_number, value_as_string, x_visit_occurrence_source_identifier, x_data_source_type, x_obs_comment, x_data_source_id,x_etl_date,x_record_num) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		sqlMap.put("DrugExposure", "INSERT INTO raw.drug_exposure(days_supply, drug_exposure_end_date, drug_exposure_source_identifier, drug_exposure_start_date, drug_source_value, drug_source_value_vocabulary, drug_type_source_value, person_source_value, provider_source_value, quantity, refills, relevant_condition_source_value, sig, stop_reason, x_visit_occurrence_source_identifier, x_data_source_type, x_drug_name, x_drug_strength, x_data_source_id,x_etl_date,x_record_num) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		sqlMap.put("DrugCost", "INSERT INTO raw.drug_cost(average_wholesale_price, dispensing_fee, drug_cost_source_identifier, drug_exposure_id, drug_exposure_source_identifier, ingredient_cost, paid_by_coordination_of_benefits, paid_by_payer, paid_coinsurance, paid_copay, paid_toward_deductible, payer_plan_period_source_identifier, total_out_of_pocket, total_paid, x_data_source_id,x_etl_date,x_record_num) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		sqlMap.put("ConditionOccurrence", "INSERT INTO raw.condition_occurrence(associated_provider_source_value, condition_end_date, condition_occurrence_source_identifier, condition_source_value, condition_source_value_vocabulary, condition_start_date, condition_type_source_value, person_source_value, stop_reason, x_visit_occurrence_source_identifier, x_condition_source_desc, x_condition_update_date, x_data_source_type, x_data_source_id,x_etl_date,x_record_num) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		sqlMap.put("ProcedureOccurrence", "INSERT INTO raw.procedure_occurrence(person_source_value, procedure_date, procedure_occurrence_source_identifier, procedure_source_value, procedure_type_source_value, procedure_source_value_vocabulary, provider_record_source_value, relevant_condition_source_value, x_visit_occurrence_source_identifier, x_data_source_type, x_data_source_id,x_etl_date,x_record_num) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
		sqlMap.put("ProcedureCost", "INSERT INTO raw.procedure_cost(disease_class_concept_id, disease_class_source_value, paid_by_coordination_of_benefits, paid_by_payer, paid_coinsurance, paid_copay, paid_toward_deductible, payer_plan_period_source_identifier, procedure_cost_source_identifier, procedure_occurrence_id, procedure_occurrence_source_identifier, revenue_code_concept_id, revenue_code_source_value, total_out_of_pocket, total_paid, x_data_source_id,x_etl_date,x_record_num) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		sqlMap.put("Cohort", "INSERT INTO raw.cohort(cohort_end_date, cohort_source_identifier, cohort_source_value, cohort_start_date, x_demographic_id, stop_reason, subject_source_identifier, x_data_source_id,x_etl_date,x_record_num) VALUES (?,?,?,?,?,?,?,?,?,?)");
		sqlMap.put("Death", "INSERT INTO raw.death(cause_of_death_source_value, death_date, death_type_concept_id, death_type_source_value, x_demographic_id, person_source_value, x_data_source_id,x_etl_date,x_record_num) VALUES (?,?,?,?,?,?,?,?,?)");
		sqlMap.put("PayerPlanPeriod", "INSERT INTO raw.payer_plan_period(x_demographic_id, family_source_value, payer_plan_period_end_date, payer_plan_period_source_identifier, payer_plan_period_start_date, payer_source_value, person_source_value, plan_source_value, x_data_source_id,x_etl_date,x_record_num) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
		
		sqlMap.put("ValidationError", "INSERT INTO cz.log(job_id, step_id, message_type, message, error_code, log_date) VALUES (?,?,?,?,?,?)");
		//sqlMap.put("ValidationError", "INSERT INTO cz.log(error_type, line_number, message, datetime, schema, location, source_type, filename, step_id, x_data_source_id) VALUES (?,?,?,?,?,?,?,?,?,?)");
		sqlMap.put("VocabularyRow", "INSERT INTO rz.source_to_concept_map(source_code, source_vocabulary_id, source_code_description, target_concept_id, target_vocabulary_id, mapping_type, primary_map, valid_start_date, valid_end_date, invalid_reason, map_source, x_data_source_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
		
		//OMOP Imports in preparation for GRID - select from PostgreSQL
		sqlMap.put("ImportOmopCareSite", "SELECT care_site_id, location_id, organization_id, place_of_service_concept_id, care_site_source_value, place_of_service_source_value, x_data_source_type,  x_care_site_name, x_grid_node_id FROM omop.care_site WHERE care_site_id > ? ORDER BY care_site_id LIMIT ?");
		sqlMap.put("ImportOmopCohort", "SELECT cohort_id, cohort_concept_id, cohort_start_date, cohort_end_date, subject_id, stop_reason, x_grid_node_id FROM omop.cohort WHERE cohort_id > ? ORDER BY cohort_id LIMIT ?");
		sqlMap.put("ImportOmopConditionEra", "SELECT condition_era_id, person_id, condition_concept_id, condition_era_start_date, condition_era_end_date, condition_type_concept_id, condition_occurrence_count FROM omop.condition_era WHERE condition_era_id > ? ORDER BY condition_era_id LIMIT ?");
		sqlMap.put("ImportOmopConditionOccurrence", "SELECT condition_occurrence_id, person_id, condition_concept_id, condition_start_date, condition_end_date, condition_type_concept_id, stop_reason, associated_provider_id, visit_occurrence_id, condition_source_value, x_data_source_type, x_condition_source_desc, x_condition_update_date, x_grid_node_id FROM omop.condition_occurrence WHERE condition_occurrence_id > ? ORDER BY condition_occurrence_id LIMIT ?");
		sqlMap.put("ImportOmopDeath", "SELECT person_id, death_date, death_type_concept_id, cause_of_death_concept_id, cause_of_death_source_value, x_grid_node_id FROM omop.death WHERE person_id > ? ORDER BY person_id LIMIT ?");
		sqlMap.put("ImportOmopDrugCost", "SELECT drug_cost_id, drug_exposure_id, paid_copay, paid_coinsurance, paid_toward_deductible, paid_by_payer, paid_by_coordination_benefits, total_out_of_pocket, total_paid, ingredient_cost, dispensing_fee, average_wholesale_price, payer_plan_period_id, x_grid_node_id FROM omop.drug_cost WHERE drug_cost_id > ? ORDER BY drug_cost_id LIMIT ?");
		sqlMap.put("ImportOmopDrugEra", "SELECT drug_era_id, person_id, drug_concept_id, drug_era_start_date, drug_era_end_date, drug_type_concept_id, drug_exposure_count FROM omop.drug_era WHERE drug_era_id > ? ORDER BY drug_era_id LIMIT ?");
		sqlMap.put("ImportOmopDrugExposure", "SELECT drug_exposure_id, person_id, drug_concept_id, drug_exposure_start_date, drug_exposure_end_date, drug_type_concept_id, stop_reason, refills, quantity, days_supply, sig, prescribing_provider_id, visit_occurrence_id, relevant_condition_concept_id, x_data_source_type, x_drug_name, drug_source_value, x_drug_strength, x_grid_node_id FROM omop.drug_exposure WHERE drug_exposure_id > ? ORDER BY drug_exposure_id LIMIT ?");
		sqlMap.put("ImportOmopLocation", "SELECT location_id, address_1, address_2, city, state, zip, county, location_source_value, x_zip_deidentified, x_location_type, x_data_source_type, x_grid_node_id FROM omop.location WHERE location_id > ? ORDER BY location_id LIMIT ?");
		sqlMap.put("ImportOmopObservation", "SELECT observation_id, person_id, observation_concept_id, observation_date, observation_time, value_as_number, value_as_string, value_as_concept_id, unit_concept_id, range_low, range_high, observation_type_concept_id, associated_provider_id, visit_occurrence_id, relevant_condition_concept_id, observation_source_value, unit_source_value, x_data_source_type, x_obs_comment, x_grid_node_id FROM omop.observation WHERE observation_id > ? ORDER BY observation_id LIMIT ?");
		sqlMap.put("ImportOmopObservationPeriod", "SELECT observation_period_id, person_id, observation_period_start_date, observation_period_end_date FROM omop.observation_period WHERE observation_period_id > ? ORDER BY observation_period_id LIMIT ?");
		sqlMap.put("ImportOmopOrganization", "SELECT organization_id, place_of_service_concept_id, location_id, organization_source_value, place_of_service_source_value, x_data_source_type, x_grid_node_id FROM omop.organization WHERE organization_id > ? ORDER BY organization_id LIMIT ?");
		sqlMap.put("ImportOmopPayerPlanPeriod", "SELECT payer_plan_period_id, person_id, payer_plan_period_start_date, payer_plan_period_end_date, payer_source_value, plan_source_value, family_source_value, x_grid_node_id FROM omop.payer_plan_period WHERE payer_plan_period_id > ? ORDER BY payer_plan_period_id LIMIT ?");
		sqlMap.put("ImportOmopPerson", "SELECT person_id, gender_concept_id, year_of_birth, month_of_birth, day_of_birth, race_concept_id, ethnicity_concept_id, location_id, provider_id, care_site_id, person_source_value, gender_source_value, race_source_value, ethnicity_source_value, x_organization_id, x_grid_node_id FROM omop.person WHERE person_id > ? ORDER BY person_id LIMIT ?");
		sqlMap.put("ImportOmopProcedureCost", "SELECT procedure_cost_id, procedure_occurrence_id, paid_copay, paid_coinsurance, paid_toward_deductible, paid_by_payer, paid_by_coordination_benefits, total_out_of_pocket, total_paid, disease_class_concept_id, revenue_code_concept_id, payer_plan_period_id, disease_class_source_value, revenue_code_source_value, x_grid_node_id FROM omop.procedure_cost WHERE procedure_cost_id > ? ORDER BY procedure_cost_id LIMIT ?");
		sqlMap.put("ImportOmopProcedureOccurrence", "SELECT procedure_occurrence_id, person_id, procedure_concept_id, procedure_date, procedure_type_concept_id, associated_provider_id, visit_occurrence_id, relevant_condition_concept_id, procedure_source_value, x_data_source_type, x_grid_node_id FROM omop.procedure_occurrence WHERE procedure_occurrence_id > ? ORDER BY procedure_occurrence_id LIMIT ?");
		sqlMap.put("ImportOmopProvider", "SELECT provider_id, npi, dea, specialty_concept_id, care_site_id, provider_source_value, specialty_source_value, x_data_source_type, x_provider_first, x_provider_middle, x_provider_last, x_organization_id, x_grid_node_id FROM omop.provider WHERE provider_id > ? ORDER BY provider_id LIMIT ?");
		sqlMap.put("ImportOmopVisitOccurrence", "SELECT visit_occurrence_id, person_id, visit_start_date, visit_end_date, place_of_service_concept_id, care_site_id, place_of_service_source_value, x_visit_occurrence_source_identifier, x_data_source_type, x_provider_id, x_grid_node_id FROM omop.visit_occurrence WHERE visit_occurrence_id > ? ORDER BY visit_occurrence_id LIMIT ?");

		//OMOP Exports to GRID - insert into MySQL
		sqlMap.put("OmopCareSite", "INSERT INTO `care_site` (`care_site_id`,`care_site_source_value`,`x_data_source_type`,`location_id`,`organization_id`,`place_of_service_concept_id`,`place_of_service_source_value`,`x_care_site_name`,`x_grid_node_id`) VALUES (?,?,?,?,?,?,?,?,?);");
		//sqlMap.put("OmopConditionEra", "
		sqlMap.put("OmopDeath", "INSERT INTO `death`(`person_id`,`death_date`,`death_type_concept_id`,`cause_of_death_concept_id`,`cause_of_death_source_value`,`x_grid_node_id`) VALUES (?,?,?,?,?,?);");
		sqlMap.put("OmopDrugCost", "INSERT INTO `drug_cost`(`drug_cost_id`,`drug_exposure_id`,`paid_copay`,`paid_coinsurance`,`paid_toward_deductible`,`paid_by_payer`,`paid_by_coordination_benefits`,`total_out_of_pocket`,`total_paid`,`ingredient_cost`,`dispensing_fee`,`average_wholesale_price`,`payer_plan_period_id`,`x_grid_node_id`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		//sqlMap.put("OmopDrugEra", "
		sqlMap.put("OmopDrugExposure", "INSERT INTO `drug_exposure`(`drug_exposure_id`,`x_data_source_type`,`person_id`,`drug_concept_id`,`drug_source_value`,`drug_exposure_start_date`,`drug_exposure_end_date`,`drug_type_concept_id`,`stop_reason`,`refills`,`quantity`,`days_supply`,`x_drug_name`,`x_drug_strength`,`sig`,`prescribing_provider_id`,`visit_occurrence_id`,`relevant_condition_concept_id`,`x_grid_node_id`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
		sqlMap.put("OmopLocation", "INSERT INTO `location`(`location_id`,`location_source_value`,`x_data_source_type`,`address_1`,`address_2`,`city`,`state`,`zip`,`county`,`x_zip_deidentified`,`x_location_type`,`x_grid_node_id`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);");
		sqlMap.put("OmopObservation", "INSERT INTO `observation`(`observation_id`,`x_data_source_type`,`person_id`,`observation_concept_id`,`observation_source_value`,`observation_date`,`observation_time`,`value_as_number`,`value_as_string`,`value_as_concept_id`,`unit_concept_id`,`unit_source_value`,`range_low`,`range_high`,`observation_type_concept_id`,`associated_provider_id`,`visit_occurrence_id`,`relevant_condition_concept_id`,`x_obs_comment`,`x_grid_node_id`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
		//sqlMap.put("OmopObservationPeriod", "
		sqlMap.put("OmopOrganization", "INSERT INTO `organization`(`organization_id`,`organization_source_value`,`x_data_source_type`,`place_of_service_concept_id`,`place_of_service_source_value`,`location_id`,`x_grid_node_id`) VALUES (?,?,?,?,?,?,?);");
		sqlMap.put("OmopPayerPlanPeriod", "INSERT INTO `payer_plan_period`(`payer_plan_period_id`,`person_id`,`payer_plan_period_start_date`,`payer_plan_period_end_date`,`payer_source_value`,`plan_source_value`,`family_source_value`,`x_grid_node_id`) VALUES (?,?,?,?,?,?,?,?);");
		sqlMap.put("OmopPerson", "INSERT INTO `person`(`person_id`,`person_source_value`,`location_id`,`year_of_birth`,`month_of_birth`,`day_of_birth`,`gender_concept_id`,`gender_source_value`,`race_concept_id`,`race_source_value`,`ethnicity_concept_id`,`ethnicity_source_value`,`provider_id`,`care_site_id`,`x_organization_id`,`x_grid_node_id`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
		sqlMap.put("OmopProcedureCost", "INSERT INTO `procedure_cost`(`procedure_cost_id`,`procedure_occurrence_id`,`paid_copay`,`paid_coinsurance`,`paid_toward_deductible`,`paid_by_payer`,`paid_by_coordination_benefits`,`total_out_of_pocket`,`total_paid`,`disease_class_concept_id`,`disease_class_source_value`,`revenue_code_concept_id`,`revenue_code_source_value`,`payer_plan_period_id`,`x_grid_node_id`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
		sqlMap.put("OmopProcedureOccurrence", "INSERT INTO `procedure_occurrence`(`procedure_occurrence_id`,`x_data_source_type`,`person_id`,`procedure_concept_id`,`procedure_source_value`,`procedure_date`,`procedure_type_concept_id`,`associated_provider_id`,`visit_occurrence_id`,`relevant_condition_concept_id`,`x_grid_node_id`) VALUES (?,?,?,?,?,?,?,?,?,?,?);");
		sqlMap.put("OmopProvider", "INSERT INTO `provider`(`provider_id`,`provider_source_value`,`x_data_source_type`,`npi`,`dea`,`specialty_concept_id`,`specialty_source_value`,`x_provider_first`,`x_provider_middle`,`x_provider_last`,`care_site_id`,`x_organization_id`,`x_grid_node_id`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);");
		sqlMap.put("OmopVisitOccurrence", "INSERT INTO `visit_occurrence`(`visit_occurrence_id`,`x_visit_occurrence_source_identifier`,`x_data_source_type`,`person_id`,`visit_start_date`,`visit_end_date`,`place_of_service_concept_id`,`place_of_service_source_value`,`x_provider_id`,`care_site_id`,`x_grid_node_id`) VALUES (?,?,?,?,?,?,?,?,?,?,?);");
		
		
		sqlMap.put("OmopXCohortMetada", "INSERT INTO omop.x_cohort_metadata(cohort_metadata_id,cohort_name,cohort_description,cohort_creator_name,cohort_creator_contact,is_cohort_shared,cohort_query,service_url,original_cohort_count,last_cohort_count,original_cohort_date,last_cohort_date,cohort_phi_notes,cohort_other_notes,cohort_expiration_date,grid_node_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		sqlMap.put("OmopCohort", "INSERT INTO omop.cohort(cohort_id,x_data_source_type,cohort_concept_id,cohort_start_date,cohort_end_date,subject_id,stop_reason,x_cohort_metadata_id,x_grid_node_id) VALUES (?,?,?,?,?,?,?,?,?)");
		
		truncateList.add("TRUNCATE TABLE raw.X_DEMOGRAPHIC;");
		truncateList.add("TRUNCATE TABLE raw.VISIT_OCCURRENCE;");
		truncateList.add("TRUNCATE TABLE raw.PROCEDURE_OCCURRENCE;");
		truncateList.add("TRUNCATE TABLE raw.CONDITION_OCCURRENCE;");
		truncateList.add("TRUNCATE TABLE raw.OBSERVATION;");
		truncateList.add("TRUNCATE TABLE raw.DRUG_EXPOSURE;");
		truncateList.add("TRUNCATE TABLE raw.PROVIDER;");
		truncateList.add("TRUNCATE TABLE raw.ORGANIZATION;");
		truncateList.add("TRUNCATE TABLE raw.CARE_SITE;");
		truncateList.add("TRUNCATE TABLE raw.DRUG_COST;");
		truncateList.add("TRUNCATE TABLE raw.COHORT;");
		truncateList.add("TRUNCATE TABLE raw.PAYER_PLAN_PERIOD;");
		truncateList.add("TRUNCATE TABLE raw.PROCEDURE_COST;");
		truncateList.add("TRUNCATE TABLE raw.DEATH;");
		
		deleteFromSourceList.add("DELETE FROM raw.X_DEMOGRAPHIC WHERE x_data_source_id=_datasourceid_;");
		deleteFromSourceList.add("DELETE FROM raw.VISIT_OCCURRENCE WHERE x_data_source_id=_datasourceid_;");
		deleteFromSourceList.add("DELETE FROM raw.PROCEDURE_OCCURRENCE WHERE x_data_source_id=_datasourceid_;");
		deleteFromSourceList.add("DELETE FROM raw.CONDITION_OCCURRENCE WHERE x_data_source_id=_datasourceid_;");
		deleteFromSourceList.add("DELETE FROM raw.OBSERVATION WHERE x_data_source_id=_datasourceid_;");
		deleteFromSourceList.add("DELETE FROM raw.DRUG_EXPOSURE WHERE x_data_source_id=_datasourceid_;");
		deleteFromSourceList.add("DELETE FROM raw.PROVIDER WHERE x_data_source_id=_datasourceid_;");
		deleteFromSourceList.add("DELETE FROM raw.ORGANIZATION WHERE x_data_source_id=_datasourceid_;");
		deleteFromSourceList.add("DELETE FROM raw.CARE_SITE WHERE x_data_source_id=_datasourceid_;");
		deleteFromSourceList.add("DELETE FROM raw.DRUG_COST WHERE x_data_source_id=_datasourceid_;");
		deleteFromSourceList.add("DELETE FROM raw.COHORT WHERE x_data_source_id=_datasourceid_;");
		deleteFromSourceList.add("DELETE FROM raw.PAYER_PLAN_PERIOD WHERE x_data_source_id=_datasourceid_;");
		deleteFromSourceList.add("DELETE FROM raw.PROCEDURE_COST WHERE x_data_source_id=_datasourceid_;");
		deleteFromSourceList.add("DELETE FROM raw.DEATH WHERE x_data_source_id=_datasourceid_;");
		
		//Run-on query gets around MySQL restriction, revise this...
		gridTruncateList.add("SET FOREIGN_KEY_CHECKS=0;" +
				"TRUNCATE TABLE care_site; " +
				"TRUNCATE TABLE cohort;" +
				"TRUNCATE TABLE condition_occurrence;" +
				"TRUNCATE TABLE death;" +
				"TRUNCATE TABLE drug_cost;" +
				"TRUNCATE TABLE drug_exposure;" +
				"TRUNCATE TABLE location;" +
				"TRUNCATE TABLE observation;" +
				"TRUNCATE TABLE organization;" +
				"TRUNCATE TABLE payer_plan_period;" +
				"TRUNCATE TABLE person;" +
				"TRUNCATE TABLE procedure_cost;" +
				"TRUNCATE TABLE procedure_occurrence;" +
				"TRUNCATE TABLE provider;" +
				"TRUNCATE TABLE visit_occurrence;" +
				"SET FOREIGN_KEY_CHECKS=1;");
		
		profileList.add("select cz.czx_oscar_analyze(-1, 'std', 'x_demographic');");
		profileList.add("select cz.czx_oscar_analyze(-1, 'std', 'visit_occurrence');");
		profileList.add("select cz.czx_oscar_analyze(-1, 'std', 'procedure_occurrence');");
		profileList.add("select cz.czx_oscar_analyze(-1, 'std', 'condition_occurrence');");
		profileList.add("select cz.czx_oscar_analyze(-1, 'std', 'observation');");
		profileList.add("select cz.czx_oscar_analyze(-1, 'std', 'drug_exposure');");
		profileList.add("select cz.czx_oscar_analyze(-1, 'std', 'provider');");
		profileList.add("select cz.czx_oscar_analyze(-1, 'std', 'organization');");
		profileList.add("select cz.czx_oscar_analyze(-1, 'std', 'care_site');");
		profileList.add("select cz.czx_oscar_analyze(-1, 'std', 'drug_cost');");
		profileList.add("select cz.czx_oscar_analyze(-1, 'std', 'cohort');");
		profileList.add("select cz.czx_oscar_analyze(-1, 'std', 'payer_plan_period');");
        profileList.add("select cz.czx_oscar_analyze(-1, 'std', 'procedure_cost');");
		profileList.add("select cz.czx_oscar_analyze(-1, 'std', 'death');");
		profileList.add("select cz.czx_concept_analyze(-1);");
		
		omopProfileList.add("select cz.czx_oscar_analyze(-1, 'omop', 'care_site');");
		omopProfileList.add("select cz.czx_oscar_analyze(-1, 'omop', 'cohort');");
		omopProfileList.add("select cz.czx_oscar_analyze(-1, 'omop', 'condition_era');");
		omopProfileList.add("select cz.czx_oscar_analyze(-1, 'omop', 'condition_occurrence');");
		omopProfileList.add("select cz.czx_oscar_analyze(-1, 'omop', 'death');");
		omopProfileList.add("select cz.czx_oscar_analyze(-1, 'omop', 'drug_cost');");
		omopProfileList.add("select cz.czx_oscar_analyze(-1, 'omop', 'drug_era');");
		omopProfileList.add("select cz.czx_oscar_analyze(-1, 'omop', 'drug_exposure');");
		omopProfileList.add("select cz.czx_oscar_analyze(-1, 'omop', 'location');");
		omopProfileList.add("select cz.czx_oscar_analyze(-1, 'omop', 'observation');");
		omopProfileList.add("select cz.czx_oscar_analyze(-1, 'omop', 'observation_period');");
		omopProfileList.add("select cz.czx_oscar_analyze(-1, 'omop', 'organization');");
		omopProfileList.add("select cz.czx_oscar_analyze(-1, 'omop', 'payer_plan_period');");
		omopProfileList.add("select cz.czx_oscar_analyze(-1, 'omop', 'person');");
		omopProfileList.add("select cz.czx_oscar_analyze(-1, 'omop', 'procedure_cost');");
		omopProfileList.add("select cz.czx_oscar_analyze(-1, 'omop', 'procedure_occurrence');");
		omopProfileList.add("select cz.czx_oscar_analyze(-1, 'omop', 'provider');");
		omopProfileList.add("select cz.czx_oscar_analyze(-1, 'omop', 'visit_occurrence');");

	}
	  
	public String get(String name) {
		return sqlMap.get(name);
	}
	
	public List<String> getProcessSql(Long jobId) {
		List<String> processStrings = new ArrayList<String>();

		processStrings.add("select cz.czx_concept_analyze(" + jobId + ");");
		processStrings.add("select omop.omx_load_location(" + jobId + ");");
		processStrings.add("select omop.omx_load_organization(" + jobId + ");");
		processStrings.add("select omop.omx_load_care_site(" + jobId + ");");
		processStrings.add("select omop.omx_load_provider(" + jobId + ");");
        processStrings.add("select omop.omx_load_person(" + jobId + ");");
        processStrings.add("select omop.omx_load_payer_plan_period(" + jobId + ");");
		processStrings.add("select omop.omx_load_visit_occurrence(" + jobId + ");");
		processStrings.add("select omop.omx_load_condition_occurrence(" + jobId + ");");
		processStrings.add("select omop.omx_load_drug_exposure(" + jobId + ");");
		processStrings.add("select omop.omx_load_drug_cost(" + jobId + ");");
		processStrings.add("select omop.omx_load_procedure_occurrence(" + jobId + ");");
		processStrings.add("select omop.omx_load_procedure_cost(" + jobId + ");");
        processStrings.add("select omop.omx_load_observation(" + jobId + ");");
        processStrings.add("select omop.omx_load_death(" + jobId + ");");

		return processStrings;
	}
	
	public List<String> getConceptMapDelete() {
		List<String> statements = new ArrayList<String>();
		
		statements.add("delete from rz.source_to_concept_map where map_source = 'SAFTINet';");
		return statements;
	}

	public List<String> getOmopTablesMapDelete() {
		List<String> statements = new ArrayList<String>();
		
		statements.add("delete from omop.cohort where x_grid_node_id > 0 ;");
		statements.add("delete from omop.x_cohort_metadata where grid_node_id > 0 ;");
		
		return statements;
	}
	
}
