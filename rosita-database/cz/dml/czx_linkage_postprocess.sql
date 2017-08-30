-- Function: cz.czx_linkage_postprocess(bigint)

DROP FUNCTION IF EXISTS cz.czx_linkage_postprocess(bigint);

CREATE OR REPLACE FUNCTION cz.czx_linkage_postprocess(p_step_id bigint)
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
	v_database_name := 'cz';
	v_function_name := 'czx_linkage_postprocess';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;	

	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;	
	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start '||v_function_name, 0, v_step_ct, 'START');	
	
	--	insert linked claims records
	
	begin
	insert into cz.cz_id_map
	(id
	,x_data_source_id
	,source_value
	,id_type
	)
	select map.id
		  ,lr.x_data_source_id_b
		  ,lr.person_source_value_b
		  ,'person_id'
	from cz.linkage_result lr
		,cz.cz_id_map map
	where lr.person_source_value_a = map.source_value
	  and lr.x_data_source_id_a = map.x_data_source_id
	  and map.id_type = 'person_id'
	  and not exists
		  (select 1 from cz.cz_id_map x
		   where lr.person_source_value_b = x.source_value
		     and lr.x_data_source_id_b = x.x_data_source_id
			 and x.id_type = 'person_id');
	get diagnostics v_row_count := row_count;
	exception 
	when others then
		perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
		v_job_status := 'ERROR';
		return 16;
	end;
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Insert claims records to cz.cz_id_map', v_row_count, v_step_ct, 'SUCCESS');
	v_step_ct := v_step_ct + 1;
	
	--	log results to linkage_log
	
	begin
	insert into cz.linkage_log
	(person_source_value
	,x_data_source_id
	,x_etl_date
	,x_record_num
	,log_type
	,log_message
	)
	select b.person_source_value
		  ,b.x_data_source_id
		  ,b.x_etl_date
		  ,b.x_record_num
		  ,case when x.person_source_value_a is not null then 'SUCCESS' else 'ERROR' end as log_type
		  ,case when x.person_source_value_a is not null then 'Claims record linked' else 'Unlinked claims record' end as log_message
	from cz.linkage_source_b b
	left outer join cz.linkage_result x
		 on  b.person_source_value = x.person_source_value_b
		 and b.x_data_source_id = x.x_data_source_id_b;	 
	get diagnostics v_row_count := row_count;
	exception 
	when others then
		perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
		v_job_status := 'ERROR';
		return 16;
	end;
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert claims records to linkage_log', v_row_count, v_step_ct, 'SUCCESS');
	v_step_ct := v_step_ct + 1;	
	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end '|| v_function_name, v_row_count, v_step_ct, 'COMPLETE');
	return 0;
	
end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cz.czx_linkage_postprocess(bigint)
  OWNER TO rosita;
