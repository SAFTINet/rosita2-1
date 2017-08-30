-- Function: std.stdx_load_std_by_rule(bigint, bigint)

-- DROP FUNCTION std.stdx_load_std_by_rule(bigint, bigint);

CREATE OR REPLACE FUNCTION std.stdx_load_std_by_rule(p_step_id bigint, p_etl_rule_id bigint)
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

	v_table record;
	v_rule record;
	v_sql text;

	lastTime timestamp;
	currTime timestamp;
	elapsedSecs numeric;

begin
	v_database_name := 'std';
	v_function_name := 'stdx_load_std_by_rule';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;		

	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;

	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Starting load', 0, v_step_ct, 'START');
	
	-- Use ETL rules to build insert and statements
	for v_rule in
		select *
		from cz.etl_rule
		where target_schema = 'std' AND etl_rule_id = p_etl_rule_id
	loop
		-- Build full sql insert statement
		v_sql = v_rule.insert_statement || ' ' || v_rule.select_statement;
		raise notice 'v_sql = %', v_sql;
		begin
			-- clock_timestamp() is the current system time
			select max(log_date) into lastTime from cz.log where step_id = p_step_id;
			perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, v_rule.rule_description || ': insert ' || v_rule.target_schema || '.' || v_rule.target_table, v_row_count, v_step_ct, 'STARTED');
			execute v_sql;
			get diagnostics v_row_count = row_count;
			select clock_timestamp() into currTime;
			elapsedSecs :=  coalesce(((DATE_PART('day', currTime - lastTime) * 24 + 
						DATE_PART('hour', currTime - lastTime)) * 60 +
			    DATE_PART('minute', currTime - lastTime)) * 60 +
			    DATE_PART('second', currTime - lastTime),0);
			perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, v_rule.rule_description || ': insert ' || v_rule.target_schema || '.' || v_rule.target_table  ||'  - Elapsed Time: ' ||elapsedSecs||'s', v_row_count, v_step_ct, 'DONE');

		exception 
			when others then
				perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, v_rule.rule_description || ': ' || sqlerrm);
				v_job_status := 'ERROR';
				return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, v_rule.rule_description || ': insert ' || v_rule.target_schema || '.' || v_rule.target_table, v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
	end loop;
	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'END', v_row_count, v_step_ct, 'COMPLETE');
	return 0;
end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION std.stdx_load_std_by_rule(bigint, bigint)
  OWNER TO rosita;