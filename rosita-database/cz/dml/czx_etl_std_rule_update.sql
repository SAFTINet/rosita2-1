-- Function: cz.czx_etl_std_rule_update(bigint)

-- DROP FUNCTION cz.czx_etl_std_rule_update(bigint);

CREATE OR REPLACE FUNCTION cz.czx_etl_std_rule_update(p_step_id bigint)
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
	
	v_rule record;
	v_map record;
	v_table record;
	v_table2 record;
	v_primary record;
	v_value_count integer;
	v_primary_values text[];
	v_insert text;
	v_select text;
	v_from text;
	v_from2 text;
	v_where text;
	v_where2 text;
	v_order_by text;
	v_group_by2 text;
	v_join text;
	v_i integer;
	
	pos integer;
	v_distinct_select text;
	v_current_table text;
	v_current_field text;
	v_current_value text;
	v_distinct_from text;
	v_distinct_where text;
	v_distinct_field_list text;
begin

	v_database_name := 'cz';
	v_function_name := 'czx_etl_rule_update';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;		

	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;

	-- Use ETL rules to build insert and select statements (for rules which have not been previously updated)
	for v_rule in
		select *
		from cz.etl_rule
		where rule_type='STD'
		--where etl_rule_id='8485683' 
		--where (insert_statement is null or select_statement is null)
		order by rule_order
	loop
		raise notice 'v_rule_id: %',v_rule.etl_rule_id;

		v_insert := '';
		v_select := '';
		v_from := '';
		v_where := '';
		v_where2 := '';

		-- Create DISTINCT SELECT Statements
		v_distinct_select := '';
		v_distinct_from := '';
		v_distinct_where := '';
		v_distinct_field_list := '';
		v_group_by2 := '';
		
		for v_table in
			select
				a.*
			from
				cz.etl_table a
			where
				a.etl_rule_id = v_rule.etl_rule_id
			order by
				a.table_order
		loop
			-- Create unique field list
			v_current_table = '@' || v_table.table_order;
			v_distinct_field_list := ',';

			--Have to check PRIMARY, JOIN and WHERE to add to unique field list
			
			for v_table2 in
				select
					a.*
				from
					cz.etl_table a
				where
					a.etl_rule_id = v_rule.etl_rule_id
				order by
					a.table_order
			loop
				v_current_value := lower(v_table2.source_value);
				
				pos := 0;
				while (pos<char_length(v_current_value)-char_length(v_current_table||'.')) loop
					v_current_field := '';
					if (substring(v_current_value from pos for char_length(v_current_table||'.')) = v_current_table||'.') then
						pos := pos + char_length(v_current_table||'.');
						while (substring(v_current_value from pos for 1)~'^[a-zA-Z0-9_]+$') AND pos<=char_length(v_current_value) loop
							v_current_field := v_current_field||substring(v_current_value from pos for 1);
							pos := pos + 1;
							--raise notice 'current_field: %',v_current_field;
						end loop;
						
					end if;
					
					if strpos(v_distinct_field_list, ','||v_current_field||',')=0 then
						v_distinct_field_list := v_distinct_field_list||v_current_field||',';
						raise notice 'current_field_list_table: %',v_distinct_field_list;
					end if;
					
					pos := pos + 1;
				end loop;
			end loop;

			for v_map in
			select
				a.*
			from
				cz.etl_map a
			where
				a.etl_rule_id = v_rule.etl_rule_id
			order by
				a.map_order
			loop
				v_current_value := lower(v_map.source_value);

				pos := 0;
				while (pos<char_length(v_current_value)-char_length(v_current_table||'.')) loop
					v_current_field := '';
					if (substring(v_current_value from pos for char_length(v_current_table||'.')) = v_current_table||'.') then
						pos := pos + char_length(v_current_table||'.');
						while (substring(v_current_value from pos for 1)~'^[a-zA-Z0-9_]+$') AND pos<=char_length(v_current_value) loop
							v_current_field := v_current_field||substring(v_current_value from pos for 1);
							pos := pos + 1;
						end loop;
						
					end if;
					
					if strpos(v_distinct_field_list, ','||v_current_field||',')=0 then
						v_distinct_field_list := v_distinct_field_list||v_current_field||',';
						raise notice 'current_field_list_map: %',v_distinct_field_list;
					end if;
					
					pos := pos + 1;
				end loop;
 			end loop;

			if upper(v_rule.target_schema) = 'STD' and upper(v_table.source_schema) = 'RAW' then
					v_distinct_where := 'x_data_source_id = ' || v_rule.x_data_source_id;
			end if;
			


			-- Add x_data_source_id, etl_date and record_num
			if strpos(v_distinct_field_list,',x_data_source_id,')=0 then
				v_distinct_field_list = v_distinct_field_list||'x_data_source_id,';
			end if;

			if upper(v_table.table_type) = 'PRIMARY' then
				if strpos(v_distinct_field_list,',x_etl_date,')=0 then
					v_distinct_field_list = v_distinct_field_list||'x_etl_date,';
				end if;
				if strpos(v_distinct_field_list,',x_record_num,')=0 then
					v_distinct_field_list = v_distinct_field_list||'x_record_num,';
				end if;
			end if;
			
			v_distinct_select := '(select '||trim(both ',' from v_distinct_field_list)||' from '||v_table.source_schema||'.'||v_table.source_table||' where '||v_distinct_where||')';

			if upper(v_table.table_type) = 'PRIMARY' then
				v_primary := v_table;
				v_primary_values := string_to_array(v_table.source_value, ',');

				-- Add x_data_source_id criteria for this rule to where clause if this rule is moving data
				-- from the RAW schema to STD.
				if upper(v_rule.target_schema) = 'STD' and upper(v_table.source_schema) = 'RAW' then
					v_where := '@' || v_primary.table_order || '.x_data_source_id = ' || v_rule.x_data_source_id;
				end if;
			end if;

			if upper(v_table.table_type) != 'WHERE' then
				if length(v_from) = 0 then
					v_from := 'from ' || v_distinct_select || ' @' || v_table.table_order;
				else
					v_from := v_from || ' ' || v_table.table_type || ' ' || v_distinct_select || ' @' || v_table.table_order ||
					' on ' || v_table.source_value;
					-- If this table is in std schema, then add criteria to select from same data source and etl date
					if v_table.source_schema = 'std' then
						v_from := v_from || 
							' and @' || v_primary.table_order || '.x_data_source_id = @' || v_table.table_order || '.x_data_source_id' ||
							' and @' || v_primary.table_order || '.x_etl_date = @' || v_table.table_order || '.x_etl_date';
					elseif v_table.source_schema = 'raw' then
						v_from := v_from || 
							' and @' || v_primary.table_order || '.x_data_source_id = @' || v_table.table_order || '.x_data_source_id';
					end if;
					
				end if;
			else
				if length(v_where) > 0 then
					v_where := v_where || ' and ';
				end if;
				v_where := v_where || v_table.source_value;			
			end if;
						
		end loop;

		v_value_count := 0;
		-- Use etl_map to add target columns to insert and select clauses
		for v_map in
			select
				a.*
			from
				cz.etl_map a
			where
				a.etl_rule_id = v_rule.etl_rule_id
			order by
				a.map_order
		loop
			if length(v_insert) = 0 then
				v_insert := 'insert into ' || v_rule.target_schema || '.' || v_rule.target_table || ' (';
			else
				v_insert := v_insert || ', ';
			end if;
			v_insert := v_insert || v_map.target_column;
			
			if length(v_select) = 0 then
				v_select = 'select distinct ';
			else
				v_select = v_select || ', ';
			end if;

			v_select := v_select || v_map.source_value;
			v_value_count := v_value_count + 1;

			raise notice 'Select :%',v_select;
		end loop;

		-- Add ETL control fields to statements if the target table is in the omop schema
		if v_rule.rule_type = 'STD' then
			-- Add ETL control fields to insert clause
			v_insert := v_insert || ', x_data_source_id, x_etl_date, x_record_num)';

			-- Add ETL control fields to select clause
			v_select := v_select || ', @' || v_primary.table_order || '.x_data_source_id, @' ||
				v_primary.table_order || '.x_etl_date, @' || v_primary.table_order || '.x_record_num';

			-- Add incremental data support to select most recent record for primary key and x_data_source_id
			-- Note that orginal from clause is used in sub select with aliases set to b<n>.
			
			
			if length(v_where) > 0 then
				v_where := ' where ' || v_where;
			end if;

			--if length(v_where) > 0 then
			--	v_where := ' and ' || v_where;
			--end if;

			for v_i in 1..array_length(v_primary_values, 1) loop
				if v_i > 1 then
					v_where2 := v_where2 || 'and ';
				end if;
				v_where2 := v_where2 || v_primary_values[v_i] || ' = ' || 'temp1.'||split_part(v_primary_values[v_i],'.',2) || ' ';
				v_group_by2 := v_group_by2 ||',' || v_primary_values[v_i] ;
			end loop;
			v_group_by2 := replace(v_group_by2,'@','b')||',b'||v_primary.table_order||'.x_data_source_id';

			if length(v_where2)>0 then
				v_where2 := replace(v_where2,'@','a')||' and ';
			end if;	

			v_join := 'join (select max(b' || v_primary.table_order || '.x_etl_date) max_date ' || v_group_by2 || ' ' ||
				replace(v_from, '@', 'b') || ' group by ' || trim(both ',' from v_group_by2) || ') temp1 '||
				'ON ' || v_where2 ||' '||
				'a'||v_primary.table_order||'.x_data_source_id = temp1.x_data_source_id AND temp1.max_date= a'||v_primary.table_order||'.x_etl_date';
		else
			v_insert := v_insert || ')';
			if length(v_where) > 0 then
				v_where := ' where ' || v_where;
			end if;
		end if;

		raise notice 'v_join = %', v_join;
		
		-- Build full select statement setting any replaining aliases to a<n>.
		v_select = replace(v_select || ' ' || v_from || ' ' || v_join || ' '|| v_where, '@', 'a');
		
		raise notice 'v_select = %', v_select;
		begin
			update
				cz.etl_rule
			set
				value_count = v_value_count,
				insert_statement = v_insert,
				select_statement = v_select
			where
				etl_rule_id = v_rule.etl_rule_id;
			get diagnostics v_row_count = row_count;
		exception 
			when others then
				perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, v_rule.rule_description || ': ' || sqlerrm);
				v_job_status := 'ERROR';
				return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, v_rule.rule_description || ' updated', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
	end loop;

	-- For custom rule

	raise notice 'start of custom rule';
	
	update 
		cz.etl_rule a
	set
		select_statement = b.select_statement
	from 
		cz.etl_custom_rules b
	where
		a.rule_description=b.rule_description AND a.x_data_source_id=b.x_data_source_id;
	
	return 0;
end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cz.czx_etl_std_rule_update(bigint)
  OWNER TO rosita;