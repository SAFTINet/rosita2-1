-- Function: omop.omx_load_condition_occurrence(bigint)

-- DROP FUNCTION omop.omx_load_condition_occurrence(bigint);

CREATE OR REPLACE FUNCTION omop.omx_load_condition_occurrence(p_step_id bigint)
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
	v_function_name := 'omx_load_condition_occurrence';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;		


	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;	

	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start omx_load_condition_occurrence', v_row_count, v_step_ct, 'START');
	
		begin
		truncate table omop.condition_occurrence;
		get diagnostics v_row_count = row_count;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate condition_occurrence', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;


		perform setval('omop.omop_cond_occur_sq', 1, false);

		get diagnostics v_row_count = row_count;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'reset omop_cond_occur_sq', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into omop.condition_occurrence (
			person_id,
			condition_concept_id ,
			condition_start_date,
			condition_end_date,
			condition_type_concept_id,
			stop_reason,
			associated_provider_id,
			visit_occurrence_id,
			condition_source_value,
			x_data_source_type,
			x_condition_source_desc,
			x_condition_update_date,
			x_grid_node_id,
			x_data_source_id
		)
		select distinct 
			temp1.person_id ,  
			coalesce(m1x.target_concept_id,m1.target_concept_id) condition_concept_id, 
			temp1.condition_start_date,
			temp1.condition_end_date,
			coalesce(m2x.target_concept_id, m2.target_concept_id) condition_type_concept_id,
			temp1.stop_reason stop_reason,
			temp1.associated_provider_id,  
			temp1.visit_occurrence_id,  
			temp1.condition_source_value condition_source_value,
			temp1.x_data_source_type x_data_source_type,
			temp1.x_condition_source_desc x_condition_source_desc,
			temp1.x_condition_update_date,
			temp1.x_grid_node_id, --default		
			temp1.x_data_source_id
		from
		(select distinct 
			temp0.person_id ,  
			--coalesce(m1x.target_concept_id,m1.target_concept_id) condition_concept_id, 
			temp0.condition_start_date,
			temp0.condition_end_date,
			--coalesce(m2x.target_concept_id, m2.target_concept_id) condition_type_concept_id,
			temp0.stop_reason stop_reason,
			pv.provider_id associated_provider_id,  
			v.visit_occurrence_id visit_occurrence_id,  
			temp0.condition_source_value condition_source_value,
			temp0.x_data_source_type x_data_source_type,
			temp0.x_condition_source_desc x_condition_source_desc,
			temp0.x_condition_update_date,
			temp0.condition_source_value_vocabulary,
			temp0.condition_type_source_value,
			temp0.x_grid_node_id, --default		
			temp0.x_data_source_id
		from
		(select distinct 
			p.id person_id ,  
			--coalesce(m1x.target_concept_id,m1.target_concept_id) condition_concept_id, 
			c.condition_start_date condition_start_date,
			case
				when c.condition_end_date is null then null
				else c.condition_end_date
			end condition_end_date,
			--coalesce(m2x.target_concept_id, m2.target_concept_id) condition_type_concept_id,
			c.stop_reason stop_reason,
			--pv.provider_id associated_provider_id,  
			--v.visit_occurrence_id visit_occurrence_id,  
			c.condition_source_value condition_source_value,
			c.x_data_source_type x_data_source_type,
			c.associated_provider_source_value,
			c.condition_source_value_vocabulary,
			c.condition_type_source_value,
			c.x_visit_occurrence_source_identifier,
			c.x_condition_source_desc x_condition_source_desc,
			case
				when c.x_condition_update_date is null then null
				else c.x_condition_update_date
			end x_condition_update_date,
			0 x_grid_node_id, --default		
			c.x_data_source_id
		from 	(SELECT DISTINCT co.condition_occurrence_source_identifier, co.x_data_source_type, co.person_source_value, 
			       co.condition_source_value, co.condition_source_value_vocabulary, co.x_condition_source_desc, 
			       co.condition_start_date, co.x_condition_update_date, co.condition_end_date, 
			       co.condition_type_source_value, co.stop_reason, co.associated_provider_source_value, 
			       co.x_visit_occurrence_source_identifier, co.x_data_source_id, co.x_etl_date
				FROM std.condition_occurrence co INNER JOIN (select
						condition_occurrence_source_identifier, x_data_source_id, 
						max(to_char(x_etl_date, 'YYYYMMDDHHMISS') || to_char(x_record_num, '0000000000')) last_rec
						from std.condition_occurrence
						group by
						condition_occurrence_source_identifier, x_data_source_id) co_maxrec ON co.condition_occurrence_source_identifier = co_maxrec.condition_occurrence_source_identifier
							and co.x_data_source_id = co_maxrec.x_data_source_id
							and to_char(co.x_etl_date, 'YYYYMMDDHHMISS') || to_char(co.x_record_num, '0000000000') = co_maxrec.last_rec ) c 	 
			join cz.cz_id_map p on c.person_source_value = p.source_value 
				 and c.x_data_source_id = p.x_data_source_id
				 and p.id_type = 'person_id'
			inner join omop.person op
				  on  p.id = op.person_id) temp0
			left outer join omop.provider pv on temp0.associated_provider_source_value = pv.provider_source_value   
				 and temp0.x_data_source_id = pv.x_data_source_id
			left outer join omop.visit_occurrence v on temp0.x_visit_occurrence_source_identifier = v.x_visit_occurrence_source_identifier  
				 and temp0.x_data_source_id = v.x_data_source_id) temp1
			left outer join cz.cz_concept_map m1 on temp1.condition_source_value = m1.source_value and temp1.condition_source_value_vocabulary = m1.source_vocabulary
				and m1.target_table = 'condition_occurrence' and m1.target_column = 'condition_concept_id'
				and m1.x_data_source_id = -1
			left outer join cz.cz_concept_map m1x on temp1.condition_source_value = m1x.source_value and temp1.condition_source_value_vocabulary = m1x.source_vocabulary
				and m1x.target_table = 'condition_occurrence' and m1x.target_column = 'condition_concept_id'
				and m1x.x_data_source_id = temp1.x_data_source_id
			left join cz.cz_concept_map m2 on temp1.condition_type_source_value = m2.source_value
				and m2.target_table = 'condition_occurrence' and m2.target_column = 'condition_type_concept_id'
				and m2.x_data_source_id = -1
			left join cz.cz_concept_map m2x on temp1.condition_type_source_value = m2x.source_value
				and m2x.target_table = 'condition_occurrence' and m2x.target_column = 'condition_type_concept_id'
				and m2x.x_data_source_id = temp1.x_data_source_id
		where coalesce(m1.target_concept_id,m1x.target_concept_id,-99) >= 0
		  and coalesce(m2.target_concept_id,m2x.target_concept_id,-99) >= 0;
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert condition_occurrence', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
		
		select count(*) into v_row_count
		from  std.condition_occurrence c 	 
		inner join cz.cz_id_map p 
			  on  c.person_source_value = p.source_value 
			  and c.x_data_source_id = p.x_data_source_id
			  and p.id_type = 'person_id'
		where not exists
			  (select 1 from omop.person op
			   where p.id = op.person_id);
		if v_row_count > 0 then
			perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'condition_occurrence records not loaded, person linked in cz.cz_id_map but no omop.person record', v_row_count, v_step_ct, 'WARNING');
			v_step_ct := v_step_ct + 1;
		end if;
		
	/*
	if p_step_id is null or p_step_id < 1 then
		perform cz.czx_end_audit(v_step_id, v_job_status);
	end if;
	*/
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end omx_load_condition_occurrence', v_row_count, v_step_ct, 'COMPLETE');
	return 0;

end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION omop.omx_load_condition_occurrence(bigint)
  OWNER TO rosita;