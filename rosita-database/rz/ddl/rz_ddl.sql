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

-- Table: rz.vocabulary

DROP TABLE IF EXISTS rz.vocabulary cascade;

CREATE TABLE rz.vocabulary
(
  vocabulary_id integer NOT NULL,
  vocabulary_name character varying(256) NOT NULL
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE rz.vocabulary
  OWNER TO rosita;

-- Table: rz.concept

DROP TABLE IF EXISTS rz.concept cascade;

CREATE TABLE rz.concept
(
  concept_id integer NOT NULL,
  concept_name character varying(256) NOT NULL,
  concept_level integer NOT NULL,
  concept_class character varying(60) NOT NULL,
  vocabulary_id integer NOT NULL,
  concept_code character varying(20) NOT NULL,
  valid_start_date date NOT NULL,
  valid_end_date date NOT NULL DEFAULT '2099-12-31'::date,
  invalid_reason character(1)
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE rz.concept
  OWNER TO rosita;

-- Table: rz.concept_ancestor

DROP TABLE IF EXISTS rz.concept_ancestor;

CREATE TABLE rz.concept_ancestor
(
  ancestor_concept_id integer NOT NULL,
  descendant_concept_id integer NOT NULL,
  min_levels_of_separation integer,
  max_levels_of_separation integer
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE rz.concept_ancestor
  OWNER TO rosita;

-- Table: rz.concept_relationship

DROP TABLE IF EXISTS rz.concept_relationship;

CREATE TABLE rz.concept_relationship
(
  concept_id_1 integer NOT NULL,
  concept_id_2 integer NOT NULL,
  relationship_id integer NOT NULL,
  valid_start_date date NOT NULL,
  valid_end_date date NOT NULL DEFAULT '2099-12-31'::date,
  invalid_reason character(1)
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE rz.concept_relationship
  OWNER TO rosita;

-- Table: rz.concept_synonym

DROP TABLE IF EXISTS rz.concept_synonym;

CREATE TABLE rz.concept_synonym
(
  concept_synonym_id integer NOT NULL,
  concept_id integer NOT NULL,
  concept_synonym_name character varying(1000) NOT NULL
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE rz.concept_synonym
  OWNER TO rosita;

-- Table: rz.relationship

DROP TABLE IF EXISTS rz.relationship;

CREATE TABLE rz.relationship
(
  relationship_id integer NOT NULL,
  relationship_name character varying(256) NOT NULL,
  is_hierarchical character(1),
  defines_ancestry character(1),
  reverse_relationship integer
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE rz.relationship
  OWNER TO rosita;

-- Table: rz.source_to_concept_map

DROP TABLE IF EXISTS rz.source_to_concept_map;

CREATE TABLE rz.source_to_concept_map
(
  source_code character varying(200) NOT NULL,
  source_vocabulary_id integer NOT NULL,
  source_code_description character varying(256),
  target_concept_id integer NOT NULL,
  target_vocabulary_id integer NOT NULL,
  mapping_type character varying(20),
  primary_map character(1),
  valid_start_date date NOT NULL,
  valid_end_date date NOT NULL DEFAULT '2099-12-31'::date,
  invalid_reason character(1),
  -- Added column
  map_source character varying(20),
  -- Added column
  x_data_source_id bigint NOT NULL DEFAULT -1
)
WITH (
  OIDS=FALSE
)
TABLESPACE rosita_data;
ALTER TABLE rz.source_to_concept_map
  OWNER TO rosita;

