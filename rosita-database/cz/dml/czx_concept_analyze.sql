-- Function: cz.czx_concept_analyze(bigint)

-- DROP FUNCTION IF EXISTS cz.czx_concept_analyze(bigint);

CREATE OR REPLACE FUNCTION cz.czx_concept_analyze(
	p_step_id bigint
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
begin
	v_database_name := 'cz';
	v_function_name := 'czx_concept_analyze';
	v_step_id := p_step_id;
	v_job_status := 'RUNNING';
	v_step_ct := 1;

	
	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;
	
	--raise notice 'step_id: %', case(v_step_id as text);
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start czx_concept_analyze', v_row_count, v_step_ct, 'START');
	
		begin
		truncate table cz.cz_concept_map;
		get diagnostics v_row_count := row_count;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate cz_concept_map', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
		end;
	
		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)
		select
			'care_site' target_table,
			'place_of_service_concept_id' target_column,
			s.place_of_service_source_value source_value,
			null source_vocabulary,
			null source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.place_of_service_source_value is null or length(s.place_of_service_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.care_site s
			left outer join rz.source_to_concept_map t on s.place_of_service_source_value = t.source_code and
				t.mapping_type = 'Place of Service' and (t.invalid_reason is null or t.invalid_reason = '')
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.place_of_service_source_value = t2.source_code and
				t2.mapping_type = 'Place of Service' and (t2.invalid_reason is null or t2.invalid_reason = '')
				and t2.x_data_source_id  = s.x_data_source_id 
		group by
			s.place_of_service_source_value,
			coalesce(t2.target_concept_id,t.target_concept_id),
			coalesce(t2.x_data_source_id,t.x_data_source_id,s.x_data_source_id);

		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (care_site.place_of_service_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

	--	create temp table for descriptions
		
		drop table if exists cz.x_source_desc;
		
		begin
		create table cz.x_source_desc as
		select t.x_data_source_id
			  ,t.source_code
			  ,substring(t.source_code_description from 1 for 200) as source_code_desc
			  ,t.source_vocabulary_id
		from rz.source_to_concept_map t
		where t.mapping_type = 'CONDITION'
		  and (t.invalid_reason is null or t.invalid_reason = '') 
		  and (t.source_vocabulary_id in (1,2));
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (condition_occurrence.condition_concept_id - ICD9)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
		
		create index x_source_desc_idx1 on cz.x_source_desc (x_data_source_id, source_code, source_vocabulary_id);
		
		--	insert any new desc from raw data tables
		
		begin
		insert into cz.x_source_desc
		select distinct m1.x_data_source_id
			  ,m1.condition_source_value as source_code
			  ,m1.x_condition_source_desc as source_code_desc
			  ,case when m1.condition_source_value_vocabulary in ('ICD9','ICD-9') then 2 else 1 end as source_vocabulary_id
		from std.condition_occurrence m1
		where m1.x_record_num in 
			(select min(m2.x_record_num) from std.condition_occurrence m2
			 where m2.x_condition_source_desc is not null 
			   and m2.x_condition_source_desc != ''
			 group by m2.x_data_source_id, m2.condition_source_value
			)
		  and m1.condition_source_value_vocabulary in ('ICD9', 'ICD-9', 'SNOMED')
		  and not exists
			 (select 1 from cz.x_source_desc x 
			  where m1.condition_source_value = x.source_code
				and case when m1.condition_source_value_vocabulary in ('ICD9','ICD-9') then 2 else 1 end = x.source_vocabulary_id
				and (m1.x_data_source_id = x.x_data_source_id or x.x_data_source_id = -1));
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Insert new condition desc into cz.x_source_desc', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;				
		
		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)		
		select
			'condition_occurrence' target_table,
			'condition_concept_id' target_column,
			s.condition_source_value source_value,
			s.condition_source_value_vocabulary source_vocabulary,
			'' as source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.condition_source_value is null or length(s.condition_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.condition_occurrence s
			left outer join rz.source_to_concept_map t on s.condition_source_value = t.source_code
				and t.mapping_type = 'CONDITION' and (t.invalid_reason is null or t.invalid_reason = '') and t.source_vocabulary_id = 2
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.condition_source_value = t2.source_code
				and t2.mapping_type = 'CONDITION' and (t2.invalid_reason is null or t2.invalid_reason = '') and t2.source_vocabulary_id = 2
				and t2.x_data_source_id  = s.x_data_source_id 
		where
			s.condition_source_value_vocabulary in ('ICD9', 'ICD-9')
		group by
			s.condition_source_value,
			s.condition_source_value_vocabulary,
			coalesce(t2.target_concept_id,t.target_concept_id),
			coalesce(t2.x_data_source_id,t.x_data_source_id,s.x_data_source_id);
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (condition_occurrence.condition_concept_id - ICD9)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)
		select
			'condition_occurrence' target_table,
			'condition_concept_id' target_column,
			s.condition_source_value source_value,
			s.condition_source_value_vocabulary source_vocabulary,
			'' as source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.condition_source_value is null or length(s.condition_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.condition_occurrence s
			left outer join rz.source_to_concept_map t on s.condition_source_value = t.source_code
				and (t.invalid_reason is null or t.invalid_reason = '') and t.source_vocabulary_id = 1
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.condition_source_value = t2.source_code
				and (t2.invalid_reason is null or t2.invalid_reason = '') and t2.source_vocabulary_id = 1
				and t2.x_data_source_id  = s.x_data_source_id 
		where
			s.condition_source_value_vocabulary = 'SNOMED'
		group by
			s.condition_source_value,
			s.condition_source_value_vocabulary,
			coalesce(t2.target_concept_id,t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (condition_occurrence.condition_concept_id - SNOMED)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

	--	update condition desc
		
		begin
		with td as (select x_data_source_id as x_x_data_source_id, source_code, source_code_desc, source_vocabulary_id from cz.x_source_desc)
		update cz.cz_concept_map
		set source_desc=td.source_code_desc
		from td
		where x_data_source_id = td.x_x_data_source_id
		  and source_value=td.source_code
		  and target_table='condition_occurrence'
		  and target_column='condition_concept_id'
		  and td.source_vocabulary_id=case when source_vocabulary in ('ICD9','ICD-9') then 2 else 1 end;
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Update cz_concept_map (condition_occurrence.condition_concept_id) desc', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
		
/*		fixed by doing count s.x_record_num

		-- Need to update source_count values for condition_concept_id because group by clause in inserts reduces count to 1.
		update cz.cz_concept_map m
		set source_count = s.source_count
		from
			(select condition_source_value source_value, count(1) source_count from std.condition_occurrence group by condition_source_value) s
		where
			m.source_value = s.source_value
			and m.target_table = 'condition_occurrence' and m.target_column = 'condition_concept_id';
	
		
		get diagnostics v_row_count := row_count;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'update cz_concept_map (condition_occurrence.condition_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
*/

		--	move drug insert here so update of desc has fewer records to process
		--	create drug desc
		
		drop table if exists cz.x_source_desc;
		
		begin
		create table cz.x_source_desc as
		select t.x_data_source_id
			  ,t.source_code
			  ,substring(t.source_code_description from 1 for 200) as source_code_desc
			  ,case when t.source_vocabulary_id = 9 then 99 else t.source_vocabulary_id end as source_vocabulary_id
		from rz.source_to_concept_map t
		where t.mapping_type = 'DRUG' 
		  and t.primary_map = 'Y' 
		  and (t.invalid_reason is null or t.invalid_reason = '') 
		  and t.source_vocabulary_id in(9, 99,4,5,3,43);
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Insert std drug desc into cz.x_source_desc', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
		
		create index x_source_desc_idx1 on cz.x_source_desc (x_data_source_id, source_code, source_vocabulary_id);
		
		--	insert any new drug desc from raw data tables
		
		begin
		insert into cz.x_source_desc
		select distinct m1.x_data_source_id
			  ,m1.drug_source_value as source_code
			  ,m1.x_drug_name as source_code_desc
			  ,case when m1.drug_source_value_vocabulary = 'NDC' then 99
					when m1.drug_source_value_vocabulary = 'CPT' then 4
					when m1.drug_source_value_vocabulary = 'HCPCS' then 5
					when m1.drug_source_value_vocabulary = 'ICD9' then 3
					when m1.drug_source_value_vocabulary = 'REVENUE' then 43 else 0 end as source_vocabulary_id
		from std.drug_exposure m1
		where m1.x_record_num in 
			(select min(m2.x_record_num) from std.drug_exposure m2
			 where m2.x_drug_name is not null 
			   and m2.x_drug_name != ''
			 group by m2.x_data_source_id, m2.drug_source_value
			)
		 and m1.drug_source_value_vocabulary in ('NDC','CPT','HCPCS','ICD9','REVENUE')
		  and not exists
			 (select 1 from cz.x_source_desc x 
			  where m1.drug_source_value = x.source_code
				and case when m1.drug_source_value_vocabulary = 'NDC' then 99
					when m1.drug_source_value_vocabulary = 'CPT' then 4
					when m1.drug_source_value_vocabulary = 'HCPCS' then 5
					when m1.drug_source_value_vocabulary = 'ICD9' then 3
					when m1.drug_source_value_vocabulary = 'REVENUE' then 43 else 0 end = x.source_vocabulary_id
					and (m1.x_data_source_id = x.x_data_source_id or x.x_data_source_id = -1));
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Insert new desc into cz.x_source_desc', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
		

		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)
		select
			'drug_exposure' target_table,
			'drug_concept_id' target_column,
			s.drug_source_value source_value,
			s.drug_source_value_vocabulary source_vocabulary,
			'' as source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.drug_source_value is null or length(s.drug_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.drug_exposure s
			left outer join rz.source_to_concept_map t on s.drug_source_value = t.source_code
				and t.mapping_type = 'DRUG' and t.primary_map = 'Y' and (t.invalid_reason is null or t.invalid_reason = '') and (
					(s.drug_source_value_vocabulary = 'NDC' and t.source_vocabulary_id in(9, 99))
					or (s.drug_source_value_vocabulary = 'CPT' and t.source_vocabulary_id = 4)
					or (s.drug_source_value_vocabulary = 'HCPCS' and t.source_vocabulary_id = 5)
					or (s.drug_source_value_vocabulary = 'ICD9' and t.source_vocabulary_id = 3)
					or (s.drug_source_value_vocabulary = 'REVENUE' and t.source_vocabulary_id = 43))
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.drug_source_value = t2.source_code
				and t2.mapping_type = 'DRUG' and t2.primary_map = 'Y' and (t2.invalid_reason is null or t2.invalid_reason = '') and (
					(s.drug_source_value_vocabulary = 'NDC' and t2.source_vocabulary_id in(9, 99))
					or (s.drug_source_value_vocabulary = 'CPT' and t2.source_vocabulary_id = 4)
					or (s.drug_source_value_vocabulary = 'HCPCS' and t2.source_vocabulary_id = 5)
					or (s.drug_source_value_vocabulary = 'ICD9' and t2.source_vocabulary_id = 3)
					or (s.drug_source_value_vocabulary = 'REVENUE' and t2.source_vocabulary_id = 43))
				and t.x_data_source_id  = s.x_data_source_id 
		group by
			s.drug_source_value,
			s.drug_source_value_vocabulary,
			coalesce(t2.target_concept_id,t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (drug_exposure.drug_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		--	update drug desc
		
		begin
		with td as (select x_data_source_id as x_x_data_source_id, source_code, source_code_desc, source_vocabulary_id from cz.x_source_desc)
		update cz.cz_concept_map
		set source_desc=td.source_code_desc
		from td
		where x_data_source_id = td.x_x_data_source_id
		  and source_value=td.source_code
		  and target_table='drug_exposure'
		  and target_column='drug_concept_id'
		  and case when source_vocabulary = 'NDC' then 99
				   when source_vocabulary = 'CPT' then 4
				   when source_vocabulary = 'HCPCS' then 5
				   when source_vocabulary = 'ICD9' then 3
				   when source_vocabulary = 'REVENUE' then 43 else 0 end = td.source_vocabulary_id;
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'Update cz_concept_map (drug_exposure.drug_concept_id) desc', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
	
		drop table if exists cz.x_source_desc;

/*		
		-- Need to update source_count values for drug_concept_id because group by clause in inserts reduces count to 1.
		update cz.cz_concept_map m
		set source_count = s.source_count
		from
			(select drug_source_value source_value, count(1) source_count from std.drug_exposure group by drug_source_value) s
		where
			m.source_value = s.source_value
			and m.target_table = 'drug_exposure' and m.target_column = 'drug_concept_id';
			
		get diagnostics v_row_count := row_count;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'update cz_concept_map (drug_exposure.drug_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
*/
	begin
	insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)		
		select 
			'condition_occurrence' target_table,
			'condition_type_concept_id' target_column,
			s.condition_type_source_value source_value,
			null source_vocabulary,
			null source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.condition_type_source_value is null or length(s.condition_type_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.condition_occurrence s
			left outer join rz.source_to_concept_map t on s.condition_type_source_value = t.source_code
				and t.mapping_type = 'CONDITIONTYPE' and (t.invalid_reason is null or t.invalid_reason = '') and t.source_vocabulary_id = 99
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.condition_type_source_value = t2.source_code
				and t2.mapping_type = 'CONDITIONTYPE' and (t2.invalid_reason is null or t2.invalid_reason = '') and t2.source_vocabulary_id = 99
				and t2.x_data_source_id  = s.x_data_source_id 
		group by
			s.condition_type_source_value,
			coalesce(t2.target_concept_id,t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		get diagnostics v_row_count := row_count;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (condition_occurrence.condition_type_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)		
		select
			'drug_exposure' target_table,
			'drug_type_concept_id' target_column,
			s.drug_type_source_value source_value,
			null source_vocabulary,
			null source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.drug_type_source_value is null or length(s.drug_type_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.drug_exposure s
			left outer join rz.source_to_concept_map t on s.drug_type_source_value = t.source_code
				and t.mapping_type = 'DRUGTYPE' and (t.invalid_reason is null or t.invalid_reason = '') and t.source_vocabulary_id = 99
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.drug_type_source_value = t2.source_code
				and t2.mapping_type = 'DRUGTYPE' and (t2.invalid_reason is null or t2.invalid_reason = '') and t2.source_vocabulary_id = 99
				and t2.x_data_source_id  = s.x_data_source_id 
		group by
			s.drug_type_source_value,
			coalesce(t2.target_concept_id,t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (drug_exposure.drug_type_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)
		select
			'drug_exposure' target_table,
			'relevant_condition_concept_id' target_column,
			s.relevant_condition_source_value source_value,
			null source_vocabulary,
			null source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.relevant_condition_source_value is null or length(s.relevant_condition_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.drug_exposure s
			left outer join rz.source_to_concept_map t on s.relevant_condition_source_value = t.source_code
				and t.mapping_type = 'CONDITION' and (t.invalid_reason is null or t.invalid_reason = '')
				and t.source_vocabulary_id = 2
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.relevant_condition_source_value = t2.source_code
				and t2.mapping_type = 'CONDITION' and (t2.invalid_reason is null or t2.invalid_reason = '')
				and t2.source_vocabulary_id = 2
				and t2.x_data_source_id  = s.x_data_source_id 
		group by
			s.relevant_condition_source_value,
			coalesce(t2.target_concept_id,t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (drug_exposure.relevant_condition_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)
		select
			'observation' target_table,
			'observation_concept_id' target_column,
			s.observation_source_value source_value,
			s.observation_source_value_vocabulary source_vocabulary,
			null source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.observation_source_value is null or length(s.observation_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.observation s
			left outer join rz.source_to_concept_map t on s.observation_source_value = t.source_code
				and t.mapping_type = 'OBSERVATION' and (t.invalid_reason is null or t.invalid_reason = '') and (
					(s.observation_source_value_vocabulary = 'LOINC' and t.source_vocabulary_id = 6)
					or (s.observation_source_value_vocabulary = 'NDC' and t.source_vocabulary_id in (9, 99))
					or (s.observation_source_value_vocabulary = 'CUSTOM' and t.source_vocabulary_id = 99))
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.observation_source_value = t2.source_code
				and t2.mapping_type = 'OBSERVATION' and (t2.invalid_reason is null or t2.invalid_reason = '') and (
					(s.observation_source_value_vocabulary = 'LOINC' and t2.source_vocabulary_id = 6)
					or (s.observation_source_value_vocabulary = 'NDC' and t2.source_vocabulary_id in (9, 99))
					or (s.observation_source_value_vocabulary = 'CUSTOM' and t2.source_vocabulary_id = 99))
				and t2.x_data_source_id  = s.x_data_source_id 
		group by
			s.observation_source_value,
			s.observation_source_value_vocabulary,
			coalesce(t2.target_concept_id, t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (observation.observation_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)
		select
			'observation' target_table,
			'observation_type_concept_id' target_column,
			s.observation_type_source_value source_value,
			null source_vocabulary,
			null source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.observation_type_source_value is null or length(s.observation_type_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.observation s
			left outer join rz.source_to_concept_map t on s.observation_type_source_value = t.source_code
				and t.mapping_type = 'OBSERVATIONTYPE' and (t.invalid_reason is null or t.invalid_reason = '') and t.source_vocabulary_id = 99
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.observation_type_source_value = t2.source_code
				and t2.mapping_type = 'OBSERVATIONTYPE' and (t2.invalid_reason is null or t.invalid_reason = '') and t2.source_vocabulary_id = 99
				and t2.x_data_source_id  = s.x_data_source_id 
		group by
			s.observation_type_source_value,
			coalesce(t2.target_concept_id, t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (observation.observation_type_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)
		select
			'observation' target_table,
			'relevant_condition_concept_id' target_column,
			s.relevant_condition_source_value source_value,
			null source_vocabulary,
			null source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.relevant_condition_source_value is null or length(s.relevant_condition_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.observation s
			left outer join rz.source_to_concept_map t on s.relevant_condition_source_value = t.source_code
				and t.mapping_type = 'CONDITION' and (t.invalid_reason is null or t.invalid_reason = '')
				and t.source_vocabulary_id = 2
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.relevant_condition_source_value = t2.source_code
				and t2.mapping_type = 'CONDITION' and (t2.invalid_reason is null or t2.invalid_reason = '')
				and t2.source_vocabulary_id = 2
				and t2.x_data_source_id  = s.x_data_source_id 
		group by
			s.relevant_condition_source_value,
			coalesce(t2.target_concept_id, t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (observation.relevant_condition_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)
		select
			'observation' target_table,
			'unit_concept_id' target_column,
			s.unit_source_value source_value,
			null source_vocabulary,
			null source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.unit_source_value is null or length(s.unit_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.observation s
			left outer join rz.source_to_concept_map t on s.unit_source_value = t.source_code
				and t.mapping_type = 'UNIT' and (t.invalid_reason is null or t.invalid_reason = '')
				and t.source_vocabulary_id = 11
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.unit_source_value = t2.source_code
				and t2.mapping_type = 'UNIT' and (t2.invalid_reason is null or t2.invalid_reason = '')
				and t2.source_vocabulary_id = 11
				and t2.x_data_source_id  = s.x_data_source_id 
		group by
			s.unit_source_value,
			coalesce(t2.target_concept_id, t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (observation.unit_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;


		-- This section is commented out, but may be used once it is determined how to map this field to concept IDs.
/*
		insert into cz.cz_concept_map (target_table, target_column, source_value, source_vocabulary, source_desc, target_concept_id, is_mapped, is_empty, source_count)
		select
			'observation' target_table,
			'value_as_concept_id' target_column,
			s.value_as_string source_value,
			null source_vocabulary,
			null source_desc,
			0 target_concept_id,
			'N' is_mapped,
			case
				when s.value_as_string is null or length(s.value_as_string) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(1) source_count
		from
			std.observation s
		group by
			s.value_as_string;

		get diagnostics v_row_count := row_count;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (observation.value_as_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
*/

		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)
		select
			'organization' target_table,
			'place_of_service_concept_id' target_column,
			s.place_of_service_source_value source_value,
			null source_vocabulary,
			null source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.place_of_service_source_value is null or length(s.place_of_service_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )			
		from
			std.organization s
			left outer join rz.source_to_concept_map t on s.place_of_service_source_value = t.source_code and
				t.mapping_type = 'Place of Service' and (t.invalid_reason is null or t.invalid_reason = '')
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.place_of_service_source_value = t2.source_code and
				t2.mapping_type = 'Place of Service' and (t2.invalid_reason is null or t2.invalid_reason = '')
				and t2.x_data_source_id  = s.x_data_source_id 
		group by
			s.place_of_service_source_value,
			coalesce(t2.target_concept_id, t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (organization.place_of_service_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)
		select
			'person' target_table,
			'gender_concept_id' target_column,
			s.gender_source_value source_value,
			null source_vocabulary,
			null source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.gender_source_value is null or length(s.gender_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.x_demographic s
			left outer join rz.source_to_concept_map t on s.gender_source_value = t.source_code and
				t.mapping_type = 'gender' and (t.invalid_reason is null or t.invalid_reason = '')
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.gender_source_value = t2.source_code and
				t2.mapping_type = 'gender' and (t2.invalid_reason is null or t2.invalid_reason = '')
				and t2.x_data_source_id  = s.x_data_source_id 
		group by
			s.gender_source_value,
			coalesce(t2.target_concept_id, t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (person.gender_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)
		select
			'person' target_table,
			'race_concept_id' target_column,
			s.race_source_value source_value,
			null source_vocabulary,
			null source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.race_source_value is null or length(s.race_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.x_demographic s
			left outer join rz.source_to_concept_map t on s.race_source_value = t.source_code and
				t.mapping_type = 'race' and (t.invalid_reason is null or t.invalid_reason = '')
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.race_source_value = t2.source_code and
				t2.mapping_type = 'race' and (t2.invalid_reason is null or t2.invalid_reason = '')
				and t2.x_data_source_id  = s.x_data_source_id 
		group by
			s.race_source_value,
			coalesce(t2.target_concept_id, t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (person.race_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)
		select
			'person' target_table,
			'ethnicity_concept_id' target_column,
			s.ethnicity_source_value source_value,
			null source_vocabulary,
			null source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.ethnicity_source_value is null or length(s.ethnicity_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.x_demographic s
			left outer join rz.source_to_concept_map t on s.ethnicity_source_value = t.source_code and
				t.mapping_type = 'ethnicity' and (t.invalid_reason is null or t.invalid_reason = '')
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.ethnicity_source_value = t2.source_code and
				t2.mapping_type = 'ethnicity' and (t2.invalid_reason is null or t2.invalid_reason = '')
				and t2.x_data_source_id  = s.x_data_source_id 
		group by
			s.ethnicity_source_value,
			coalesce(t2.target_concept_id, t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (person.ethnicity_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)
		select
			'procedure_occurrence' target_table,
			'procedure_concept_id' target_column,
			s.procedure_source_value source_value,
			s.procedure_source_value_vocabulary source_vocabulary,
			null source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.procedure_source_value is null or length(s.procedure_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.procedure_occurrence s
			left outer join rz.source_to_concept_map t on s.procedure_source_value = t.source_code
				and t.mapping_type = 'PROCEDURE' and (t.invalid_reason is null or t.invalid_reason = '') and (
					(s.procedure_source_value_vocabulary = 'CPT' and t.source_vocabulary_id in (4, 99))
					or (s.procedure_source_value_vocabulary = 'HCPCS' and t.source_vocabulary_id in (5, 99))
					or (s.procedure_source_value_vocabulary = 'ICD9' and t.source_vocabulary_id = 3)
					or (s.procedure_source_value_vocabulary = 'REVENUE' and t.source_vocabulary_id = 43))
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.procedure_source_value = t2.source_code
				and t2.mapping_type = 'PROCEDURE' and (t2.invalid_reason is null or t2.invalid_reason = '') and (
					(s.procedure_source_value_vocabulary = 'CPT' and t2.source_vocabulary_id in (4, 99))
					or (s.procedure_source_value_vocabulary = 'HCPCS' and t2.source_vocabulary_id in (5, 99))
					or (s.procedure_source_value_vocabulary = 'ICD9' and t2.source_vocabulary_id = 3)
					or (s.procedure_source_value_vocabulary = 'REVENUE' and t2.source_vocabulary_id = 43))
				and t2.x_data_source_id  = s.x_data_source_id 
		group by
			s.procedure_source_value,
			s.procedure_source_value_vocabulary,
			coalesce(t2.target_concept_id, t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (procedure_occurrence.procedure_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)
		select
			'procedure_occurrence' target_table,
			'procedure_type_concept_id' target_column,
			s.procedure_type_source_value source_value,
			null source_vocabulary,
			null source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.procedure_type_source_value is null or length(s.procedure_type_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.procedure_occurrence s
			left outer join rz.source_to_concept_map t on s.procedure_type_source_value = t.source_code
				and t.mapping_type = 'PROCEDURETYPE' and (t.invalid_reason is null or t.invalid_reason = '') and t.source_vocabulary_id = 99
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.procedure_type_source_value = t2.source_code
				and t2.mapping_type = 'PROCEDURETYPE' and (t2.invalid_reason is null or t2.invalid_reason = '') and t2.source_vocabulary_id = 99
				and t2.x_data_source_id  = s.x_data_source_id 
		group by
			s.procedure_type_source_value,
			coalesce(t2.target_concept_id, t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (procedure_occurrence.procedure_type_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)
		select
			'procedure_occurrence' target_table,
			'relevant_condition_concept_id' target_column,
			s.relevant_condition_source_value source_value,
			null source_vocabulary,
			null source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.relevant_condition_source_value is null or length(s.relevant_condition_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.procedure_occurrence s
			left outer join rz.source_to_concept_map t on s.relevant_condition_source_value = t.source_code
				and t.mapping_type = 'CONDITION' and (t.invalid_reason is null or t.invalid_reason = '')
				and t.source_vocabulary_id = 2
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.relevant_condition_source_value = t2.source_code
				and t2.mapping_type = 'CONDITION' and (t2.invalid_reason is null or t2.invalid_reason = '')
				and t2.source_vocabulary_id = 2
				and t2.x_data_source_id  = s.x_data_source_id 
		group by
			s.relevant_condition_source_value,
			coalesce(t2.target_concept_id, t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (procedure_occurrence.relevant_condition_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
		
		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)
		select
			'procedure_cost' target_table,
			'disease_class_concept_id' target_column,
			s.disease_class_source_value source_value,
			null source_vocabulary,
			null source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.disease_class_source_value is null or length(s.disease_class_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.procedure_cost s
			left outer join rz.source_to_concept_map t on s.disease_class_source_value = t.source_code
				and t.mapping_type = 'MDC' and (t.invalid_reason is null or t.invalid_reason = '')
				and t.source_vocabulary_id = 41
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.disease_class_source_value = t2.source_code
				and t2.mapping_type = 'MDC' and (t2.invalid_reason is null or t2.invalid_reason = '')
				and t2.source_vocabulary_id = 41
				and t2.x_data_source_id  = s.x_data_source_id 
		group by
			s.disease_class_source_value,
			coalesce(t2.target_concept_id, t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (procedure_cost.disease_class_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
		
		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)
		select
			'procedure_cost' target_table,
			'revenue_code_concept_id' target_column,
			s.revenue_code_source_value source_value,
			null source_vocabulary,
			null source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.revenue_code_source_value is null or length(s.revenue_code_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.procedure_cost s
			left outer join rz.source_to_concept_map t on s.revenue_code_source_value = t.source_code
				and t.mapping_type = 'Revenue Code' and (t.invalid_reason is null or t.invalid_reason = '')
				and t.source_vocabulary_id = 43
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.revenue_code_source_value = t2.source_code
				and t2.mapping_type = 'Revenue Code' and (t2.invalid_reason is null or t2.invalid_reason = '')
				and t2.source_vocabulary_id = 43
				and t2.x_data_source_id  = s.x_data_source_id 
		group by
			s.revenue_code_source_value,
			coalesce(t2.target_concept_id, t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (procedure_cost.revenue_code_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
		
		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)
		select
			'provider' target_table,
			'specialty_concept_id' target_column,
			s.specialty_source_value source_value,
			null source_vocabulary,
			null source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.specialty_source_value is null or length(s.specialty_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.provider s
			left outer join rz.source_to_concept_map t on s.specialty_source_value = t.source_code
				and t.mapping_type = 'provider_specialty' and (t.invalid_reason is null or t.invalid_reason = '')
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.specialty_source_value = t2.source_code
				and t2.mapping_type = 'provider_specialty' and (t.invalid_reason is null or t2.invalid_reason = '')
				and t2.x_data_source_id  = s.x_data_source_id 
		group by
			s.specialty_source_value,
			coalesce(t2.target_concept_id, t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (provider.specialty_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)
		select
			'visit_occurrence' target_table,
			'place_of_service_concept_id' target_column,
			s.place_of_service_source_value source_value,
			null source_vocabulary,
			null source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.place_of_service_source_value is null or length(s.place_of_service_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.visit_occurrence s
			left outer join rz.source_to_concept_map t on s.place_of_service_source_value = t.source_code and
				t.mapping_type = 'visit_placeofservice' and (t.invalid_reason is null or t.invalid_reason = '')
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.place_of_service_source_value = t2.source_code and
				t2.mapping_type = 'visit_placeofservice' and (t2.invalid_reason is null or t2.invalid_reason = '')
				and t2.x_data_source_id  = s.x_data_source_id 
		group by
			s.place_of_service_source_value,
			coalesce(t2.target_concept_id, t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (visit_occurrence.place_of_service_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
		v_job_status := 'SUCCESS';
		
		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)
		select
			'death' target_table,
			'death_type_concept_id' target_column,
			s.death_type_source_value source_value,
			null source_vocabulary,
			null source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.death_type_source_value is null or length(s.death_type_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.death s
			left outer join rz.source_to_concept_map t on s.death_type_source_value = t.source_code
				and t.mapping_type = 'Death Type' and (t.invalid_reason is null or t.invalid_reason = '')
				and t.source_vocabulary_id = 45
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.death_type_source_value = t2.source_code
				and t2.mapping_type = 'Death Type' and (t2.invalid_reason is null or t2.invalid_reason = '')
				and t2.source_vocabulary_id = 45
				and t2.x_data_source_id  = s.x_data_source_id 
		group by
			s.death_type_source_value,
			coalesce(t2.target_concept_id, t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (death.death_type_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into cz.cz_concept_map 
		(target_table
		,target_column
		,source_value
		,source_vocabulary
		,source_desc
		,target_concept_id
		,is_mapped
		,is_empty
		,source_count
		,x_data_source_id 
		)
		select
			'death' target_table,
			'cause_of_death_concept_id' target_column,
			s.cause_of_death_source_value source_value,
			null source_vocabulary,
			null source_desc,
			case when coalesce(t2.target_concept_id,t.target_concept_id) is null then 0 else coalesce(t2.target_concept_id,t.target_concept_id) end target_concept_id,
			case
				when coalesce(t2.target_concept_id,t.target_concept_id) is not null then 'Y'
				else 'N'
			end is_mapped,
			case
				when s.cause_of_death_source_value is null or length(s.cause_of_death_source_value) = 0 then 'Y'
				else 'N'
			end is_empty,
			count(s.x_record_num) source_count,
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id )
		from
			std.death s
			left outer join rz.source_to_concept_map t on s.cause_of_death_source_value = t.source_code
				and t.mapping_type = 'DEATHCAUSE' and (t.invalid_reason is null or t.invalid_reason = '')
				--and t.source_vocabulary_id = 2
				and t.x_data_source_id  = -1
			left outer join rz.source_to_concept_map t2 on s.cause_of_death_source_value = t2.source_code
				and t2.mapping_type = 'DEATHCAUSE' and (t2.invalid_reason is null or t2.invalid_reason = '')
				--and t2.source_vocabulary_id = 2
				and t2.x_data_source_id  = s.x_data_source_id 
		group by
			s.cause_of_death_source_value,
			coalesce(t2.target_concept_id, t.target_concept_id),
			coalesce(t2.x_data_source_id ,t.x_data_source_id ,s.x_data_source_id );
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_concept_map (death.cause_of_death_concept_id)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
		
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end czx_concept_analyze', v_row_count, v_step_ct, 'COMPLETE');
		return 0;

	/*	
	if p_step_id is null or p_step_id < 1 then
		perform cz.czx_end_audit(v_step_id, v_job_status);
	end if;
	*/
	
	
	
end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cz.czx_concept_analyze(bigint)
  OWNER TO rosita;
