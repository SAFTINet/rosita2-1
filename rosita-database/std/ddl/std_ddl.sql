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

-- Table: std.care_site

DROP TABLE IF EXISTS std.care_site;

CREATE TABLE std.care_site
(
  care_site_source_value character varying(50) NOT NULL,
  x_data_source_type character varying(20) NOT NULL,
  organization_source_value character varying(50) NOT NULL,
  place_of_service_source_value character varying(50),
  x_care_site_name character varying(50),
  care_site_address_1 character varying(50),
  care_site_address_2 character varying(50),
  care_site_city character varying(50),
  care_site_state character varying(2),
  care_site_zip character varying(9),
  care_site_county character varying(20),
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE std.care_site
  OWNER TO rosita;

-- Table: std.cohort

DROP TABLE IF EXISTS std.cohort;

CREATE TABLE std.cohort
(
  cohort_source_identifier character varying(50) NOT NULL,
  x_data_source_type character varying(20) not null,
  cohort_source_value character varying(50) NOT NULL,
  cohort_start_date date NOT NULL,
  cohort_end_date date,
  subject_source_identifier character varying(50) NOT NULL,
  stop_reason character varying(20),
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE std.cohort
  OWNER TO rosita;

-- Table: std.condition_occurrence

DROP TABLE IF EXISTS std.condition_occurrence;

CREATE TABLE std.condition_occurrence
(
  condition_occurrence_source_identifier character varying(50) NOT NULL,
  x_data_source_type character varying(20) NOT NULL,
  person_source_value character varying(50) NOT NULL,
  condition_source_value character varying(50) NOT NULL,
  condition_source_value_vocabulary character varying(50) NOT NULL,
  x_condition_source_desc character varying(50),
  condition_start_date date NOT NULL,
  x_condition_update_date date,
  condition_end_date date,
  condition_type_source_value character varying(50) NOT NULL,
  stop_reason character varying(20),
  associated_provider_source_value character varying(50),
  x_visit_occurrence_source_identifier character varying(50),
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE std.condition_occurrence
  OWNER TO rosita;

-- Index: std.cond_occur_prov_src_idx 

DROP INDEX IF EXISTS std.cond_occur_prov_src_idx;

CREATE INDEX cond_occur_prov_src_idx
  ON std.condition_occurrence
  USING btree
  (x_data_source_id, associated_provider_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: std.cond_occur_person_src_idx

DROP INDEX IF EXISTS std.cond_occur_person_src_idx;

CREATE INDEX cond_occur_person_src_idx
  ON std.condition_occurrence
  USING btree
  (x_data_source_id, person_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: std.cond_occur_src_id_idx

DROP INDEX IF EXISTS std.cond_occur_src_id_idx;

CREATE INDEX cond_occur_src_id_idx
  ON std.condition_occurrence
  USING btree
  (x_data_source_id, condition_occurrence_source_identifier COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: std.cond_occur_visit_src_id_idx

DROP INDEX IF EXISTS std.cond_occur_visit_src_id_idx;

CREATE INDEX cond_occur_visit_src_id_idx
  ON std.condition_occurrence
  USING btree
  (x_data_source_id, x_visit_occurrence_source_identifier COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Table: std.death

DROP TABLE IF EXISTS std.death;

CREATE TABLE std.death
(
  person_source_value character varying(50) NOT NULL,
  x_data_source_type character varying(20) not null,
  death_date date,
  death_type_source_value character varying(50),
  cause_of_death_source_value character varying(50),
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE std.death
  OWNER TO rosita;

-- Table: std.drug_cost

DROP TABLE IF EXISTS std.drug_cost;

CREATE TABLE std.drug_cost
(
  drug_cost_source_identifier character varying(50) NOT NULL,
  x_data_source_type character varying(20) not null,
  drug_exposure_source_identifier character varying(50)NOT NULL,
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
  payer_plan_period_source_identifier character varying(50),
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE std.drug_cost
  OWNER TO rosita;

-- Table: std.drug_exposure

DROP TABLE IF EXISTS std.drug_exposure;

CREATE TABLE std.drug_exposure
(
  drug_exposure_source_identifier character varying(50) NOT NULL,
  x_data_source_type character varying(20) NOT NULL,
  person_source_value character varying(50) NOT NULL,
  drug_source_value character varying(50),
  drug_source_value_vocabulary character varying(50),
  drug_exposure_start_date date NOT NULL,
  drug_exposure_end_date date,
  drug_type_source_value character varying(50) NOT NULL,
  stop_reason character varying(20),
  refills numeric(4,0),
  quantity numeric(8,0),
  days_supply numeric(4,0),
  x_drug_name character varying(255) NOT NULL,
  x_drug_strength character varying(50),
  sig character varying(500),
  provider_source_value character varying(50),
  x_visit_occurrence_source_identifier character varying(50),
  relevant_condition_source_value character varying(50),
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE std.drug_exposure
  OWNER TO rosita;

-- Index: std.drug_exp_person_src_idx

DROP INDEX IF EXISTS std.drug_exp_person_src_idx;

CREATE INDEX drug_exp_person_src_idx
  ON std.drug_exposure
  USING btree
  (x_data_source_id, person_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: std.drug_exp_src_id_idx

DROP INDEX IF EXISTS std.drug_exp_src_id_idx;

CREATE INDEX drug_exp_src_id_idx
  ON std.drug_exposure
  USING btree
  (x_data_source_id, drug_exposure_source_identifier COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Table: std.observation

DROP TABLE IF EXISTS std.observation;

CREATE TABLE std.observation
(
  observation_source_identifier character varying(50) NOT NULL,
  x_data_source_type character varying(20) NOT NULL,
  person_source_value character varying(50) NOT NULL,
  observation_source_value character varying(50) NOT NULL,
  observation_source_value_vocabulary character varying(50) NOT NULL,
  observation_date date NOT NULL,
  observation_time time without time zone,
  value_as_number numeric(14,3),
  value_as_string character varying(60),
  unit_source_value character varying(50),
  range_low numeric(14,3),
  range_high numeric(14,3),
  observation_type_source_value character varying(50) NOT NULL,
  associated_provider_source_value character varying(50),
  x_visit_occurrence_source_identifier character varying(50),
  relevant_condition_source_value character varying(50),
  x_obs_comment character varying(500),
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE std.observation
  OWNER TO rosita;

-- Index: std.obs_person_src_idx

DROP INDEX IF EXISTS std.obs_person_src_idx;

CREATE INDEX obs_person_src_idx
  ON std.observation
  USING btree
  (x_data_source_id, person_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: std.obs_visit_src_id_idx

DROP INDEX IF EXISTS std.obs_visit_src_id_idx;

CREATE INDEX obs_visit_src_id_idx
  ON std.observation
  USING btree
  (x_data_source_id, x_visit_occurrence_source_identifier COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: std.obs_src_idx

DROP INDEX IF EXISTS std.obs_src_idx;

CREATE INDEX obs_src_idx
  ON std.observation
  USING btree
  (x_data_source_id, observation_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;
  
-- Table: std.organization

DROP TABLE IF EXISTS std.organization;

CREATE TABLE std.organization
(
  organization_source_value character varying(50) NOT NULL,
  x_data_source_type character varying(20) NOT NULL,
  place_of_service_source_value character varying(50) NOT NULL,
  organization_address_1 character varying(50),
  organization_address_2 character varying(50),
  organization_city character varying(50),
  organization_state character varying(2),
  organization_zip character varying(9),
  organization_county character varying(20),
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE std.organization
  OWNER TO rosita;

-- Table: std.payer_plan_period

DROP TABLE IF EXISTS std.payer_plan_period;

CREATE TABLE std.payer_plan_period
(
  payer_plan_period_source_identifier character varying(50) NOT NULL,
  x_data_source_type character varying(20) not null,
  person_source_value character varying(50) NOT NULL,
  payer_plan_period_start_date date NOT NULL,
  payer_plan_period_end_date date NOT NULL,
  payer_source_value character varying(50),
  plan_source_value character varying(50),
  family_source_value character varying(50),
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE std.payer_plan_period
  OWNER TO rosita;

-- Table: std.procedure_cost

DROP TABLE IF EXISTS std.procedure_cost;

CREATE TABLE std.procedure_cost
(
  procedure_cost_source_identifier character varying(50) NOT NULL,
  x_data_source_type character varying(20) not null,
  procedure_occurrence_source_identifier character varying(50) NOT NULL,
  paid_copay numeric(8,2),
  paid_coinsurance numeric(8,2),
  paid_toward_deductible numeric(8,2),
  paid_by_payer numeric(8,2),
  paid_by_coordination_benefits numeric(8,2),
  total_out_of_pocket numeric(8,2),
  total_paid numeric(8,2),
  disease_class_source_value character varying(50),
  disease_class_concept_id character varying(50),
  revenue_code_source_value character varying(50),
  revenue_code_concept_id character varying(50),
  payer_plan_period_source_identifier character varying(50),
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE std.procedure_cost
  OWNER TO rosita;

-- Table: std.procedure_occurrence

DROP TABLE IF EXISTS std.procedure_occurrence;

CREATE TABLE std.procedure_occurrence
(
  procedure_occurrence_source_identifier character varying(50) NOT NULL,
  x_data_source_type character varying(20) NOT NULL,
  person_source_value character varying(50) NOT NULL,
  procedure_source_value character varying(50) NOT NULL,
  procedure_source_value_vocabulary character varying(50) NOT NULL,
  procedure_date date NOT NULL,
  procedure_type_source_value character varying(50),
  provider_record_source_value character varying(50),
  x_visit_occurrence_source_identifier character varying(50),
  relevant_condition_source_value character varying(50),
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE std.procedure_occurrence
  OWNER TO rosita;

-- Index: std.proc_occur_src_id_idx

DROP INDEX IF EXISTS std.proc_occur_src_id_idx;

CREATE INDEX proc_occur_src_id_idx
  ON std.procedure_occurrence
  USING btree
  (x_data_source_id, procedure_occurrence_source_identifier COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: std.proc_occur_person_src_idx

DROP INDEX IF EXISTS std.proc_occur_person_src_idx;

CREATE INDEX proc_occur_person_src_idx
  ON std.procedure_occurrence
  USING btree
  (x_data_source_id, person_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: std.proc_occur_cond_src_idx

DROP INDEX IF EXISTS std.proc_occur_cond_src_idx;

CREATE INDEX proc_occur_cond_src_idx
  ON std.procedure_occurrence
  USING btree
  (x_data_source_id, relevant_condition_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: std.proc_occur_visit_src_id_idx

DROP INDEX IF EXISTS std.proc_occur_visit_src_id_idx;

CREATE INDEX proc_occur_visit_src_id_idx
  ON std.procedure_occurrence
  USING btree
  (x_data_source_id, x_visit_occurrence_source_identifier COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;
  
-- Table: std.provider

DROP TABLE IF EXISTS std.provider;

CREATE TABLE std.provider
(
  provider_source_value character varying(50) NOT NULL,
  x_data_source_type character varying(20) NOT NULL,
  npi character varying(50),
  dea character varying(50),
  specialty_source_value character varying(50),
  x_provider_first character varying(75),
  x_provider_middle character varying(75),
  x_provider_last character varying(75),
  care_site_source_value character varying(50),
  x_organization_source_value character varying(50) NOT NULL,
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE std.provider
  OWNER TO rosita;

-- Table: std.visit_occurrence

DROP TABLE IF EXISTS std.visit_occurrence;

CREATE TABLE std.visit_occurrence
(
  x_visit_occurrence_source_identifier character varying(50) NOT NULL,
  x_data_source_type character varying(20) NOT NULL,
  person_source_value character varying(50) NOT NULL,
  visit_start_date date NOT NULL,
  visit_end_date date NOT NULL,
  place_of_service_source_value character varying(50),
  x_provider_source_value character varying(50),
  care_site_source_value character varying(50),
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE std.visit_occurrence
  OWNER TO rosita;

-- Index: std.visit_occur_care_site_src_idx

DROP INDEX IF EXISTS std.visit_occur_care_site_src_idx;

CREATE INDEX visit_occur_care_site_src_idx
  ON std.visit_occurrence
  USING btree
  (x_data_source_id, care_site_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: std.visit_occur_place_srvc_src_idx

DROP INDEX IF EXISTS std.visit_occur_place_srvc_src_idx;

CREATE INDEX visit_occur_place_srvc_src_idx
  ON std.visit_occurrence
  USING btree
  (x_data_source_id, place_of_service_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: std.visit_occur_prov_src_idx

DROP INDEX IF EXISTS std.visit_occur_prov_src_idx;

CREATE INDEX visit_occur_prov_src_idx
  ON std.visit_occurrence
  USING btree
  (x_data_source_id, x_provider_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: std.visit_occur_person_src_idx

DROP INDEX IF EXISTS std.visit_occur_person_src_idx;

CREATE INDEX visit_occur_person_src_idx
  ON std.visit_occurrence
  USING btree
  (x_data_source_id, person_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Table: std.x_demographic

DROP TABLE IF EXISTS std.x_demographic;

CREATE TABLE std.x_demographic
(
  person_source_value character varying(50) NOT NULL,
  x_data_source_type character varying(20) NOT NULL,
  medicaid_id_number character varying(50),
  ssn character varying(50),
  last character varying(75),
  middle character varying(75),
  first character varying(75),
  address_1 character varying(50),
  address_2 character varying(50),
  city character varying(50),
  state character varying(2),
  zip character varying(9),
  county character varying(20),
  year_of_birth numeric(4,0) NOT NULL,
  month_of_birth numeric(2,0),
  day_of_birth numeric(2,0),
  gender_source_value character varying(50),
  race_source_value character varying(50),
  ethnicity_source_value character varying(50),
  provider_source_value character varying(50),
  care_site_source_value character varying(50),
  x_organization_source_value character varying(50),
  x_data_source_id	bigint,
  x_etl_date	timestamp without time zone,
  x_record_num	bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE std.x_demographic
  OWNER TO rosita;

-- Index: std.demo_city_state_zip_idx

DROP INDEX IF EXISTS std.demo_city_state_zip_idx;

CREATE INDEX demo_city_state_zip_idx
  ON std.x_demographic
  USING btree
  (x_data_source_id, city COLLATE pg_catalog."default" , state COLLATE pg_catalog."default" , zip COLLATE pg_catalog."default" , county COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: std.demo_care_site_src_idx

DROP INDEX IF EXISTS std.demo_care_site_src_idx;

CREATE INDEX demo_care_site_src_idx
  ON std.x_demographic
  USING btree
  (x_data_source_id, care_site_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: std.demo_id_idx

DROP INDEX IF EXISTS std.demo_id_idx;

--CREATE INDEX demo_id_idx
--  ON std.x_demographic
--  USING btree
--  (x_data_source_id, x_demographic COLLATE pg_catalog."default" )
--TABLESPACE rosita_indx;

-- Index: std.demo_org_src_idx

DROP INDEX IF EXISTS std.demo_org_src_idx;

CREATE INDEX demo_org_src_idx
  ON std.x_demographic
  USING btree
  (x_data_source_id, x_organization_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: std.demo_person_src_idx

DROP INDEX IF EXISTS std.demo_person_src_idx;

CREATE INDEX demo_person_src_idx
  ON std.x_demographic
  USING btree
  (x_data_source_id, person_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: std.demo_prov_src_idx

DROP INDEX IF EXISTS std.demo_prov_src_idx;

CREATE INDEX demo_prov_src_idx
  ON std.x_demographic
  USING btree
  (x_data_source_id, provider_source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;
