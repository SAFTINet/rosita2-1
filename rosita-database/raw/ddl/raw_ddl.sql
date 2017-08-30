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

-- Table: raw.care_site

DROP TABLE IF EXISTS raw.care_site;

CREATE TABLE raw.care_site
(
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
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE raw.care_site
  OWNER TO rosita;

-- Table: raw.cohort

DROP TABLE IF EXISTS raw.cohort;

CREATE TABLE raw.cohort
(
  cohort_source_identifier character varying(200),
  x_data_source_type character varying(200),
  cohort_source_value character varying(200),
  cohort_start_date character varying(200),
  cohort_end_date character varying(200),
  subject_source_identifier character varying(200),
  stop_reason character varying(200),
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE raw.cohort
  OWNER TO rosita;

-- Table: raw.condition_occurrence

DROP TABLE IF EXISTS raw.condition_occurrence;

CREATE TABLE raw.condition_occurrence
(
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
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE raw.condition_occurrence
  OWNER TO rosita;

-- Table: raw.death

DROP TABLE IF EXISTS raw.death;

CREATE TABLE raw.death
(
  person_source_value character varying(200),
  x_data_source_type character varying(200),
  death_date character varying(200),
  death_type_source_value character varying(200),
  cause_of_death_source_value character varying(200),
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE raw.death
  OWNER TO rosita;

-- Table: raw.drug_cost

DROP TABLE IF EXISTS raw.drug_cost;

CREATE TABLE raw.drug_cost
(
  drug_cost_source_identifier character varying(200),
  x_data_source_type character varying(200),
  drug_exposure_source_identifier character varying(200),
  paid_copay character varying(200),
  paid_coinsurance character varying(200),
  paid_toward_deductible character varying(200),
  paid_by_payer character varying(200),
  paid_by_coordination_benefits character varying(200),
  total_out_of_pocket character varying(200),
  total_paid character varying(200),
  ingredient_cost character varying(200),
  dispensing_fee character varying(200),
  average_wholesale_price character varying(200),
  payer_plan_period_source_identifier character varying(200),
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE raw.drug_cost
  OWNER TO rosita;

-- Table: raw.drug_exposure

DROP TABLE IF EXISTS raw.drug_exposure;

CREATE TABLE raw.drug_exposure
(
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
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE raw.drug_exposure
  OWNER TO rosita;

-- Table: raw.observation

DROP TABLE IF EXISTS raw.observation;

CREATE TABLE raw.observation
(
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
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE raw.observation
  OWNER TO rosita;

-- Table: raw.organization

DROP TABLE IF EXISTS raw.organization;

CREATE TABLE raw.organization
(
  organization_source_value character varying(200),
  x_data_source_type character varying(200),
  place_of_service_source_value character varying(200),
  organization_address_1 character varying(200),
  organization_address_2 character varying(200),
  organization_city character varying(200),
  organization_state character varying(200),
  organization_zip character varying(200),
  organization_county character varying(200),
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE raw.organization
  OWNER TO rosita;

-- Table: raw.payer_plan_period

DROP TABLE IF EXISTS raw.payer_plan_period;

CREATE TABLE raw.payer_plan_period
(
  payer_plan_period_source_identifier character varying(200),
  x_data_source_type character varying(200),
  person_source_value character varying(200),
  payer_plan_period_start_date character varying(200),
  payer_plan_period_end_date character varying(200),
  payer_source_value character varying(200),
  plan_source_value character varying(200),
  family_source_value character varying(200),
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE raw.payer_plan_period
  OWNER TO rosita;

-- Table: raw.procedure_cost

DROP TABLE IF EXISTS raw.procedure_cost;

CREATE TABLE raw.procedure_cost
(
  procedure_cost_source_identifier character varying(200),
  procedure_occurrence_source_identifier character varying(200),
  x_data_source_type character varying(200),
  paid_copay character varying(200),
  paid_coinsurance character varying(200),
  paid_toward_deductible character varying(200),
  paid_by_payer character varying(200),
  paid_by_coordination_benefits character varying(200),
  total_out_of_pocket character varying(200),
  total_paid character varying(200),
  disease_class_concept_id character varying(200),
  disease_class_source_value character varying(200),
  revenue_code_concept_id character varying(200),
  revenue_code_source_value character varying(200),
  payer_plan_period_source_identifier character varying(200),
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE raw.procedure_cost
  OWNER TO rosita;

-- Table: raw.procedure_occurrence

DROP TABLE IF EXISTS raw.procedure_occurrence;

CREATE TABLE raw.procedure_occurrence
(
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
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE raw.procedure_occurrence
  OWNER TO rosita;
  
-- Table: raw.provider

DROP TABLE IF EXISTS raw.provider;

CREATE TABLE raw.provider
(
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
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE raw.provider
  OWNER TO rosita;

-- Table: raw.visit_occurrence

DROP TABLE IF EXISTS raw.visit_occurrence;

CREATE TABLE raw.visit_occurrence
(
  x_visit_occurrence_source_identifier character varying(200),
  x_data_source_type character varying(200),
  person_source_value character varying(200),
  visit_start_date character varying(200),
  visit_end_date character varying(200),
  place_of_service_source_value character varying(200),
  x_provider_source_value character varying(200),
  care_site_source_value character varying(200),
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE raw.visit_occurrence
  OWNER TO rosita;

-- Table: raw.x_demographic

DROP TABLE IF EXISTS raw.x_demographic;

CREATE TABLE raw.x_demographic
(
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
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE raw.x_demographic
  OWNER TO rosita;
