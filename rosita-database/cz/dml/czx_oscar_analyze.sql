-- Function: cz.czx_oscar_analyze(bigint, character varying, character varying)

DROP FUNCTION IF EXISTS cz.czx_oscar_analyze(bigint, character varying, character varying);

CREATE OR REPLACE FUNCTION cz.czx_oscar_analyze(p_step_id bigint, p_schema_name character varying, p_table_name_prefix character varying)
  RETURNS void AS
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
	v_ds record;
	v_x_data_source_id bigint;
	v_rule record;
	v_insert text;
	v_values_1 text;
	v_values_2 text;
	v_values_3 text;
	v_values_4 text;
	v_select text;
	v_select_variables text;
	v_query text;
	v_query_variables text;
	v_group_by_variables text;
	v_sql text;

begin
	v_database_name := 'cz';
	v_function_name := 'czx_oscar_analyze';
	v_step_id := p_step_id;
	v_job_status := 'RUNNING';
	v_step_ct := 1;		

	-- Start auditing
	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;

	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start omx_oscar_analyze', v_row_count, v_step_ct, 'START');

	-- Purge prior statistics for specified schema and table name prefix
	delete from
		omop.oscar_result
	where
		x_source_schema_name = p_schema_name
		and source_table_name like p_table_name_prefix || '%';

	get diagnostics v_row_count = row_count;
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'delete cz_oscar_result', v_row_count, v_step_ct, 'SUCCESS');
	v_step_ct := v_step_ct + 1;

	-- Define insert and profiling statements
	v_insert := 'insert into omop.oscar_result (x_oscar_rule_id, x_source_schema_name, source_table_name, variable_name, x_statistic_name,' ||
		' variable_description_level_1, variable_description_level_2,' ||
		' variable_description_level_3, variable_description_level_4,' ||
		' x_dnp, x_create_date, x_data_source_id,' ||
		' variable_type, variable_value, statistic_type, statistic_value,' ||
		' variable_value_level_1, variable_value_level_2,' ||
		' variable_value_level_3, variable_value_level_4)';

	v_values_1 := '1, null, unnest(array[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]),' ||
		' unnest(array[st1::varchar, st2::varchar, st3::varchar, st4::varchar, st5::varchar, st6::varchar, st7::varchar, st8::varchar, st9::varchar, st10::varchar, st11::varchar]),' ||
		' variable_value_level_1::varchar, variable_value_level_2::varchar, variable_value_level_3::varchar, variable_value_level_4::varchar' ||
		' from (select count(statistic_value) st1, avg(statistic_value) st2,' ||
		' stddev_samp(statistic_value) st3, min(statistic_value) st4,' ||
		' cz.czx_percentile_cont(array_agg(statistic_value), .25) st5, ' ||
		' cz.czx_percentile_cont(array_agg(statistic_value), .50) st6, ' ||
		' cz.czx_percentile_cont(array_agg(statistic_value), .75) st7, ' ||
		' max(statistic_value) st8,' ||
		' sum(case when statistic_value is null then 1 else 0 end) st9,' ||
		' sum(case when statistic_value::varchar = '''' then 1 else 0 end) st10,' ||
		' count(distinct statistic_value) st11, :variable_values' ||
		' from (:query) query :group_by) query2';

	v_values_2 := '2, variable_value::varchar, 1, statistic_value::varchar,' ||
		' variable_value_level_1::varchar, variable_value_level_2::varchar, variable_value_level_3::varchar, variable_value_level_4::varchar' ||
		' from (select variable_value, count(statistic_value) statistic_value, :variable_values' ||
		' from (:query) query group by variable_value :group_by) query2';

	v_values_3 := '3, null, unnest(array[1, 4, 8, 9, 10, 11]),' ||
		' unnest(array[st1::varchar, st4::varchar, st8::varchar, st9::varchar, st10::varchar, st11::varchar]),' ||
		' variable_value_level_1::varchar, variable_value_level_2::varchar, variable_value_level_3::varchar, variable_value_level_4::varchar' ||
		' from (select count(statistic_value) st1, min(statistic_value) st4, max(statistic_value) st8,' ||
		' sum(case when statistic_value is null then 1 else 0 end) st9,' ||
		' sum(case when statistic_value::varchar = '''' then 1 else 0 end) st10,' ||
		' count(distinct statistic_value) st11, :variable_values' ||
		' from (:query) query :group_by) query2';

	v_values_4 := '4, null, unnest(array[1, 9, 10, 11]),' ||
		' unnest(array[st1::varchar, st9::varchar, st10::varchar, st11::varchar]),' ||
		' variable_value_level_1::varchar, variable_value_level_2::varchar, variable_value_level_3::varchar, variable_value_level_4::varchar' ||
		' from (select count(statistic_value) st1,' ||
		' sum(case when statistic_value is null then 1 else 0 end) st9,' ||
		' sum(case when statistic_value::varchar = '''' then 1 else 0 end) st10,' ||
		' count(distinct statistic_value) st11, :variable_values' ||
		' from (:query) query :group_by) query2';

	-- Outer for loop will iterate through all the x_data_source_id's and finally -1 for all	
	for v_ds in
		--	if x_data_source_id = -1 and row number = 2 then there are only 2 records in the set, a real x_data_source_id and the -1 (default).  If this
		--	is the case, the -1 record is dropped so only one record is in the set and the x_data_source_id is forced to -1
		select case when y.ds_ct = 1 then -1 else y.x_data_source_id end as x_data_source_id
		from (select x.x_data_source_id, z.ds_ct, row_number() over (order by x.x_data_source_id desc) as rnum
			  from (select x_data_source_id
					from cz.cz_data_source
					where active
					union
					select -1 as x_data_source_id
					order by x_data_source_id desc) x 
			  cross join (select count(distinct x_data_source_id) as ds_ct from cz.cz_data_source where active) z) y
		where not (y.x_data_source_id = -1 and y.rnum = 2)
/*      original would select real x_data_source_id if only one
		select y.x_data_source_id
		from (select x.x_data_source_id, row_number() over (order by x.x_data_source_id desc) as rnum
			  from (select x_data_source_id
					from cz.cz_data_source
					where active
					union
					select -1 as x_data_source_id
					order by x_data_source_id desc) x 
			  ) y
		where not (y.x_data_source_id = -1 and y.rnum = 2);
*/
		
	loop
		v_x_data_source_id := v_ds.x_data_source_id;

		-- Iterate through rules for specifed schema and table name prefix
		for v_rule in
			select
				*
			from
				cz.oscar_rule
			where
				x_source_schema_name = p_schema_name
				and source_table_name like p_table_name_prefix || '%'
			order by
				source_table_name, variable_name
		loop
			begin
				-- Build base select statement
				v_select := 'select ' || v_rule.x_oscar_rule_id || ', ''' || v_rule.x_source_schema_name || ''',' ||
					' ''' || v_rule.source_table_name || ''', ''' || v_rule.variable_name || ''',' ||
					' ' || coalesce('''' || v_rule.x_statistic_name || ''',', 'null,') ||
					' ' || coalesce('''' || v_rule.variable_description_level_1 || ''',', 'null,') ||
					' ' || coalesce('''' || v_rule.variable_description_level_2 || ''',', 'null,') ||
					' ' || coalesce('''' || v_rule.variable_description_level_3 || '''', 'null,') ||
					' ' || coalesce('''' || v_rule.variable_description_level_4 || '''', 'null,') ||
					' ' || coalesce('''' || v_rule.x_dnp || ''',', 'null,') ||
					' current_timestamp, ' || v_x_data_source_id || ',';
					
				v_select_variables = '';
				v_query_variables = '';
				v_group_by_variables := '';

				-- Build variable lists for statistics select, data profile select, and group by clause
				-- based on whether or not variable_value_level_<n> values are defined
				if v_rule.variable_value_level_1 is not null then
					v_select_variables := 'variable_value_level_1 variable_value_level_1';
					v_query_variables := v_rule.variable_value_level_1 || ' variable_value_level_1';
					v_group_by_variables := 'variable_value_level_1';
				else
					v_select_variables := 'null variable_value_level_1';
				end if;
				if v_rule.variable_value_level_2 is not null then
					v_select_variables := v_select_variables || ', variable_value_level_2 variable_value_level_2';
					v_query_variables := v_query_variables || ', ' || v_rule.variable_value_level_2 || ' variable_value_level_2';
					v_group_by_variables := v_group_by_variables || ', variable_value_level_2';
				else
					v_select_variables := v_select_variables || ', null variable_value_level_2';
				end if;
				if v_rule.variable_value_level_3 is not null then
					v_select_variables := v_select_variables || ', variable_value_level_3 variable_value_level_3';
					v_query_variables := v_query_variables || ', ' || v_rule.variable_value_level_3 || ' variable_value_level_3';
					v_group_by_variables := v_group_by_variables || ', variable_value_level_3';
				else
					v_select_variables := v_select_variables || ', null variable_value_level_3';
				end if;
				if v_rule.variable_value_level_4 is not null then
					v_select_variables := v_select_variables || ', variable_value_level_4 variable_value_level_4';
					v_query_variables := v_query_variables || ', ' || v_rule.variable_value_level_4 || ' variable_value_level_4';
					v_group_by_variables := v_group_by_variables || ', variable_value_level_4';
				else
					v_select_variables := v_select_variables || ', null variable_value_level_4';
				end if;

				-- Use custom query, if defined; otherwise dynamically build data profile query
				if v_rule.custom_query is not null then
					v_query := v_rule.custom_query;
					-- Add x_data_source_id to query's where clause if the custom query uses an alias of "a" for its primary table
					if v_x_data_source_id != -1 then
						if strpos(lower(v_query), ' a ') = 0 then
							perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Unable to gather statistics statistics for x_oscar_rule_id = ' || v_rule.x_oscar_rule_id::varchar || ' and x_data_source_id = ' || v_x_data_source_id::varchar || ' because its custom_query cannot be associated with an x_data_soruce_id.', v_row_count, v_step_ct, 'WARNING');
							continue;
						end if;
						if strpos(lower(v_query), ' where ') = 0 then
							v_query := v_query || ' where ';
						else
							v_query := v_query || ' and ';
						end if;
						v_query := v_query || 'a.' || 'x_data_source_id = ' || v_x_data_source_id;
					end if;
				else
					v_query := 'select ' || coalesce(v_rule.variable_value, 'null') || ' variable_value, ';
					
					if v_rule.statistic_value is not null then
						v_query := v_query || v_rule.statistic_value || ' statistic_value';
					else
						if v_rule.variable_type = '2' then
							v_query := v_query || '1 statistic_value';
						else
							v_query := v_query || 'null statistic_value';
						end if;
					end if;
					
					if length(v_query_variables) > 0 then
						v_query := v_query || ', ' || v_query_variables;
					end if;
					
					v_query := v_query || ' from ' || v_rule.x_source_schema_name || '.' || v_rule.source_table_name;

					if v_x_data_source_id != -1 then
						v_query := v_query || ' where ' || v_rule.source_table_name || '.' || 'x_data_source_id = ' || v_x_data_source_id;
					end if;
				end if;

				-- Build insert and select statements based on variable_type
				case v_rule.variable_type
				when '1' then
					v_sql := v_insert || ' ' || v_select || ' ' || v_values_1;
				when '2' then
					v_sql := v_insert || ' ' || v_select || ' ' || v_values_2;
				when '3' then
					v_sql := v_insert || ' ' || v_select || ' ' || v_values_3;
				when '4' then
					v_sql := v_insert || ' ' || v_select || ' ' || v_values_4;
				else
					raise exception 'unexpected variable type % for oscar rule ID %', v_rule.variable_type, v_rule.x_oscar_rule_id;
				end case;
				-- Replace :group_by placeholder with group by variables
				if length(v_group_by_variables) > 0 then
					if v_rule.variable_type = '2' then
						v_sql := replace(v_sql, ':group_by', ', ' || v_group_by_variables);
					else
						v_sql := replace(v_sql, ':group_by', 'group by ' || v_group_by_variables);
					end if;
				else
					v_sql := replace(v_sql, ':group_by', '');
				end if;

				-- Replace :variable_values and :query placeholders with variable names and data profile query
				v_sql := replace(v_sql, ':variable_values', v_select_variables);
				v_sql := replace(v_sql, ':query', v_query);

--				raise notice 'SQL statement: %', v_sql;
				execute v_sql;
				
				get diagnostics v_row_count = row_count;
				perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_oscar_result, x_oscar_rule_id=' || v_rule.x_oscar_rule_id::varchar, v_row_count, v_step_ct, 'SUCCESS');
				v_step_ct := v_step_ct + 1;

				v_job_status := 'SUCCESS';
			exception
				when others then
				raise notice 'SQL statement: %', v_sql;
				perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
				v_job_status := 'ERROR';
			end;
		end loop;    
	end loop;
	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end omx_oscar_analyze', v_row_count, v_step_ct, 'COMPLETE');

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cz.czx_oscar_analyze(bigint, character varying, character varying)
  OWNER TO rosita;
