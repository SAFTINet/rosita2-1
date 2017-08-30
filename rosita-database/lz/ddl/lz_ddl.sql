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

-- Table: lz.lz_src_care_site

DROP TABLE IF EXISTS lz.lz_src_care_site;

CREATE TABLE lz.lz_src_care_site
(
  care_site_id character varying(200),
  organization_id character varying(200),
  care_site_source_value character varying(200),
  x_data_source_type character varying(200),
  organization_source_value character varying(200),
  place_of_service_source_value character varying(200),
  x_care_site_name character varying(200),
  care_site_address_1 character varying(200),
  care_site_address_2 character varying(200),
  care_site_city character varying(200),
  care_site_state character varying(200),
  care_site_zip character varying(200),
  care_site_county character varying(200),
  data_source_id	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE lz.lz_src_care_site
  OWNER TO rosita;

-- Table: lz.lz_src_cohort

DROP TABLE IF EXISTS lz.lz_src_cohort;

CREATE TABLE lz.lz_src_cohort
(
  cohort_id character varying(200),
  x_demographic_id character varying(200),
  cohort_source_identifier character varying(200),
  cohort_source_value character varying(200),
  cohort_start_date character varying(200),
  cohort_end_date character varying(200),
  subject_source_identifier character varying(200),
  stop_reason character varying(200),
  data_source_id	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE lz.lz_src_cohort
  OWNER TO rosita;

-- Table: lz.lz_src_condition_occurrence

DROP TABLE IF EXISTS lz.lz_src_condition_occurrence;

CREATE TABLE lz.lz_src_condition_occurrence
(
  condition_occurrence_id character varying(200),
  x_demographic_id character varying(200),
  condition_occurrence_source_identifier character varying(200),
  x_data_source_type character varying(200),
  person_source_value character varying(200),
  condition_source_value character varying(200),
  condition_source_value_vocabulary character varying(200),
  x_condition_source_desc character varying(200),
  condition_start_date character varying(200),
  x_condition_update_date character varying(200),
  condition_end_date character varying(200),
  condition_type_source_value character varying(200),
  stop_reason character varying(200),
  associated_provider_source_value character varying(200),
  x_visit_occurrence_source_identifier character varying(200),
  data_source_id	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE lz.lz_src_condition_occurrence
  OWNER TO rosita;

-- Index: lz.lz_cond_occur_prov_src_idx

DROP INDEX IF EXISTS lz.lz_cond_occur_prov_src_idx;

CREATE INDEX lz_cond_occur_prov_src_idx
  ON lz.lz_src_condition_occurrence
  USING btree
  (data_source_id, associated_provider_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: lz.lz_cond_occur_person_src_idx

DROP INDEX IF EXISTS lz.lz_cond_occur_person_src_idx;

CREATE INDEX lz_cond_occur_person_src_idx
  ON lz.lz_src_condition_occurrence
  USING btree
  (data_source_id, person_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: lz.lz_cond_occur_src_id_idx

DROP INDEX IF EXISTS lz.lz_cond_occur_src_id_idx;

CREATE INDEX lz_cond_occur_src_id_idx
  ON lz.lz_src_condition_occurrence
  USING btree
  (data_source_id, condition_occurrence_source_identifier COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: lz.lz_cond_occur_visit_src_id_idx

DROP INDEX IF EXISTS lz.lz_cond_occur_visit_src_id_idx;

CREATE INDEX lz_cond_occur_visit_src_id_idx
  ON lz.lz_src_condition_occurrence
  USING btree
  (data_source_id, x_visit_occurrence_source_identifier COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Table: lz.lz_src_death

DROP TABLE IF EXISTS lz.lz_src_death;

CREATE TABLE lz.lz_src_death
(
  death_id character varying(200),
  x_demographic_id character varying(200),
  person_source_value character varying(200),
  death_date character varying(200),
  death_type_concept_id character varying(200),
  death_type_source_value character varying(200),
  cause_of_death_source_value character varying(200),
  data_source_id	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE lz.lz_src_death
  OWNER TO rosita;

-- Table: lz.lz_src_drug_cost

DROP TABLE IF EXISTS lz.lz_src_drug_cost;

CREATE TABLE lz.lz_src_drug_cost
(
  drug_cost_id character varying(200),
  drug_exposure_id character varying(200),
  drug_cost_source_identifier character varying(200),
  drug_exposure_source_identifier character varying(200),
  paid_copay character varying(200),
  paid_coinsurance character varying(200),
  paid_toward_deductible character varying(200),
  paid_by_payer character varying(200),
  paid_by_coordination_of_benefits character varying(200),
  total_out_of_pocket character varying(200),
  total_paid character varying(200),
  ingredient_cost character varying(200),
  dispensing_fee character varying(200),
  average_wholesale_price character varying(200),
  payer_plan_period_source_identifier character varying(200),
  data_source_id	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE lz.lz_src_drug_cost
  OWNER TO rosita;

-- Table: lz.lz_src_drug_exposure

DROP TABLE IF EXISTS lz.lz_src_drug_exposure;

CREATE TABLE lz.lz_src_drug_exposure
(
  drug_exposure_id character varying(200),
  x_demographic_id character varying(200),
  drug_exposure_source_identifier character varying(200),
  x_data_source_type character varying(200),
  person_source_value character varying(200),
  drug_source_value character varying(200),
  drug_source_value_vocabulary character varying(200),
  drug_exposure_start_date character varying(200),
  drug_exposure_end_date character varying(200),
  drug_type_source_value character varying(200),
  stop_reason character varying(200),
  refills character varying(200),
  quantity character varying(200),
  days_supply character varying(200),
  x_drug_name character varying(255),
  x_drug_strength character varying(200),
  sig character varying(500),
  provider_source_value character varying(200),
  x_visit_occurrence_source_identifier character varying(200),
  relevant_condition_source_value character varying(200),
  data_source_id	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE lz.lz_src_drug_exposure
  OWNER TO rosita;

-- Index: lz.lz_drug_exp_person_src_idx

DROP INDEX IF EXISTS lz.lz_drug_exp_person_src_idx;

CREATE INDEX lz_drug_exp_person_src_idx
  ON lz.lz_src_drug_exposure
  USING btree
  (data_source_id, person_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: lz.lz_drug_exp_src_id_idx

DROP INDEX IF EXISTS lz.lz_drug_exp_src_id_idx;

CREATE INDEX lz_drug_exp_src_id_idx
  ON lz.lz_src_drug_exposure
  USING btree
  (data_source_id, drug_exposure_source_identifier COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Table: lz.lz_src_observation

DROP TABLE IF EXISTS lz.lz_src_observation;

CREATE TABLE lz.lz_src_observation
(
  observation_id character varying(200),
  x_demographic_id character varying(200),
  observation_source_identifier character varying(200),
  x_data_source_type character varying(200),
  person_source_value character varying(200),
  observation_source_value character varying(200),
  observation_source_value_vocabulary character varying(200),
  observation_date character varying(200),
  observation_time character varying(200),
  value_as_number character varying(200),
  value_as_string character varying(500),
  unit_source_value character varying(200),
  range_low character varying(200),
  range_high character varying(200),
  observation_type_source_value character varying(200),
  associated_provider_source_value character varying(200),
  x_visit_occurrence_source_identifier character varying(200),
  relevant_condition_source_value character varying(200),
  x_obs_comment character varying(500),
  data_source_id	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE lz.lz_src_observation
  OWNER TO rosita;

-- Index: lz.lz_obs_person_src_idx

DROP INDEX IF EXISTS lz.lz_obs_person_src_idx;

CREATE INDEX lz_obs_person_src_idx
  ON lz.lz_src_observation
  USING btree
  (data_source_id, person_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: lz.lz_obs_visit_src_id_idx

DROP INDEX IF EXISTS lz.lz_obs_visit_src_id_idx;

CREATE INDEX lz_obs_visit_src_id_idx
  ON lz.lz_src_observation
  USING btree
  (data_source_id, x_visit_occurrence_source_identifier COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: lz.lz_obs_src_idx

DROP INDEX IF EXISTS lz.lz_obs_src_idx;

CREATE INDEX lz_obs_src_idx
  ON lz.lz_src_observation
  USING btree
  (data_source_id, observation_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;
  
-- Table: lz.lz_src_organization

DROP TABLE IF EXISTS lz.lz_src_organization;

CREATE TABLE lz.lz_src_organization
(
  organization_id character varying(10),
  organization_source_value character varying(200),
  x_data_source_type character varying(200),
  place_of_service_source_value character varying(200),
  organization_address_1 character varying(200),
  organization_address_2 character varying(200),
  organization_city character varying(200),
  organization_state character varying(200),
  organization_zip character varying(200),
  organization_county character varying(200),
  data_source_id	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE lz.lz_src_organization
  OWNER TO rosita;

-- Table: lz.lz_src_payer_plan_period

DROP TABLE IF EXISTS lz.lz_src_payer_plan_period;

CREATE TABLE lz.lz_src_payer_plan_period
(
  payer_plan_period_id character varying(200),
  x_demographic_id character varying(200),
  payer_plan_period_source_identifier character varying(200),
  person_source_value character varying(200),
  payer_plan_period_start_date character varying(200),
  payer_plan_period_end_date character varying(200),
  payer_source_value character varying(200),
  plan_source_value character varying(200),
  family_source_value character varying(200),
  data_source_id	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE lz.lz_src_payer_plan_period
  OWNER TO rosita;

-- Table: lz.lz_src_procedure_cost

DROP TABLE IF EXISTS lz.lz_src_procedure_cost;

CREATE TABLE lz.lz_src_procedure_cost
(
  procedure_cost_id character varying(200),
  procedure_occurrence_id character varying(200),
  procedure_cost_source_identifier character varying(200),
  procedure_occurrence_source_identifier character varying(200),
  paid_copay character varying(200),
  paid_coinsurance character varying(200),
  paid_toward_deductible character varying(200),
  paid_by_payer character varying(200),
  paid_by_coordination_of_benefits character varying(200),
  total_out_of_pocket character varying(200),
  total_paid character varying(200),
  disease_class_concept_id character varying(200),
  disease_class_source_value character varying(200),
  revenue_code_concept_id character varying(200),
  revenue_code_source_value character varying(200),
  payer_plan_period_source_identifier character varying(200),
  data_source_id	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE lz.lz_src_procedure_cost
  OWNER TO rosita;

-- Table: lz.lz_src_procedure_occurrence

DROP TABLE IF EXISTS lz.lz_src_procedure_occurrence;

CREATE TABLE lz.lz_src_procedure_occurrence
(
  procedure_occurrence_id character varying(200),
  x_demographic_id character varying(200),
  procedure_occurrence_source_identifier character varying(200),
  x_data_source_type character varying(200),
  person_source_value character varying(200),
  procedure_source_value character varying(200),
  procedure_source_value_vocabulary character varying(200),
  procedure_date character varying(200),
  procedure_type_source_value character varying(200),
  provider_record_source_value character varying(200),
  x_visit_occurrence_source_identifier character varying(200),
  relevant_condition_source_value character varying(200),
  data_source_id	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE lz.lz_src_procedure_occurrence
  OWNER TO rosita;

-- Index: lz.lz_proc_occur_src_id_idx

DROP INDEX IF EXISTS lz.lz_proc_occur_src_id_idx;

CREATE INDEX lz_proc_occur_src_id_idx
  ON lz.lz_src_procedure_occurrence
  USING btree
  (data_source_id, procedure_occurrence_source_identifier COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: lz.lz_proc_occur_person_src_idx

DROP INDEX IF EXISTS lz.lz_proc_occur_person_src_idx;

CREATE INDEX lz_proc_occur_person_src_idx
  ON lz.lz_src_procedure_occurrence
  USING btree
  (data_source_id, person_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: lz.lz_proc_occur_cond_src_idx

DROP INDEX IF EXISTS lz.lz_proc_occur_cond_src_idx;

CREATE INDEX lz_proc_occur_cond_src_idx
  ON lz.lz_src_procedure_occurrence
  USING btree
  (data_source_id, relevant_condition_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: lz.lz_proc_occur_visit_src_id_idx

DROP INDEX IF EXISTS lz.lz_proc_occur_visit_src_id_idx;

CREATE INDEX lz_proc_occur_visit_src_id_idx
  ON lz.lz_src_procedure_occurrence
  USING btree
  (data_source_id, x_visit_occurrence_source_identifier COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;
  
-- Table: lz.lz_src_provider

DROP TABLE IF EXISTS lz.lz_src_provider;

CREATE TABLE lz.lz_src_provider
(
  provider_id character varying(200),
  care_site_id character varying(200),
  provider_source_value character varying(200),
  x_data_source_type character varying(200),
  npi character varying(200),
  dea character varying(200),
  specialty_source_value character varying(200),
  x_provider_first character varying(200),
  x_provider_middle character varying(200),
  x_provider_last character varying(200),
  care_site_source_value character varying(200),
  x_organization_source_value character varying(200),
  data_source_id	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE lz.lz_src_provider
  OWNER TO rosita;

-- Table: lz.lz_src_visit_occurrence

DROP TABLE IF EXISTS lz.lz_src_visit_occurrence;

CREATE TABLE lz.lz_src_visit_occurrence
(
  visit_occurrence_id character varying(200),
  x_demographic_id character varying(200),
  x_visit_occurrence_source_identifier character varying(200),
  x_data_source_type character varying(200),
  person_source_value character varying(200),
  visit_start_date character varying(200),
  visit_end_date character varying(200),
  place_of_service_source_value character varying(200),
  x_provider_source_value character varying(200),
  care_site_source_value character varying(200),
  data_source_id	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE lz.lz_src_visit_occurrence
  OWNER TO rosita;

-- Index: lz.lz_visit_occur_care_site_src_idx

DROP INDEX IF EXISTS lz.lz_visit_occur_care_site_src_idx;

CREATE INDEX lz_visit_occur_care_site_src_idx
  ON lz.lz_src_visit_occurrence
  USING btree
  (data_source_id, care_site_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: lz.lz_visit_occur_place_srvc_src_idx

DROP INDEX IF EXISTS lz.lz_visit_occur_place_srvc_src_idx;

CREATE INDEX lz_visit_occur_place_srvc_src_idx
  ON lz.lz_src_visit_occurrence
  USING btree
  (data_source_id, place_of_service_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: lz.lz_visit_occur_prov_src_idx

DROP INDEX IF EXISTS lz.lz_visit_occur_prov_src_idx;

CREATE INDEX lz_visit_occur_prov_src_idx
  ON lz.lz_src_visit_occurrence
  USING btree
  (data_source_id, x_provider_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: lz.lz_visit_occur_person_src_idx

DROP INDEX IF EXISTS lz.lz_visit_occur_person_src_idx;

CREATE INDEX lz_visit_occur_person_src_idx
  ON lz.lz_src_visit_occurrence
  USING btree
  (data_source_id, person_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Table: lz.lz_src_x_demographic

DROP TABLE IF EXISTS lz.lz_src_x_demographic;

CREATE TABLE lz.lz_src_x_demographic
(
  x_demographic_id character varying(200),
  organization_id character varying(200),
  person_source_value character varying(200),
  x_data_source_type character varying(200),
  medicaid_id_number character varying(200),
  ssn character varying(200),
  last character varying(200),
  middle character varying(200),
  first character varying(200),
  address_1 character varying(200),
  address_2 character varying(200),
  city character varying(200),
  state character varying(200),
  zip character varying(200),
  county character varying(200),
  year_of_birth character varying(200),
  month_of_birth character varying(200),
  day_of_birth character varying(200),
  gender_source_value character varying(200),
  race_source_value character varying(200),
  ethnicity_source_value character varying(200),
  provider_source_value character varying(200),
  care_site_source_value character varying(200),
  x_organization_source_value character varying(200),
  data_source_id	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE lz.lz_src_x_demographic
  OWNER TO rosita;

-- Index: lz.lz_demo_city_state_zip_idx

DROP INDEX IF EXISTS lz.lz_demo_city_state_zip_idx;

CREATE INDEX lz_demo_city_state_zip_idx
  ON lz.lz_src_x_demographic
  USING btree
  (data_source_id, city COLLATE pg_catalog."default" , state COLLATE pg_catalog."default" , zip COLLATE pg_catalog."default" , county COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: lz.lz_demo_care_site_src_idx

DROP INDEX IF EXISTS lz.lz_demo_care_site_src_idx;

CREATE INDEX lz_demo_care_site_src_idx
  ON lz.lz_src_x_demographic
  USING btree
  (data_source_id, care_site_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: lz.lz_demo_id_idx

DROP INDEX IF EXISTS lz.lz_demo_id_idx;

CREATE INDEX lz_demo_id_idx
  ON lz.lz_src_x_demographic
  USING btree
  (data_source_id, x_demographic_id COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: lz.lz_demo_org_src_idx

DROP INDEX IF EXISTS lz.lz_demo_org_src_idx;

CREATE INDEX lz_demo_org_src_idx
  ON lz.lz_src_x_demographic
  USING btree
  (data_source_id, x_organization_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: lz.lz_demo_person_src_idx

DROP INDEX IF EXISTS lz.lz_demo_person_src_idx;

CREATE INDEX lz_demo_person_src_idx
  ON lz.lz_src_x_demographic
  USING btree
  (data_source_id, person_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: lz.lz_demo_prov_src_idx

DROP INDEX IF EXISTS lz.lz_demo_prov_src_idx;

CREATE INDEX lz_demo_prov_src_idx
  ON lz.lz_src_x_demographic
  USING btree
  (data_source_id, provider_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;



