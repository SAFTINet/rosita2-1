echo off
rem 
rem  Copyright 2012-2013 The Regents of the University of Colorado
rem 
rem  Licensed under the Apache License, Version 2.0 (the "License")
rem  you may not use this file except in compliance with the License.
rem  You may obtain a copy of the License at
rem 
rem      http://www.apache.org/licenses/LICENSE-2.0
rem 
rem  Unless required by applicable law or agreed to in writing, software
rem  distributed under the License is distributed on an "AS IS" BASIS,
rem  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
rem  See the License for the specific language governing permissions and
rem  limitations under the License.
rem 
rem 
rem Batch file to create ROSITA database from scratch
rem
rem Note that the C:/Program Files/PostgreSQL/9.1/rosita_data and rosita_indx directories
rem must exist and have full access control configured for the postgres system user.
rem
set psql="C:\Program Files\PostgreSQL\9.2\bin\psql.exe"
%psql% --port=5432 --file=setup1_win.sql postgres
set PGDATABASE=
set PGUSER=
set PGPASSWORD=
%psql% --port=5432 --file=setup2.sql
rem	create tables, etc in schemas
%psql% --port=5432 --file=cz/ddl/cz_ddl.sql
%psql% --port=5432 --file=raw/ddl/raw_ddl.sql
%psql% --port=5432 --file=rz/ddl/rz_drop_constraints.sql
%psql% --port=5432 --file=rz/ddl/rz_ddl.sql
%psql% --port=5432 --file=rz/ddl/rz_add_constraints.sql
%psql% --port=5432 --file=std/ddl/std_ddl.sql
rem	load cz functions and data
%psql% --port=5432 --file=cz/dml/czx_concept_analyze.sql
%psql% --port=5432 --file=cz/dml/czx_etl_rule_update.sql
%psql% --port=5432 --file=cz/dml/czx_end_audit.sql
%psql% --port=5432 --file=cz/dml/czx_start_audit.sql
%psql% --port=5432 --file=cz/dml/czx_to_numeric.sql
%psql% --port=5432 --file=cz/dml/czx_to_varchar.sql
%psql% --port=5432 --file=cz/dml/czx_write_audit.sql
%psql% --port=5432 --file=cz/dml/czx_write_error.sql
%psql% --port=5432 --file=cz/dml/czx_array_sort.sql
%psql% --port=5432 --file=cz/dml/czx_percentile_cont.sql
%psql% --port=5432 --file=cz/dml/czx_oscar_analyze.sql
%psql% --port=5432 --file=cz/dml/czx_linkage_preprocess.sql
%psql% --port=5432 --file=cz/dml/czx_linkage_process.sql
%psql% --port=5432 --file=cz/dml/czx_linkage_postprocess.sql
%psql% --port=5432 --file=cz/dml/czx_linkage_adjustment.sql
%psql% --port=5432 --file=cz/dml/czx_table_index_maint.sql

%psql% --port=5432 --file=cz/data/cz_workflow_step_definitions.sql
%psql% --port=5432 --file=cz/data/cz_validation_error_handlers.sql

rem load data
%psql% --port=5432 --file=omop/ddl/omop_ddl.sql
%psql% --port=5432 --file=omop/dml/omx_load_all.sql
%psql% --port=5432 --file=omop/dml/omx_load_care_site.sql
%psql% --port=5432 --file=omop/dml/omx_load_condition_occurrence.sql
%psql% --port=5432 --file=omop/dml/omx_load_death.sql
%psql% --port=5432 --file=omop/dml/omx_load_drug_exposure.sql
%psql% --port=5432 --file=omop/dml/omx_load_location.sql
%psql% --port=5432 --file=omop/dml/omx_load_observation.sql
%psql% --port=5432 --file=omop/dml/omx_load_organization.sql
%psql% --port=5432 --file=omop/dml/omx_load_payer_plan_period.sql
%psql% --port=5432 --file=omop/dml/omx_load_person.sql
%psql% --port=5432 --file=omop/dml/omx_load_procedure_occurrence.sql
%psql% --port=5432 --file=omop/dml/omx_load_provider.sql
%psql% --port=5432 --file=omop/dml/omx_load_visit_occurrence.sql

%psql% --port=5432 --file=std/dml/stdx_load_std.sql

rem	Revision History
rem
rem	20130613	Added std schema, load of median.sql and czx_oscar_analyze.sql
