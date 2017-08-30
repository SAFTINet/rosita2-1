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


-- Sequence: cz.seq_au_pk_id

DROP SEQUENCE IF EXISTS cz.seq_au_pk_id;

CREATE SEQUENCE cz.seq_au_pk_id
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE cz.seq_au_pk_id
  OWNER TO rosita;
  
DROP SEQUENCE IF EXISTS public.hibernate_sequence;

CREATE SEQUENCE public.hibernate_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER SEQUENCE public.hibernate_sequence
  OWNER TO rosita;

-- Table: cz.au_permission

DROP TABLE IF EXISTS cz.au_permission CASCADE;

CREATE TABLE cz.au_permission
(
  id bigint NOT NULL,
  description character varying(500),
  name character varying(50) NOT NULL,
  unique_id character varying(255) NOT NULL,
  CONSTRAINT au_permission_pk PRIMARY KEY (id )
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.au_permission
  OWNER TO rosita;

-- Table: cz.au_principal

DROP TABLE IF EXISTS cz.au_principal CASCADE;

CREATE TABLE cz.au_principal
(
  principal_id bigint NOT NULL,
  category character varying(255) NOT NULL,
  date_created timestamp without time zone NOT NULL,
  last_updated timestamp without time zone NOT NULL,
  name character varying(255) NOT NULL,
  security_identifier character varying(255),
  username character varying(255),
  class character varying(255) NOT NULL,
  provider_code character varying(255),
  CONSTRAINT au_principal_pk PRIMARY KEY (principal_id )
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.au_principal
  OWNER TO rosita;

-- Table: cz.au_person

DROP TABLE IF EXISTS cz.au_person CASCADE;

CREATE TABLE cz.au_person
(
  person_id bigint NOT NULL,
  email character varying(255) NOT NULL,
  email_show boolean NOT NULL,
  enabled boolean NOT NULL,
  password character varying(255) NOT NULL,
  principal_id bigint,
  username character varying(255) NOT NULL,
  CONSTRAINT au_person_pk PRIMARY KEY (person_id ),
  CONSTRAINT au_person_principal_fk FOREIGN KEY (principal_id)
      REFERENCES cz.au_principal (principal_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT au_person_username_uk UNIQUE (username )
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.au_person
  OWNER TO rosita;

-- Table: cz.au_role_descr

DROP TABLE IF EXISTS cz.au_role_descr CASCADE;

CREATE TABLE cz.au_role_descr
(
  role_descr_id bigint NOT NULL,
  authority character varying(255) NOT NULL,
  description character varying(255),
  display_name character varying(255) NOT NULL,
  security_level integer NOT NULL,
  CONSTRAINT au_role_descr_pk PRIMARY KEY (role_descr_id ),
  CONSTRAINT au_role_descr_authority_uk UNIQUE (authority )
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.au_role_descr
  OWNER TO rosita;

-- Table: cz.au_person_role

DROP TABLE IF EXISTS cz.au_person_role;

CREATE TABLE cz.au_person_role
(
  person_id bigint NOT NULL,
  authority_id bigint NOT NULL,
  CONSTRAINT au_person_role_pk PRIMARY KEY (authority_id , person_id ),
  CONSTRAINT au_person_role_descr_fk FOREIGN KEY (authority_id)
      REFERENCES cz.au_role_descr (role_descr_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT au_person_role_person_fk FOREIGN KEY (person_id)
      REFERENCES cz.au_person (person_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.au_person_role
  OWNER TO rosita;

-- Table: cz.au_request_map

DROP TABLE IF EXISTS cz.au_request_map;

CREATE TABLE cz.au_request_map
(
  request_map_id bigint NOT NULL,
  version bigint NOT NULL,
  config_attribute character varying(255) NOT NULL,
  url character varying(255) NOT NULL,
  CONSTRAINT au_request_map_pk PRIMARY KEY (request_map_id ),
  CONSTRAINT au_request_map_url_uk UNIQUE (url )
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.au_request_map
  OWNER TO rosita;

-- Table: cz.au_user_group_member

DROP TABLE IF EXISTS cz.au_user_group_member;

CREATE TABLE cz.au_user_group_member
(
  au_group_id bigint,
  au_user_id bigint,
  CONSTRAINT au_user_group_member_principal_user_fk FOREIGN KEY (au_user_id)
      REFERENCES cz.au_principal (principal_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT au_user_group_member_principal_group_fk FOREIGN KEY (au_group_id)
      REFERENCES cz.au_principal (principal_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.au_user_group_member
  OWNER TO rosita;


-- Table: cz.cz_wrk_id_map

DROP TABLE IF EXISTS cz.cz_wrk_id_map;

CREATE TABLE cz.cz_wrk_id_map
(
 source_value character varying(200),
 x_data_source_id bigint,
 mapped_id bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.cz_wrk_id_map
  OWNER TO rosita;

-- Sequence: cz.cz_sq

DROP SEQUENCE IF EXISTS cz.cz_sq CASCADE;

CREATE SEQUENCE cz.cz_sq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE cz.cz_sq
  OWNER TO rosita;
  
-- Table: cz.cz_concept_map

DROP TABLE IF EXISTS cz.cz_concept_map CASCADE;

CREATE TABLE cz.cz_concept_map
(
  concept_map_id bigint NOT NULL DEFAULT nextval('cz.cz_sq'::regclass),
  target_table character varying(50),
  target_column character varying(50),
  source_value character varying(200),
  source_vocabulary character varying(50),
  source_desc character varying(200),
  target_concept_id bigint,
  is_mapped character(1),
  is_empty character(1),
  source_count bigint,
  x_data_source_id  bigint,
  CONSTRAINT cz_concept_map_id_pk PRIMARY KEY (concept_map_id )
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.cz_concept_map
  OWNER TO rosita;

-- Index: cz.cz_concept_map_tab_col_val_idx

 DROP INDEX IF EXISTS cz.cz_concept_map_tab_col_val_idx;

CREATE INDEX cz_concept_map_tab_col_val_idx
  ON cz.cz_concept_map
  USING btree
  (x_data_source_id , target_table COLLATE pg_catalog."default" , target_column COLLATE pg_catalog."default" , source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: cz.cz_concept_map_tab_col_val_voc_idx

 DROP INDEX IF EXISTS cz.cz_concept_map_tab_col_val_voc_idx;

CREATE INDEX cz_concept_map_tab_col_val_voc_idx
  ON cz.cz_concept_map
  USING btree
  (x_data_source_id , target_table COLLATE pg_catalog."default" , target_column COLLATE pg_catalog."default" , source_value COLLATE pg_catalog."default" , source_vocabulary COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;


-- Sequence: cz.cz_id_map_sq

DROP SEQUENCE IF EXISTS cz.cz_id_map_sq CASCADE;

CREATE SEQUENCE cz.cz_id_map_sq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE cz.cz_id_map_sq
  OWNER TO rosita;

  -- Table: cz.cz_id_map

DROP TABLE IF EXISTS cz.cz_id_map CASCADE;
  
CREATE TABLE cz.cz_id_map
(
  id bigint NOT NULL DEFAULT nextval('cz.cz_id_map_sq'::regclass),
  x_data_source_id 	bigint NOT NULL,
  source_value character varying(200) NOT NULL,
  id_type character varying(20) NOT NULL,
  CONSTRAINT cz_id_map_pk PRIMARY KEY (x_data_source_id , source_value , id_type )
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.cz_id_map
  OWNER TO rosita;

-- Index: cz.cz_id_map_id_idx

DROP INDEX IF EXISTS cz.cz_id_map_id_idx;

CREATE INDEX cz_id_map_id_idx
  ON cz.cz_id_map
  USING btree
  (id )
TABLESPACE rosita_indx;

-- Index: cz.cz_id_map_src_val_idx

 DROP INDEX IF EXISTS cz.cz_id_map_src_val_idx;

CREATE INDEX cz_id_map_src_val_idx
  ON cz.cz_id_map
  USING btree
  (x_data_source_id , source_value COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;

-- Index: cz.cz_id_map_srcv_typ_idx

-- DROP INDEX IF EXISTS cz.cz_id_map_srcv_typ_idx;

CREATE INDEX cz_id_map_srcv_typ_idx
  ON cz.cz_id_map
  USING btree
  (x_data_source_id , source_value COLLATE pg_catalog."default" , id_type COLLATE pg_catalog."default" )
TABLESPACE rosita_indx;


-- Table: cz.oscar_rule

DROP TABLE IF EXISTS cz.oscar_rule;

CREATE TABLE cz.oscar_rule
(
  x_oscar_rule_id bigint NOT NULL DEFAULT nextval('cz.cz_sq'::regclass),
  x_source_schema_name character varying(50) NOT NULL,
  source_table_name character varying(50) NOT NULL,
  variable_name character varying(50) NOT NULL,
  variable_value character varying(2000),
  x_statistic_name character varying(50),
  statistic_value character varying(2000),
  variable_type character varying(2),
  variable_description_level_1 character varying(2000),
  variable_value_level_1 character varying(2000),
  variable_description_level_2 character varying(2000),
  variable_value_level_2 character varying(2000),
  variable_description_level_3 character varying(2000),
  variable_value_level_3 character varying(2000),
  variable_description_level_4 character varying(2000),
  variable_value_level_4 character varying(2000),
  custom_query text,
  x_dnp character(1),
  CONSTRAINT cz_oscar_rule_pk PRIMARY KEY (x_oscar_rule_id)
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.oscar_rule
  OWNER TO rosita;

-- Table: cz.cz_random_id

DROP TABLE IF EXISTS cz.cz_random_id;

CREATE TABLE cz.cz_random_id
(
  id integer NOT NULL,
  id_type character varying(20) NOT NULL,
  random_id integer NOT NULL,
  CONSTRAINT cz_random_id_pk PRIMARY KEY (id, id_type)
  USING INDEX TABLESPACE rosita_indx,
  CONSTRAINT cz_random_id_uk UNIQUE (id_type, random_id)
  USING INDEX TABLESPACE rosita_indx
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.cz_random_id
  OWNER TO rosita;

-- Sequence: cz.cz_data_source_sq

DROP SEQUENCE IF EXISTS cz.cz_data_source_sq CASCADE;

CREATE SEQUENCE cz.cz_data_source_sq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER SEQUENCE cz.cz_data_source_sq
  OWNER TO rosita;
  
-- Table: cz.cz_data_source
 DROP TABLE IF EXISTS cz.cz_data_source CASCADE;
   
CREATE TABLE cz.cz_data_source
(
  x_data_source_id bigint NOT NULL DEFAULT nextval('cz.cz_data_source_sq'::regclass),
  data_source_directory character varying(200) NOT NULL,
  data_source_name character varying(200) NOT NULL,
  file_type character varying(255),
  active boolean DEFAULT true,
  incremental boolean,
  first_row_type character varying(20),
  delimiter character varying(5),
  quote_character character varying(1),
  schema_path character varying(200),
  etl_rules_file character varying(200),
  data_source_type character varying(20),
  linkage_type character varying(20),
  linkage_field character varying(200),
  CONSTRAINT cz_data_source_pkey PRIMARY KEY (x_data_source_id)
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.cz_data_source
  OWNER TO rosita;


  
-- Table: cz.cz_clinic_status

DROP TABLE IF EXISTS cz.cz_clinic_status CASCADE;

CREATE TABLE cz.cz_clinic_status
(
  clinic_status_id bigint NOT NULL DEFAULT nextval('cz.cz_sq'::regclass),
  job_id bigint NOT NULL,
  workflow_step_id bigint NOT NULL,
  x_data_source_id  bigint,
  success boolean NOT NULL,
  CONSTRAINT cz_clinic_status_pk PRIMARY KEY (clinic_status_id),
  CONSTRAINT cz_clinic_status_data_source_fk FOREIGN KEY (x_data_source_id )
      REFERENCES cz.cz_data_source (x_data_source_id ) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.cz_clinic_status
  OWNER TO rosita;



-- Table: cz.etl_rule

DROP TABLE IF EXISTS cz.etl_rule;

CREATE TABLE cz.etl_rule
(
  etl_rule_id bigint NOT NULL DEFAULT nextval('cz.cz_sq'::regclass),
  rule_type character varying(50),
  rule_order integer,
  rule_description character varying(2000),
  target_schema character varying(50),
  target_table character varying(50),
  value_count integer,
  insert_statement text,
  select_statement text,
  x_data_source_id bigint
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cz.etl_rule
  OWNER TO rosita;
  
-- Table: cz.etl_map

DROP TABLE IF EXISTS cz.etl_map;

CREATE TABLE cz.etl_map
(
  etl_map_id bigint NOT NULL DEFAULT nextval('cz.cz_sq'::regclass),
  etl_rule_id bigint NOT NULL,
  map_order integer,
  target_column character varying(50),
  source_value character varying(2000)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cz.etl_map
  OWNER TO rosita;

-- Table: cz.etl_table

DROP TABLE IF EXISTS cz.etl_table;

CREATE TABLE cz.etl_table
(
  etl_table_id bigint NOT NULL DEFAULT nextval('cz.cz_sq'::regclass),
  etl_rule_id bigint NOT NULL,
  table_type character varying(50),
  table_order integer,
  source_schema character varying(50),
  source_table character varying(50),
  source_value character varying(2000)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cz.etl_table
  OWNER TO rosita;

-- View: cz.cz_all_table_columns

DROP VIEW IF EXISTS cz.cz_all_table_columns;

CREATE OR REPLACE VIEW cz.cz_all_table_columns AS 
 SELECT c.nspname AS schema_name, b.relname AS table_name, a.attname AS column_name, a.attnum AS column_index, d.typname AS type_name, 
        CASE d.typname
            WHEN 'numeric'::name THEN 
            CASE
                WHEN a.atttypmod = (-1) THEN NULL::integer
                ELSE ((a.atttypmod - 4) >> 16) & 65535
            END
            WHEN 'varchar'::name THEN 
            CASE
                WHEN a.atttypmod = (-1) THEN NULL::integer
                ELSE a.atttypmod - 4
            END
            WHEN 'int2'::name THEN 16
            WHEN 'int4'::name THEN 32
            WHEN 'int8'::name THEN 64
            WHEN 'float4'::name THEN 24
            WHEN 'float8'::name THEN 53
            ELSE NULL::integer
        END AS column_length, 
        CASE
            WHEN a.atttypid = ANY (ARRAY[21::oid, 23::oid, 20::oid]) THEN 0
            WHEN a.atttypid = 1700::oid THEN 
            CASE
                WHEN a.atttypmod = (-1) THEN NULL::integer
                ELSE (a.atttypmod - 4) & 65535
            END
            ELSE NULL::integer
        END AS columnscale, 
        CASE a.attnotnull
            WHEN true THEN 'NOT NULL'::text
            ELSE NULL::text
        END AS not_null
   FROM pg_attribute a
   JOIN pg_class b ON a.attrelid = b.oid
   JOIN pg_namespace c ON b.relnamespace = c.oid
   JOIN pg_type d ON a.atttypid = d.oid
  WHERE a.attnum > 0;

ALTER TABLE cz.cz_all_table_columns
  OWNER TO rosita;
  
DROP TABLE IF EXISTS cz.step; 
  
CREATE TABLE cz.step
(
  step_id bigint NOT NULL DEFAULT nextval('cz.cz_sq'::regclass) CONSTRAINT step_pkey PRIMARY KEY,
  job_id bigint,
  step_description_id bigint,
  state varchar(50),
  start_date timestamp without time zone,
  end_date timestamp without time zone
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.step
  OWNER TO rosita;
 
DROP TABLE IF EXISTS cz.step_description; 
  
CREATE TABLE cz.step_description
(
  step_description_id bigint NOT NULL DEFAULT nextval('cz.cz_sq'::regclass)  CONSTRAINT step_description_pkey PRIMARY KEY,
  step_number bigint,
  handler character varying(255),
  initial_state character varying(20),
  next_step_description_id bigint,
  description varchar(200)
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.step_description 
  OWNER TO rosita;

 
DROP TABLE IF EXISTS cz.job CASCADE; 
  
CREATE TABLE cz.job
(
  job_id bigint NOT NULL DEFAULT nextval('cz.cz_sq'::regclass) CONSTRAINT job_pkey PRIMARY KEY,
  job_name varchar(200),
  file_name varchar(200),
  start_date timestamp without time zone,
  end_date timestamp without time zone
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.job
  OWNER TO rosita;

DROP TABLE IF EXISTS cz.job_property; 

CREATE TABLE cz.job_property
(
   job_property_id bigint NOT NULL DEFAULT nextval('cz.cz_sq'::regclass), 
   job_id bigint, 
   key character varying(50), 
   value character varying(500), 
   PRIMARY KEY (job_property_id) USING INDEX TABLESPACE rosita_indx, 
   FOREIGN KEY (job_id) REFERENCES cz.job (job_id) ON UPDATE CASCADE ON DELETE CASCADE
) 
WITH (
  OIDS = FALSE
)

TABLESPACE rosita_data;
ALTER TABLE cz.job_property
  OWNER TO rosita;

DROP TABLE IF EXISTS cz.log; 
  
CREATE TABLE cz.log
(
  log_id bigint NOT NULL DEFAULT nextval('cz.cz_sq'::regclass) CONSTRAINT log_pkey PRIMARY KEY,
  job_id bigint,
  step_id bigint,
  message_type varchar(50),
  message text,
  error_code varchar(50),
  schema_name varchar(50),
  table_name varchar(50),
  function_name varchar(50),
  subtask_num bigint,
  records_manipulated bigint,
  file_name varchar(200),
  record_num bigint,
  log_date timestamp without time zone,
  time_elapsed_secs numeric(18,4),
  x_data_source_id bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.log
  OWNER TO rosita;
  
-- Index: cz.cz_log_step_id_message_type_idx

DROP INDEX IF EXISTS cz.cz_log_step_id_message_type_idx;

CREATE INDEX cz_log_step_id_message_type_idx
  ON cz.log
  USING btree
  (step_id, message_type COLLATE pg_catalog."default")
TABLESPACE rosita_indx;

DROP TABLE IF EXISTS cz.signal; 
  
CREATE TABLE cz.signal
(
  signal_id bigint DEFAULT nextval('cz.cz_sq'::regclass) CONSTRAINT signal_pkey PRIMARY KEY,
  step_id bigint,
  message text,
  pending boolean,
  success boolean,
  signal_date timestamp without time zone
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.signal
  OWNER TO rosita;
  -- tables already removed and no longer used
  DROP TABLE IF EXISTS cz.cz_console_output;
  DROP TABLE IF EXISTS cz.cz_etl_error_log;
  DROP TABLE IF EXISTS cz.cz_job_audit;
  DROP TABLE IF EXISTS cz.cz_job_master;
  DROP TABLE IF EXISTS cz.cz_rosita_job cascade;
  DROP TABLE IF EXISTS cz.cz_workflow_signal;
  DROP TABLE IF EXISTS cz.cz_workflow_step;
  DROP TABLE IF EXISTS cz.cz_workflow_title;
  DROP TABLE IF EXISTS cz.cz_xml_error_log;

DROP TABLE IF EXISTS cz.validation_error_handler;

CREATE TABLE cz.validation_error_handler
(
  name character varying(50),
  allow boolean,
  parameters character varying(200),
  validation_error_handler_id bigint NOT NULL DEFAULT nextval('cz.cz_sq'::regclass),
  description character varying(200),
  allow_message character varying(200),
  CONSTRAINT validation_error_handler_pkey PRIMARY KEY (validation_error_handler_id)
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.validation_error_handler
  OWNER TO rosita;
  
  -- Table: cz.linkage_source_a

DROP TABLE IF EXISTS cz.linkage_source_a CASCADE;
  
CREATE TABLE cz.linkage_source_a
(person_source_value		varchar(50) not null
,x_data_source_type			varchar(20) not null
,medicaid_id_number			varchar(50)
,ssn						varchar(50)
,last						varchar(75)
,middle						varchar(75)
,first						varchar(75)
,address_1					varchar(50)
,address_2					varchar(50)
,city						varchar(50)
,state						varchar(50)
,zip						varchar(9)
,county						varchar(20)
,year_of_birth				numeric(4,0)
,month_of_birth				numeric(2,0)
,day_of_birth				numeric(2,0)
,x_data_source_id			bigint
,x_etl_date					timestamp without time zone
,x_record_num				bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.linkage_source_a
  OWNER TO rosita;
  
  -- Table: cz.linkage_source_b

DROP TABLE IF EXISTS cz.linkage_source_b CASCADE;
  
CREATE TABLE cz.linkage_source_b
(person_source_value		varchar(50) not null
,x_data_source_type			varchar(20) not null
,medicaid_id_number			varchar(50)
,ssn						varchar(50)
,last						varchar(75)
,middle						varchar(75)
,first						varchar(75)
,address_1					varchar(50)
,address_2					varchar(50)
,city						varchar(50)
,state						varchar(50)
,zip						varchar(9)
,county						varchar(20)
,year_of_birth				numeric(4,0)
,month_of_birth				numeric(2,0)
,day_of_birth				numeric(2,0)
,x_data_source_id			bigint
,x_etl_date					timestamp without time zone
,x_record_num				bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.linkage_source_b
  OWNER TO rosita;
  
  -- Table: cz.linkage_log

DROP TABLE IF EXISTS cz.linkage_log CASCADE;
  
CREATE TABLE cz.linkage_log
(
	 linkage_id bigint NOT NULL DEFAULT nextval('cz.cz_sq'::regclass)
	,person_source_value		varchar(50) not null
	,x_data_source_id			bigint
	,x_etl_date					timestamp without time zone
	,x_record_num				bigint
	,log_type					varchar(50)
	,log_message				text
	,CONSTRAINT linkage_log_pk PRIMARY KEY (linkage_id)
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.linkage_log
  OWNER TO rosita;
  
  
DROP TABLE IF EXISTS cz.linkage_result CASCADE;
  
CREATE TABLE cz.linkage_result
( 	
	 linkage_id bigint NOT NULL DEFAULT nextval('cz.cz_sq'::regclass)
	,person_source_value_a		varchar(50) not null
	,x_data_source_id_a			bigint
	,person_source_value_b		varchar(50) not null
	,x_data_source_id_b			bigint
	,confidence					int
	,CONSTRAINT linkage_result_pk PRIMARY KEY (linkage_id)
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.linkage_result
  OWNER TO rosita;
  
create unique index linkage_result_idx on cz.linkage_result (person_source_value_b, x_data_source_id_b);

drop table if exists cz.linkage_adjustment cascade;

CREATE TABLE cz.linkage_adjustment
(person_source_value		varchar(50) not null
,x_data_source_id			bigint		not null
,link_source_value			varchar(50)
,link_x_data_source_id		bigint
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE cz.linkage_adjustment
  OWNER TO rosita;
  

DROP TABLE IF EXISTS cz.table_index;

CREATE TABLE cz.table_index
(
  schema_name text,
  table_name text,
  index_name text,
  index_sql text
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cz.table_index
  OWNER TO rosita;

DROP TABLE IF EXISTS cz.etl_custom_rules;

CREATE TABLE cz.etl_custom_rules
(
  select_statement character varying,
  rule_description character varying NOT NULL,
  x_data_source_id bigint NOT NULL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cz.etl_custom_rules
  OWNER TO rosita;

