-- Function: std.stdx_truncate_std(bigint)

-- DROP FUNCTION std.stdx_truncate_std(bigint);

CREATE OR REPLACE FUNCTION std.stdx_truncate_std(p_step_id bigint)
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

begin
	v_database_name := 'std';
	v_function_name := 'stdx_truncate_std';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;		

	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;

	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Starting load', 0, v_step_ct, 'START');
	
	-- truncate std tables
	for v_table in
		select
			table_schema || '.' || table_name as table_name
		from
			information_schema.tables
		where
			table_schema = 'std' and table_type = 'BASE TABLE'
		order by
			table_name
	loop
		v_sql := 'truncate table ' || v_table.table_name || ' cascade';
		execute v_sql;
		get diagnostics v_row_count = row_count;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate table ' || v_table.table_name, v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
	end loop;	 
	
	return 0;
end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION std.stdx_truncate_std(bigint)
  OWNER TO rosita;