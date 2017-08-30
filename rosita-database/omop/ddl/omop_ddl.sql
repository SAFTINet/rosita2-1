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

-- Sequence: omop.omop_care_site_sq

DROP SEQUENCE IF EXISTS omop.omop_care_site_sq CASCADE;

CREATE SEQUENCE omop.omop_care_site_sq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 2147483647
  START 1
  CACHE 10;
ALTER TABLE omop.omop_care_site_sq
  OWNER TO rosita;

-- Sequence: omop.omop_x_cohort_metadata_sq

DROP SEQUENCE IF EXISTS omop.omop_x_cohort_metadata_sq CASCADE;

CREATE SEQUENCE omop.omop_x_cohort_metadata_sq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 2147483647
  START 1
  CACHE 10;
ALTER TABLE omop.omop_x_cohort_metadata_sq
  OWNER TO rosita;

-- Sequence: omop.omop_care_site_sq

DROP SEQUENCE IF EXISTS omop.omop_cohort_sq CASCADE;

CREATE SEQUENCE omop.omop_cohort_sq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 2147483647
  START 1
  CACHE 10;
ALTER TABLE omop.omop_cohort_sq
  OWNER TO rosita;

-- Sequence: omop.omop_cond_era_sq

DROP SEQUENCE IF EXISTS omop.omop_cond_era_sq CASCADE;

CREATE SEQUENCE omop.omop_cond_era_sq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 2147483647
  START 1
  CACHE 10;
ALTER TABLE omop.omop_cond_era_sq
  OWNER TO rosita;
  
-- Sequence: omop.omop_cond_occur_sq

DROP SEQUENCE IF EXISTS omop.omop_cond_occur_sq CASCADE;

CREATE SEQUENCE omop.omop_cond_occur_sq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 2147483647
  START 1
  CACHE 10;
ALTER TABLE omop.omop_cond_occur_sq
  OWNER TO rosita;
  
-- Sequence: omop.omop_drug_cost_sq

DROP SEQUENCE IF EXISTS omop.omop_drug_cost_sq CASCADE;

CREATE SEQUENCE omop.omop_drug_cost_sq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 2147483647
  START 1
  CACHE 10;
ALTER TABLE omop.omop_drug_cost_sq
  OWNER TO rosita;

-- Sequence: omop.omop_drug_era_sq

DROP SEQUENCE IF EXISTS omop.omop_drug_era_sq CASCADE;

CREATE SEQUENCE omop.omop_drug_era_sq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 2147483647
  START 1
  CACHE 10;
ALTER TABLE omop.omop_drug_era_sq
  OWNER TO rosita;

-- Sequence: omop.omop_drug_exp_sq

DROP SEQUENCE IF EXISTS omop.omop_drug_exp_sq CASCADE;

CREATE SEQUENCE omop.omop_drug_exp_sq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 2147483647
  START 1
  CACHE 10;
ALTER TABLE omop.omop_drug_exp_sq
  OWNER TO rosita;
  
  -- Sequence: omop.omop_death_sq

DROP SEQUENCE IF EXISTS omop.omop_death_sq CASCADE;

CREATE SEQUENCE omop.omop_death_sq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 2147483647
  START 1
  CACHE 10;
ALTER TABLE omop.omop_death_sq
  OWNER TO rosita;

-- Sequence: omop.omop_location_sq

DROP SEQUENCE IF EXISTS omop.omop_location_sq CASCADE;

CREATE SEQUENCE omop.omop_location_sq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 2147483647
  START 1
  CACHE 10;
ALTER TABLE omop.omop_location_sq
  OWNER TO rosita;

-- Sequence: omop.omop_obs_sq

DROP SEQUENCE IF EXISTS omop.omop_obs_sq CASCADE;

CREATE SEQUENCE omop.omop_obs_sq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 2147483647
  START 1
  CACHE 10;
ALTER TABLE omop.omop_obs_sq
  OWNER TO rosita;

-- Sequence: omop.omop_obs_sq

DROP SEQUENCE IF EXISTS omop.omop_obs_period_sq CASCADE;

CREATE SEQUENCE omop.omop_obs_period_sq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 2147483647
  START 1
  CACHE 10;
ALTER TABLE omop.omop_obs_period_sq
  OWNER TO rosita;

-- Sequence: omop.omop_organ_sq

DROP SEQUENCE IF EXISTS omop.omop_organization_sq CASCADE;

CREATE SEQUENCE omop.omop_organization_sq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 2147483647
  START 1
  CACHE 10;
ALTER TABLE omop.omop_organization_sq
  OWNER TO rosita;

-- Sequence: omop.omop_oscar_result_sq

DROP SEQUENCE IF EXISTS omop.omop_oscar_result_sq CASCADE;

CREATE SEQUENCE omop.omop_oscar_result_sq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 2147483647
  START 1
  CACHE 10;
ALTER TABLE omop.omop_oscar_result_sq
  OWNER TO rosita;

-- Sequence: omop.omop_payer_plan_sq

DROP SEQUENCE IF EXISTS omop.omop_payer_plan_sq CASCADE;

CREATE SEQUENCE omop.omop_payer_plan_sq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 2147483647
  START 1
  CACHE 10;
ALTER TABLE omop.omop_payer_plan_sq
  OWNER TO rosita;

-- Sequence: omop.omop_proc_cost_sq

DROP SEQUENCE IF EXISTS omop.omop_proc_cost_sq CASCADE;

CREATE SEQUENCE omop.omop_proc_cost_sq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 2147483647
  START 1
  CACHE 10;
ALTER TABLE omop.omop_proc_cost_sq
  OWNER TO rosita;

-- Sequence: omop.omop_proc_occur_sq

DROP SEQUENCE IF EXISTS omop.omop_proc_occur_sq CASCADE;

CREATE SEQUENCE omop.omop_proc_occur_sq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 2147483647
  START 1
  CACHE 10;
ALTER TABLE omop.omop_proc_occur_sq
  OWNER TO rosita;

-- Sequence: omop.omop_provider_sq

DROP SEQUENCE IF EXISTS omop.omop_provider_sq CASCADE;

CREATE SEQUENCE omop.omop_provider_sq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 2147483647
  START 1
  CACHE 10;
ALTER TABLE omop.omop_provider_sq
  OWNER TO rosita;
  

-- Table: omop.care_site

DROP TABLE IF EXISTS omop.care_site;

CREATE TABLE omop.care_site
(
  care_site_id bigint NOT NULL DEFAULT nextval('omop.omop_care_site_sq'),
  location_id bigint NOT NULL,
  organization_id bigint NOT NULL,
  place_of_service_concept_id bigint,
  care_site_source_value character varying(50) NOT NULL,
  place_of_service_source_value character varying(50),
  x_data_source_type character varying(20) NOT NULL,
  x_care_site_name character varying(50),
  x_grid_node_id bigint NOT NULL,
  x_data_source_id	bigint NOT NULL
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.care_site
  OWNER TO rosita;

-- Index: omop.omop_care_site_source_idx

DROP INDEX IF EXISTS omop.omop_care_site_source_idx;

CREATE INDEX omop_care_site_source_idx
  ON omop.care_site
  USING btree
  (x_data_source_id, care_site_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Table: omop.cohort

DROP TABLE IF EXISTS omop.cohort;

CREATE TABLE omop.cohort
(
  cohort_id integer NOT NULL DEFAULT nextval('omop.omop_cohort_sq'),
  x_data_source_type character varying(20) NOT NULL,
  cohort_concept_id integer NOT NULL,
  cohort_start_date date NOT NULL,
  cohort_end_date date,
  subject_id integer NOT NULL,
  stop_reason character varying(20),
  x_cohort_metadata_id integer NOT NULL,
  x_grid_node_id integer NOT NULL,
  CONSTRAINT omop_cohort_pk1 PRIMARY KEY (cohort_id,x_grid_node_id)
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.cohort
  OWNER TO rosita;
  
-- Table: omop.condition_era

DROP TABLE IF EXISTS omop.condition_era;

CREATE TABLE omop.condition_era
(
  condition_era_id bigint NOT NULL DEFAULT nextval('omop.omop_cond_era_sq'),
  person_id bigint NOT NULL,
  condition_concept_id bigint NOT NULL,
  condition_era_start_date date NOT NULL,
  condition_era_end_date date NOT NULL,
  condition_type_concept_id bigint NOT NULL,
  condition_occurrence_count numeric(4,0),
  x_data_source_id bigint not null
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.condition_era
  OWNER TO rosita;

-- Table: omop.condition_occurrence

DROP TABLE IF EXISTS omop.condition_occurrence;

CREATE TABLE omop.condition_occurrence
(
  condition_occurrence_id bigint NOT NULL DEFAULT nextval('omop.omop_cond_occur_sq'),
  person_id bigint NOT NULL,
  condition_concept_id bigint NOT NULL,
  condition_start_date date NOT NULL,
  condition_end_date date,
  condition_type_concept_id bigint NOT NULL,
  stop_reason character varying(20),
  associated_provider_id bigint,
  visit_occurrence_id bigint,
  condition_source_value character varying(50),
  x_data_source_type character varying(20) NOT NULL,
  x_condition_source_desc character varying(50),
  x_condition_update_date date,
  x_grid_node_id bigint NOT NULL,
  x_data_source_id	bigint not null
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.condition_occurrence
  OWNER TO rosita;

-- Table: omop.death

DROP TABLE IF EXISTS omop.death;

CREATE TABLE omop.death
(
  death_id bigint NOT NULL DEFAULT nextval('omop.omop_death_sq'),
  x_data_source_type character varying(20) NOT NULL,
  person_id bigint NOT NULL,
  death_date date,
  death_type_concept_id bigint NOT NULL,
  cause_of_death_concept_id bigint,
  cause_of_death_source_value character varying(50),
  x_grid_node_id bigint NOT NULL,
  x_data_source_id bigint not null
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.death
  OWNER TO rosita;

-- Table: omop.drug_cost

DROP TABLE IF EXISTS omop.drug_cost;

CREATE TABLE omop.drug_cost
(
  drug_cost_id bigint NOT NULL DEFAULT nextval('omop.omop_drug_cost_sq'::regclass),
  x_data_source_type character varying(20) NOT NULL,
  drug_exposure_id bigint NOT NULL,
  paid_copay numeric(8,2),
  paid_coinsurance numeric(8,2),
  paid_toward_deductible numeric(8,2),
  paid_by_payer numeric(8,2),
  paid_by_coordination_benefits numeric(8,2),
  total_out_of_pocket numeric(8,2),
  total_paid numeric(8,2),
  ingredient_cost numeric(8,2),
  dispensing_fee numeric(8,2),
  average_wholesale_price numeric(8,2),
  payer_plan_period_id bigint,
  x_grid_node_id bigint NOT NULL,
  x_data_source_id bigint not null
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.drug_cost
  OWNER TO rosita;

-- Table: omop.drug_era

DROP TABLE IF EXISTS omop.drug_era;

CREATE TABLE omop.drug_era
(
  drug_era_id bigint NOT NULL DEFAULT nextval('omop.omop_drug_era_sq'::regclass),
  person_id bigint NOT NULL,
  drug_concept_id bigint NOT NULL,
  drug_era_start_date date NOT NULL,
  drug_era_end_date date NOT NULL,
  drug_type_concept_id bigint NOT NULL,
  drug_exposure_count numeric(4,0),
  x_data_source_id bigint not null
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.drug_era
  OWNER TO rosita;

-- Table: omop.drug_exposure

DROP TABLE IF EXISTS omop.drug_exposure;

CREATE TABLE omop.drug_exposure
(
  drug_exposure_id bigint NOT NULL DEFAULT nextval('omop.omop_drug_exp_sq'::regclass),
  person_id bigint NOT NULL,
  drug_concept_id bigint NOT NULL,
  drug_exposure_start_date date NOT NULL,
  drug_exposure_end_date date,
  drug_type_concept_id bigint NOT NULL,
  stop_reason character varying(20),
  refills numeric(4,0),
  quantity numeric(8,0),
  days_supply numeric(4,0),
  sig character varying(500),
  prescribing_provider_id bigint,
  visit_occurrence_id bigint,
  relevant_condition_concept_id bigint,
  drug_source_value character varying(50),
  x_data_source_type character varying(20) NOT NULL,
  x_drug_name character varying(255) NOT NULL,
  x_drug_strength character varying(50),
  x_grid_node_id bigint NOT NULL,
  x_data_source_id bigint not null
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.drug_exposure
  OWNER TO rosita;

-- Table: omop.location

DROP TABLE IF EXISTS omop.location;

CREATE TABLE omop.location
(
  location_id bigint NOT NULL DEFAULT nextval('omop.omop_location_sq'),
  address_1 character varying(50),
  address_2 character varying(50),
  city character varying(50),
  state character varying(2),
  zip character varying(9),
  county character varying(20),
  location_source_value character varying(50),
  x_zip_deidentified character varying(9),
  x_location_type character varying(20) NOT NULL,
  x_data_source_type character varying(20),
  x_grid_node_id bigint NOT NULL,
  x_data_source_id bigint not null
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.location
  OWNER TO rosita;

-- Index: omop.omop_location_source_type_idx

DROP INDEX IF EXISTS omop.omop_location_source_type_idx;

CREATE INDEX omop_location_source_type_idx
  ON omop.location
  USING btree
  (x_data_source_id, location_source_value COLLATE pg_catalog."default" , x_location_type COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: omop.omop_location_src_city_zip_state_idx

DROP INDEX IF EXISTS omop.omop_location_src_city_zip_state_idx;

CREATE INDEX omop_location_src_city_zip_state_idx
  ON omop.location
  USING btree
  (x_data_source_id, x_location_type COLLATE pg_catalog."default", city COLLATE pg_catalog."default", state COLLATE pg_catalog."default", zip COLLATE pg_catalog."default", county COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Table: omop.observation

DROP TABLE IF EXISTS omop.observation;

CREATE TABLE omop.observation
(
  observation_id bigint NOT NULL DEFAULT nextval('omop.omop_obs_sq'),
  person_id bigint NOT NULL,
  observation_concept_id bigint NOT NULL,
  observation_date date NOT NULL,
  observation_time time without time zone,
  value_as_number numeric(14,3),
  value_as_string character varying(60),
  value_as_concept_id bigint,
  unit_concept_id bigint,
  range_low numeric(14,3),
  range_high numeric(14,3),
  observation_type_concept_id bigint NOT NULL,
  associated_provider_id bigint,
  visit_occurrence_id bigint,
  relevant_condition_concept_id bigint,
  observation_source_value character varying(50) NOT NULL,
  unit_source_value character varying(50),
  x_data_source_type character varying(20) NOT NULL,
  x_obs_comment character varying(500),
  x_grid_node_id bigint NOT NULL,
  x_data_source_id bigint not null
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.observation
  OWNER TO rosita;

-- Table: omop.observation_period

DROP TABLE IF EXISTS omop.observation_period;

CREATE TABLE omop.observation_period
(
  observation_period_id bigint NOT NULL DEFAULT nextval('omop.omop_obs_period_sq'),
  person_id bigint NOT NULL,
  observation_period_start_date date NOT NULL,
  observation_period_end_date date NOT NULL,
  x_data_source_id bigint not null
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.observation_period
  OWNER TO rosita;

-- Table: omop.organization

DROP TABLE IF EXISTS omop.organization;

CREATE TABLE omop.organization
(
  organization_id bigint NOT NULL DEFAULT nextval('omop.omop_organization_sq'),
  place_of_service_concept_id bigint,
  location_id bigint NOT NULL,
  organization_source_value character varying(50) NOT NULL,
  place_of_service_source_value character varying(50),
  x_data_source_type character varying(20) NOT NULL,
  x_grid_node_id bigint NOT NULL,
  x_data_source_id bigint not null
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.organization
  OWNER TO rosita;

-- Index: omop.omop_organization_source_idx

DROP INDEX IF EXISTS omop.omop_organization_source_idx;

CREATE INDEX omop_organization_source_idx
  ON omop.organization
  USING btree
  (x_data_source_id, organization_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Table: omop.oscar_result

DROP TABLE IF EXISTS omop.oscar_result;

CREATE TABLE omop.oscar_result
(
  oscar_result_id bigint NOT NULL DEFAULT nextval('omop.omop_oscar_result_sq'::regclass),
  x_oscar_rule_id bigint,
  x_source_schema_name character varying(50),
  source_table_name character varying(50),
  variable_name character varying(50),
  variable_type character varying(2),
  summary_level character varying(2),
  variable_value character varying(2000),
  x_statistic_name character varying(50),
  statistic_type character varying(2),
  statistic_value character varying(2000),
  variable_description_level_1 character varying(2000),
  variable_value_level_1 character varying(2000),
  variable_description_level_2 character varying(2000),
  variable_value_level_2 character varying(2000),
  variable_description_level_3 character varying(2000),
  variable_value_level_3 character varying(2000),
  variable_description_level_4 character varying(2000),
  variable_value_level_4 character varying(2000),
  x_dnp character(1),
  x_create_date timestamp without time zone,
  x_data_source_id bigint NOT NULL,
  x_grid_node_id integer NOT NULL DEFAULT 0::integer,
  CONSTRAINT omop_oscar_result_pk PRIMARY KEY (oscar_result_id)
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.oscar_result
  OWNER TO rosita;

-- Table: omop.payer_plan_period

DROP TABLE IF EXISTS omop.payer_plan_period;

CREATE TABLE omop.payer_plan_period
(
  payer_plan_period_id bigint NOT NULL DEFAULT nextval('omop.omop_payer_plan_sq'),
  x_data_source_type character varying(20) NOT NULL,
  person_id bigint NOT NULL,
  payer_plan_period_start_date date NOT NULL,
  payer_plan_period_end_date date NOT NULL,
  payer_source_value character varying(50),
  plan_source_value character varying(50),
  family_source_value character varying(50),
  x_grid_node_id bigint NOT NULL,
  x_data_source_id bigint not null
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.payer_plan_period
  OWNER TO rosita;

-- Table: omop.person

DROP TABLE IF EXISTS omop.person;

CREATE TABLE omop.person
(
  person_id bigint NOT NULL,
  gender_concept_id bigint NOT NULL,
  year_of_birth numeric(4,0) NOT NULL,
  month_of_birth numeric(2,0),
  day_of_birth numeric(2,0),
  race_concept_id bigint NOT NULL,
  ethnicity_concept_id bigint NOT NULL,
  location_id bigint NOT NULL,
  provider_id bigint,
  care_site_id bigint,
  person_source_value character varying(50),
  gender_source_value character varying(50),
  race_source_value character varying(50),
  ethnicity_source_value character varying(50),
  x_organization_id bigint NOT NULL,
  x_grid_node_id bigint NOT NULL,
  x_data_source_id bigint not null,
  CONSTRAINT omop_person_pk PRIMARY KEY (person_id)
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.person
  OWNER TO rosita;

-- Index: omop.omop_person_source_idx

DROP INDEX IF EXISTS omop.omop_person_source_idx;

CREATE INDEX omop_person_source_idx
  ON omop.person
  USING btree
  (x_data_source_id, person_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;


-- Table: omop.procedure_cost

DROP TABLE IF EXISTS omop.procedure_cost;

CREATE TABLE omop.procedure_cost
(
  procedure_cost_id bigint NOT NULL DEFAULT nextval('omop.omop_proc_cost_sq'),
  procedure_occurrence_id bigint NOT NULL,
  x_data_source_type character varying(20) NOT NULL,
  paid_copay numeric(8,2),
  paid_coinsurance numeric(8,2),
  paid_toward_deductible numeric(8,2),
  paid_by_payer numeric(8,2),
  paid_by_coordination_benefits numeric(8,2),
  total_out_of_pocket numeric(8,2),
  total_paid numeric(8,2),
  disease_class_concept_id bigint,
  revenue_code_concept_id bigint,
  payer_plan_period_id bigint,
  disease_class_source_value character varying(50),
  revenue_code_source_value character varying(50),
  x_grid_node_id bigint NOT NULL,
  x_data_source_id bigint not null
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.procedure_cost
  OWNER TO rosita;

-- Table: omop.procedure_occurrence

DROP TABLE IF EXISTS omop.procedure_occurrence;

CREATE TABLE omop.procedure_occurrence
(
  procedure_occurrence_id bigint NOT NULL DEFAULT nextval('omop.omop_proc_occur_sq'),
  person_id bigint NOT NULL,
  procedure_concept_id bigint NOT NULL,
  procedure_date date NOT NULL,
  procedure_type_concept_id bigint NOT NULL,
  associated_provider_id bigint,
  visit_occurrence_id bigint,
  relevant_condition_concept_id bigint,
  procedure_source_value character varying(50),
  x_data_source_type character varying(20) NOT NULL,
  x_grid_node_id bigint NOT NULL,
  x_data_source_id bigint not null
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.procedure_occurrence
  OWNER TO rosita;

-- Table: omop.provider

DROP TABLE IF EXISTS omop.provider;

CREATE TABLE omop.provider
(
  provider_id bigint NOT NULL DEFAULT nextval('omop.omop_provider_sq'),
  npi character varying(50),
  dea character varying(50),
  specialty_concept_id bigint,
  care_site_id bigint,
  provider_source_value character varying(50) NOT NULL,
  specialty_source_value character varying(50),
  x_data_source_type character varying(20) NOT NULL,
  x_provider_first character varying(75),
  x_provider_middle character varying(75),
  x_provider_last character varying(75),
  x_organization_id bigint NOT NULL,
  x_grid_node_id bigint NOT NULL,
  x_data_source_id bigint not null
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.provider
  OWNER TO rosita;

-- Index: omop.omop_provider_source_idx

DROP INDEX IF EXISTS omop.omop_provider_source_idx;

CREATE INDEX omop_provider_source_idx
  ON omop.provider
  USING btree
  (x_data_source_id, provider_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;


-- Table: omop.visit_occurrence

DROP TABLE IF EXISTS omop.visit_occurrence;

CREATE TABLE omop.visit_occurrence
(
  visit_occurrence_id bigint NOT NULL,
  person_id bigint NOT NULL,
  visit_start_date date NOT NULL,
  visit_end_date date NOT NULL,
  place_of_service_concept_id bigint NOT NULL,
  care_site_id bigint,
  place_of_service_source_value character varying(50),
  x_visit_occurrence_source_identifier character varying(50) NOT NULL,
  x_data_source_type character varying(20) NOT NULL,
  x_provider_id bigint,
  x_grid_node_id bigint NOT NULL,
  x_data_source_id bigint not null
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.visit_occurrence
  OWNER TO rosita;

-- Index: omop.omop_visit_occurrence_source_idx

DROP INDEX IF EXISTS omop.omop_visit_occurrence_source_idx;

CREATE INDEX omop_visit_occurrence_source_idx
  ON omop.visit_occurrence
  USING btree
  (x_data_source_id, x_visit_occurrence_source_identifier COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Table: omop.x_cohort_metadata

DROP TABLE IF EXISTS omop.x_cohort_metadata CASCADE;

CREATE TABLE omop.x_cohort_metadata
(
  cohort_metadata_id integer NOT NULL DEFAULT nextval('omop.omop_x_cohort_metadata_sq'),
  cohort_name character varying(50) NOT NULL,
  cohort_description character varying(200),
  cohort_creator_name character varying(50),
  cohort_creator_contact character varying(200),
  is_cohort_shared boolean,
  cohort_query text,
  service_url character varying(100),
  original_cohort_count integer,
  last_cohort_count integer,
  original_cohort_date date,
  last_cohort_date date,
  cohort_phi_notes text,
  cohort_other_notes text,
  cohort_expiration_date date,
  grid_node_id integer NOT NULL,
  CONSTRAINT omop_x_cohort_metadata_pk1 PRIMARY KEY (cohort_metadata_id, grid_node_id)
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.x_cohort_metadata
  OWNER TO rosita;

-- Table: omop.x_measure

DROP TABLE IF EXISTS omop.x_measure;

CREATE TABLE omop.x_measure
(
  measure_id bigint NOT NULL,
  measure_start_date timestamp without time zone,
  measure_name character varying(255),
  provider_id integer,
  eligible_pt_ids text,
  included_pt_ids text,
  excluded_pt_ids text,
  measure_code_id bigint,
  grid_node_id integer NOT NULL
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.x_measure
  OWNER TO rosita;

-- Table: omop.x_measure_code

DROP TABLE IF EXISTS omop.x_measure_code;

CREATE TABLE omop.x_measure_code
(
  measure_code_id bigint NOT NULL,
  measure_code_desc text,
  measure_code_start_date timestamp without time zone,
  measure_code_end_date timestamp without time zone,
  grid_node_id integer NOT NULL
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE omop.x_measure_code
  OWNER TO rosita;
