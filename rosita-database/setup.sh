#!/bin/bash
#
# Copyright 2012-2013 The Regents of the University of Colorado
#
# Licensed under the Apache License, Version 2.0 (the "License")
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
#export PGUSER=
#export PGPASSWORD=
#mkdir /var/lib/pgsql/9.1/rosita_data
#mkdir /var/lib/pgsql/9.1/rosita_indx
#psql --file=setup1_linux.sql 
export PGDATABASE=
export PGUSER=
export PGPASSWORD=
psql --file=setup2.sql
# CZ
psql --file=cz/ddl/cz_ddl.sql
psql --file=cz/dml/czx_concept_analyze.sql
psql --file=cz/dml/czx_end_audit.sql
psql --file=cz/dml/czx_etl_rule_update.sql
psql --file=cz/dml/czx_etl_std_rule_update.sql
psql --file=cz/dml/czx_oscar_analyze.sql
psql --file=cz/dml/czx_start_audit.sql
psql --file=cz/dml/czx_write_audit.sql
psql --file=cz/dml/czx_write_error.sql
psql --file=cz/dml/czx_array_sort.sql
psql --file=cz/dml/czx_percentile_cont.sql
psql --file=cz/dml/czx_linkage_preprocess.sql
psql --file=cz/dml/czx_linkage_process.sql
psql --file=cz/dml/czx_linkage_postprocess.sql
psql --file=cz/dml/czx_linkage_adjustment.sql
psql --file=cz/dml/czx_table_index_maint.sql
psql --file=cz/data/cz_validation_error_handlers.sql
psql --file=cz/data/cz_workflow_step_definitions.sql
# RAW
psql --file=raw/ddl/raw_ddl.sql
# STD
psql --file=std/ddl/std_ddl.sql
psql --file=std/dml/stdx_load_std.sql
psql --file=std/dml/stdx_load_std_by_rule.sql
psql --file=std/dml/stdx_truncate_std.sql
# RZ
psql --file=rz/ddl/rz_drop_constraints.sql
psql --file=rz/ddl/rz_ddl.sql
psql --file=rz/ddl/rz_add_constraints.sql
# OMOP
psql --file=omop/ddl/omop_ddl.sql
psql --file=omop/dml/omx_load_all.sql
psql --file=omop/dml/omx_load_care_site.sql
psql --file=omop/dml/omx_load_condition_occurrence.sql
psql --file=omop/dml/omx_load_death.sql
psql --file=omop/dml/omx_load_drug_cost.sql
psql --file=omop/dml/omx_load_drug_exposure.sql
psql --file=omop/dml/omx_load_location.sql
psql --file=omop/dml/omx_load_observation.sql
psql --file=omop/dml/omx_load_organization.sql
psql --file=omop/dml/omx_load_payer_plan_period.sql
psql --file=omop/dml/omx_load_person.sql
psql --file=omop/dml/omx_load_procedure_cost.sql
psql --file=omop/dml/omx_load_procedure_occurrence.sql
psql --file=omop/dml/omx_load_provider.sql
psql --file=omop/dml/omx_load_visit_occurrence.sql
# POSTGRES
psql --file=postgres/remove_jasperserver.sql
createdb -T template0 jasperserver
psql jasperserver < omop/jaserserver.out
