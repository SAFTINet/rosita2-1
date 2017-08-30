-- Function: omop.omx_load_observation(bigint)

-- DROP FUNCTION omop.omx_load_observation(bigint);

CREATE OR REPLACE FUNCTION omop.omx_load_observation(p_step_id bigint)
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
	v_function_name := 'omx_load_observation';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;		


	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;		

	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start omx_load_observation', v_row_count, v_step_ct, 'START');

	begin
		truncate table omop.observation;

		get diagnostics v_row_count = row_count;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate observation', v_row_count, v_step_ct, v_job_status);
		v_step_ct := v_step_ct + 1;


		perform setval('omop.omop_obs_sq', 1, false);

		get diagnostics v_row_count = row_count;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'reset omop_obs_sq', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;


		insert into omop.observation (
			person_id,
			observation_concept_id,
			observation_date,
			observation_time,
			value_as_number,
			value_as_string,
			value_as_concept_id,
			unit_concept_id,
			range_low,
			range_high,
			observation_type_concept_id,
			associated_provider_id,
			visit_occurrence_id,
			relevant_condition_concept_id,
			observation_source_value,
			unit_source_value,
			x_data_source_type,
			x_obs_comment,
			x_grid_node_id,
			x_data_source_id
		)
		select distinct
			temp1.person_id, 	
			coalesce(m1x.target_concept_id, m1.target_concept_id) observation_concept_id ,  
			temp1.observation_date,
			temp1.observation_time,
			temp1.value_as_number,  
			temp1.value_as_string,
			temp1.value_as_concept_id, -- hardcode to 0 until mapping this to concept id is determined
			--m2.target_concept_id value_as_concept_id,
			coalesce(m3x.target_concept_id, m3.target_concept_id) unit_concept_id,
			temp1.range_low,
			temp1.range_high,
			coalesce(m4x.target_concept_id, m4.target_concept_id) observation_type_concept_id,
			temp1.associated_provider_id, 
			temp1.visit_occurrence_id, 
			coalesce(m5x.target_concept_id, m5.target_concept_id) relevant_condition_concept_id, 
			temp1.observation_source_value,
			temp1.unit_source_value,  
			temp1.x_data_source_type,
			temp1.x_obs_comment,
			temp1.x_grid_node_id,
			temp1.x_data_source_id
		from
		(select distinct
			temp0.person_id, 	
			--coalesce(m1x.target_concept_id, m1.target_concept_id) observation_concept_id ,  
			temp0.observation_date,
			temp0.observation_time,
			temp0.value_as_number,  
			temp0.value_as_string,
			temp0.value_as_concept_id, -- hardcode to 0 until mapping this to concept id is determined
			--m2.target_concept_id value_as_concept_id,
			--coalesce(m3x.target_concept_id, m3.target_concept_id) unit_concept_id,
			temp0.range_low,
			temp0.range_high,
			--coalesce(m4x.target_concept_id, m4.target_concept_id) observation_type_concept_id,
			pv.provider_id associated_provider_id, 
			v.visit_occurrence_id visit_occurrence_id, 
			--coalesce(m5x.target_concept_id, m5.target_concept_id) relevant_condition_concept_id, 
			temp0.observation_source_value,
			temp0.observation_source_value_vocabulary,
			temp0.relevant_condition_source_value,
			temp0.observation_type_source_value,
			temp0.unit_source_value,  
			temp0.x_data_source_type,
			temp0.x_obs_comment,
			temp0.x_grid_node_id,
			temp0.x_data_source_id
		from
		(select distinct
			p.id person_id, 	
			--coalesce(m1x.target_concept_id, m1.target_concept_id) observation_concept_id ,  
			o.observation_date observation_date,
			o.observation_time observation_time,
			o.value_as_number value_as_number,  
			o.value_as_string value_as_string,
			0 value_as_concept_id, -- hardcode to 0 until mapping this to concept id is determined
			--m2.target_concept_id value_as_concept_id,
			--coalesce(m3x.target_concept_id, m3.target_concept_id) unit_concept_id,
			o.range_low range_low,
			o.range_high range_high,
			--coalesce(m4x.target_concept_id, m4.target_concept_id) observation_type_concept_id,
			--pv.provider_id associated_provider_id, 
			--v.visit_occurrence_id visit_occurrence_id, 
			--coalesce(m5x.target_concept_id, m5.target_concept_id) relevant_condition_concept_id, 
			o.observation_source_value observation_source_value,
			o.unit_source_value unit_source_value,  
			o.x_data_source_type x_data_source_type,
			o.x_obs_comment x_obs_comment,
			0 x_grid_node_id,
			p.id id,
			o.associated_provider_source_value,
			o.x_visit_occurrence_source_identifier,
			o.observation_source_value_vocabulary,
			o.observation_type_source_value,
			o.relevant_condition_source_value,
			o.x_data_source_id
		from
			(SELECT DISTINCT ob.observation_source_identifier, ob.x_data_source_type, ob.person_source_value, 
			ob.observation_source_value, ob.observation_source_value_vocabulary, 
			ob.observation_date, ob.observation_time, ob.value_as_number, ob.value_as_string, 
			ob.unit_source_value, ob.range_low, ob.range_high, ob.observation_type_source_value, 
			ob.associated_provider_source_value, ob.x_visit_occurrence_source_identifier, 
			ob.relevant_condition_source_value, ob.x_obs_comment, ob.x_data_source_id, 
			ob.x_etl_date, ob.x_record_num
			FROM std.observation ob INNER JOIN 
						(select observation_source_identifier, x_data_source_id, 
						max(to_char(x_etl_date, 'YYYYMMDDHHMISS') || to_char(x_record_num, '0000000000')) last_rec
						from std.observation
						group by observation_source_identifier, x_data_source_id) ob_maxrec ON ob.observation_source_identifier = ob_maxrec.observation_source_identifier
						and ob.x_data_source_id = ob_maxrec.x_data_source_id
						and to_char(ob.x_etl_date, 'YYYYMMDDHHMISS') || to_char(ob.x_record_num, '0000000000') = ob_maxrec.last_rec) o

			join cz.cz_id_map p on o.person_source_value = p.source_value 
				 and o.x_data_source_id = p.x_data_source_id
				 and p.id_type = 'person_id'
			inner join omop.person op
				  on  p.id = op.person_id) temp0
			left outer join omop.provider pv on temp0.associated_provider_source_value =  pv.provider_source_value
				 and temp0.x_data_source_id = pv.x_data_source_id
			left outer join omop.visit_occurrence v on temp0.x_visit_occurrence_source_identifier = v.x_visit_occurrence_source_identifier
				 and temp0.x_data_source_id = v.x_data_source_id) temp1
			left outer join cz.cz_concept_map m1 on temp1.observation_source_value = m1.source_value and temp1.observation_source_value_vocabulary = m1.source_vocabulary
				and m1.target_table = 'observation' and m1.target_column = 'observation_concept_id'
				and m1.x_data_source_id = -1
			left outer join cz.cz_concept_map m1x on temp1.observation_source_value = m1x.source_value and temp1.observation_source_value_vocabulary = m1x.source_vocabulary
				and m1x.target_table = 'observation' and m1x.target_column = 'observation_concept_id'
				and m1x.x_data_source_id = temp1.x_data_source_id
			-- join cz.cz_concept_map m2 on o.value_as_string = m2.source_value
			--	and m2.target_table = 'observation' and m2.target_column = 'value_as_concept_id'
			left outer join cz.cz_concept_map m3 on temp1.unit_source_value = m3.source_value
				and m3.target_table = 'observation' and m3.target_column = 'unit_concept_id'
				and m3.x_data_source_id = -1
			left outer join cz.cz_concept_map m3x on temp1.unit_source_value = m3x.source_value
				and m3x.target_table = 'observation' and m3x.target_column = 'unit_concept_id'
				and m3x.x_data_source_id = temp1.x_data_source_id
			left outer join cz.cz_concept_map m4 on temp1.observation_type_source_value = m4.source_value
				and m4.target_table = 'observation' and m4.target_column = 'observation_type_concept_id'
				and m4.x_data_source_id = -1
			left outer join cz.cz_concept_map m4x on temp1.observation_type_source_value = m4x.source_value
				and m4x.target_table = 'observation' and m4x.target_column = 'observation_type_concept_id'
				and m4x.x_data_source_id = temp1.x_data_source_id
			left outer join cz.cz_concept_map m5 on temp1.relevant_condition_source_value = m5.source_value
				and m5.target_table = 'observation' and m5.target_column = 'relevant_condition_concept_id'
				and m5.x_data_source_id = -1
			left outer join cz.cz_concept_map m5x on temp1.relevant_condition_source_value = m5x.source_value
				and m5x.target_table = 'observation' and m5x.target_column = 'relevant_condition_concept_id'
				and m5x.x_data_source_id = temp1.x_data_source_id
			where coalesce(m1x.target_concept_id, m1.target_concept_id,-99) >= 0
			  and coalesce(m3x.target_concept_id, m3.target_concept_id,-99) >= 0
			  and coalesce(m4x.target_concept_id, m4.target_concept_id,-99) >= 0
			  and coalesce(m5x.target_concept_id, m5.target_concept_id,-99) >= 0;					
		get diagnostics v_row_count = row_count;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert observation', v_row_count, v_step_ct, v_job_status);
		v_step_ct := v_step_ct + 1;
	exception
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
	end;
	
	select count(*) into v_row_count
	from  std.observation c 	 
	inner join cz.cz_id_map p 
		  on  c.person_source_value = p.source_value 
		  and c.x_data_source_id = p.x_data_source_id
		  and p.id_type = 'person_id'
	where not exists
		  (select 1 from omop.person op
		   where p.id = op.person_id);
	if v_row_count > 0 then
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'observation records not loaded, person linked in cz.cz_id_map but no omop.person record', v_row_count, v_step_ct, 'WARNING');
		v_step_ct := v_step_ct + 1;
	end if;	

	/*
	if p_step_id is null or p_step_id < 1 then
		perform cz.czx_end_audit(v_step_id, v_job_status);
	end if;
	*/
	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end omx_load_observation', v_row_count, v_step_ct, 'COMPLETE');
	return 0;
	

end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION omop.omx_load_observation(bigint)
  OWNER TO rosita;