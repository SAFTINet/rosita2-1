-- Function: omop.omx_load_condition_occurrence(bigint)

DROP FUNCTION IF EXISTS cz.czx_table_index_maint(bigint, character varying, character varying);

CREATE OR REPLACE FUNCTION cz.czx_table_index_maint(
	p_step_id bigint
	,p_run_type	character varying
	,p_schema character varying
) RETURNS bigint AS
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
	
	v_ct		int;
	v_index		record;
	v_sql		varchar(4000);
  
begin

	v_database_name := 'cz';
	v_function_name := 'czx_table_index_maint';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;

	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;	

	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start ' || v_function_name || ' ' || p_run_type || ' ' || p_schema, 0, v_step_ct, 'START');
	v_step_ct := v_step_ct + 1;

	if p_run_type not in ('DROP','ADD','SAVE') then
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Invalid run_type: ' || p_run_type, 0, v_step_ct, 'ERROR');
		v_step_ct := v_step_ct + 1;
		return 16;
	end if;
	
	if p_schema is null or p_schema = '' then
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Schema name missing', 0, v_step_ct, 'ERROR');
		v_step_ct := v_step_ct + 1;
		return 16;
	end if;
	
	select count(*) into v_ct
	from cz.table_index
	where schema_name = p_schema;
	
	if v_ct = 0 then
		if p_run_type != 'SAVE' then
			perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name,'No saved indexes found for schema: ' || p_schema, 0, v_step_ct, 'WARNING');
			v_step_ct := v_step_ct + 1;
			return 8;
		end if;
	end if;
	
	--	SAVE indexes
	
	if p_run_type = 'SAVE' then
	
		--	check that indexes exist for supplied schema name
		
		select count(*) into v_ct
		from pg_class a
		inner join pg_index b
			  on  a.oid = b.indexrelid 
		inner join pg_class c
			  on  b.indrelid = c.oid
		inner join pg_attribute d
			  on  c.oid = d.attrelid 
			  and d.attnum = any(b.indkey)
		inner join pg_namespace n
			  on n.oid = a.relnamespace
		where a.relname not like 'pg_%'
		  and n.nspname = p_schema;
	
		if v_ct = 0 then
			perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name,'No indexes found for schema: ' || p_schema, 0, v_step_ct, 'WARNING');
			v_step_ct := v_step_ct + 1;
			return 8;
		end if;	
	
		--	delete existing indexes for schema tables from cz.table_maint
		
		begin
		delete from cz.table_index
		where schema_name = p_schema;
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Deleted existing indexes for ' || p_schema, v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
		
		--	insert indexes for schema tables into cz.table_index
		
		begin
		insert into cz.table_index
		(schema_name
		,table_name
		,index_name
		,index_sql
		)
		select distinct n.nspname as schema_name
			  ,c.relname as table_name
			  ,a.relname as index_name
			  ,pg_get_indexdef(b.indexrelid) as index_sql
		from pg_class a
		inner join pg_index b
			  on  a.oid = b.indexrelid 
		inner join pg_class c
			  on  b.indrelid = c.oid
		inner join pg_attribute d
			  on  c.oid = d.attrelid 
			  and d.attnum = any(b.indkey)
		inner join pg_namespace n
			  on n.oid = a.relnamespace
		where a.relname not like 'pg_%'
		  and n.nspname = p_schema
		order by schema_name, table_name, index_name;
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Saved indexes for ' || p_schema, v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
	else 
		--	drop indexes for DROP or ADD
		
		for v_index in (select * from cz.table_index where schema_name = p_schema)
		loop
			v_sql = 'drop index if exists ' || v_index.schema_name || '.' || v_index.index_name || ' cascade';
			begin
			execute v_sql;
			get diagnostics v_row_count = row_count;
			exception 
			when others then
				perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, v_sql || ': ' || sqlerrm);
				return 16;
			end;
			perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Dropped index ' || v_index.index_name || ' on ' || p_schema || '.' || v_index.table_name, 1, v_step_ct, 'SUCCESS');
			v_step_ct := v_step_ct + 1;
			
			if p_run_type = 'ADD' then
				--	add indexes for schema
				v_sql = v_index.index_sql;
				begin
				execute v_sql;
				get diagnostics v_row_count = row_count;
				perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Added index ' || v_index.index_name || ' on ' || p_schema || '.' || v_index.table_name, 1, v_step_ct, 'SUCCESS');
				exception 
				when others then
					perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'WARNING', sqlstate, v_index.index_sql || ': ' || sqlerrm);
					--return 16;
				end;
				v_step_ct := v_step_ct + 1;
			end if;
		end loop;
	end if;

	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end ' || v_function_name, 0, v_step_ct, 'COMPLETE');
	return 0;
	

end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cz.czx_table_index_maint(bigint, character varying, character varying)
  OWNER TO rosita;
 
 
