-- Function: cz.czx_linkage_process(bigint, bigint)

-- DROP FUNCTION cz.czx_linkage_process(bigint, character varying);

CREATE OR REPLACE FUNCTION cz.czx_linkage_process(p_step_id bigint, p_linkage_cols character varying)
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
	
	v_linkage_cols		varchar(2000);
	v_linkage_cols_array varchar(100)[];
	v_linkage_cols_sel	varchar(2000)[];
	v_array_pos			int;
	v_sql				text;
	v_exists			int;
	v_match_sql			text;
	v_ct				int;

	
begin
	v_database_name := 'cz';
	v_function_name := 'czx_linkage_process';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;

	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;	
	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start '||v_function_name, v_row_count, v_step_ct, 'START');
	
	if p_linkage_cols is null then
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'No linkage columns specified', v_row_count, v_step_ct, 'ERROR');	
		return 16;
	end if;
		
	v_match_sql := 'insert into cz.linkage_result (person_source_value_a,x_data_source_id_a,person_source_value_b,x_data_source_id_b,confidence)';
	v_match_sql := v_match_sql || '	select linkage_source_a.person_source_value as person_source_value_a,linkage_source_a.x_data_source_id as x_data_source_id_a';
	v_match_sql := v_match_sql || ' ,linkage_source_b.person_source_value as person_source_value_b,linkage_source_b.x_data_source_id as x_data_source_id_b,100';
	v_match_sql := v_match_sql || ' from cz.linkage_source_a, cz.linkage_source_b where 1=1';
		
	v_linkage_cols := trim(both ',' from p_linkage_cols);
	v_linkage_cols_array := string_to_array(replace(v_linkage_cols,' ',''),',');

	--	validate linkage cols against table columns
	
	v_array_pos := 1;
	while v_array_pos <= array_length(v_linkage_cols_array,1)
	loop
		select count(*) into v_exists
		from information_schema.columns
		where table_schema = 'cz'
		  and table_name   = 'linkage_source_a'
		  and column_name = v_linkage_cols_array[v_array_pos];
		  
		if v_exists = 0 then
			perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Invalid linkage column: '||v_linkage_cols_array[v_array_pos] , v_row_count, v_step_ct, 'ERROR');	
			return 16;
		end if;
		
		v_linkage_cols_sel[v_array_pos] := ' and linkage_source_a.' || v_linkage_cols_array[v_array_pos] || '=linkage_source_b.' || v_linkage_cols_array[v_array_pos];
		--raise notice 'sel: %', v_linkage_cols_sel[v_array_pos];
		v_array_pos := v_array_pos + 1;
	end loop;
	
	--	truncate linkage result
	
	begin
	truncate table cz.linkage_result;
	get diagnostics v_row_count = row_count;
	end;
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate linkage_result', v_row_count, v_step_ct, 'SUCCESS');
	v_step_ct := v_step_ct + 1;	
	
	--	Link claims data (linkage_source_b) to clinical data (linkage_source_a)
	
	v_sql := v_match_sql;
	v_array_pos := 1;
	while v_array_pos <= array_length(v_linkage_cols_array,1)
	loop
		v_sql := v_sql || v_linkage_cols_sel[v_array_pos];
		v_array_pos := v_array_pos + 1;
	end loop;
	v_sql := v_sql || ' and not exists (select 1 from cz.linkage_result x where linkage_source_b.person_source_value=x.person_source_value_b and linkage_source_b.x_data_source_id=x.x_data_source_id_b)';
	-- raise notice 'link sql: %', v_sql;
	begin
	execute v_sql;
	get diagnostics v_row_count := row_count;
	exception 
	when others then
		perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
		v_job_status := 'ERROR';
		return 16;
	end;
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Insert linked claims records to linkage_results', v_row_count, v_step_ct, 'SUCCESS');
	v_step_ct := v_step_ct + 1;	
	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end '|| v_function_name, v_row_count, v_step_ct, 'COMPLETE');
	return 0;
	

end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cz.czx_linkage_process(bigint, character varying)
  OWNER TO rosita;