-- Function: omop.omx_load_all(bigint)

-- DROP FUNCTION omop.omx_load_all(bigint);

CREATE OR REPLACE FUNCTION omop.omx_load_all(p_step_id bigint)
  RETURNS bigint AS
$BODY$
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
declare
	v_database_name varchar(50);
	v_function_name varchar(50);
	v_step_id bigint;
	v_job_status varchar(50);
	v_step_ct int;
	v_row_count bigint;
begin
	v_database_name := 'omop';
	v_function_name := 'omx_load_all';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;		

	
	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;
	
	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start omx_load_all', v_row_count, v_step_ct, 'START');

	begin
		perform omop.omx_load_location(v_step_id);
		perform omop.omx_load_organization(v_step_id);
		perform omop.omx_load_care_site(v_step_id);
		perform omop.omx_load_provider(v_step_id);
		perform omop.omx_load_person(v_step_id);
		perform omop.omx_load_payer_plan_period(v_step_id);
		perform omop.omx_load_visit_occurrence(v_step_id);
		perform omop.omx_load_condition_occurrence(v_step_id);
		perform omop.omx_load_drug_exposure(v_step_id);
		perform omop.omx_load_drug_cost(v_step_id);
		perform omop.omx_load_procedure_occurrence(v_step_id);
		perform omop.omx_load_procedure_cost(v_step_id);
		perform omop.omx_load_observation(v_step_id);
		perform omop.omx_load_death(v_step_id);

	exception
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
	end;

	/*	
	if p_step_id is null or p_step_id < 1 then
		perform cz.czx_end_audit(v_step_id, v_job_status);
	end if;
	return 0;
	*/
	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end omx_load_all', v_row_count, v_step_ct, 'COMPLETE');
	return 0;
	
end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION omop.omx_load_all(bigint)
  OWNER TO rosita;