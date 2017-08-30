-- Function: omop.omx_load_death(bigint)

-- DROP FUNCTION omop.omx_load_death(bigint);

CREATE OR REPLACE FUNCTION omop.omx_load_death(p_step_id bigint)
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
	v_function_name := 'omx_load_death';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;

	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;	

	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start omx_load_death', v_row_count, v_step_ct, 'START');

	begin
		truncate table omop.death;
		get diagnostics v_row_count = row_count;
	end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate death', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;


		perform setval('omop.omop_death_sq', 1, false);
		
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'reset omop_death_sq', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
		
		
	begin
		insert into omop.death(
			person_id,
			death_date,
			death_type_concept_id,
			cause_of_death_concept_id,
			cause_of_death_source_value,
			x_grid_node_id,
			x_data_source_id,
			x_data_source_type
			)
			
			select distinct
			p.id person_id,
			d.death_date death_date,
			coalesce(m1x.target_concept_id, m1.target_concept_id) death_type_concept_id,
			coalesce(m2x.target_concept_id, m2.target_concept_id) cause_of_death_concept_id,
			d.cause_of_death_source_value cause_of_death_source_value,
			0 x_grid_node_id,
			d.x_data_source_id x_data_source_id,
			d.x_data_source_type
			
			from
			(SELECT DISTINCT de.person_source_value, de.x_data_source_type, de.death_date, de.death_type_source_value, 
			de.cause_of_death_source_value, de.x_data_source_id, de.x_etl_date, de.x_record_num
			FROM std.death de INNER JOIN 
						(select person_source_value, x_data_source_id, 
						max(to_char(x_etl_date, 'YYYYMMDDHHMISS') || to_char(x_record_num, '0000000000')) last_rec
						from std.death
						group by person_source_value, x_data_source_id) de_maxrec ON de.person_source_value = de_maxrec.person_source_value
						and de.x_data_source_id = de_maxrec.x_data_source_id
						and to_char(de.x_etl_date, 'YYYYMMDDHHMISS') || to_char(de.x_record_num, '0000000000') = de_maxrec.last_rec) d 
			join cz.cz_id_map p on d.person_source_value = p.source_value 
				 and d.x_data_source_id = p.x_data_source_id
				 and p.id_type = 'person_id'
			inner join omop.person op
				  on  p.id = op.person_id
			left outer join cz.cz_concept_map m1 on d.death_type_source_value = m1.source_value
				and m1.target_table = 'death' and m1.target_column = 'death_type_concept_id'
				and m1.x_data_source_id = -1
			left outer join cz.cz_concept_map m1x on d.death_type_source_value = m1x.source_value
				and m1x.target_table = 'death' and m1x.target_column = 'death_type_concept_id'
				and m1x.x_data_source_id = d.x_data_source_id
				
			left outer join cz.cz_concept_map m2 on d.cause_of_death_source_value = m2.source_value
				and m2.target_table = 'death' and m2.target_column = 'cause_of_death_concept_id'
				and m2.x_data_source_id = -1
				
			left outer join cz.cz_concept_map m2x on d.cause_of_death_source_value = m2x.source_value
				and m2x.target_table = 'death' and m2x.target_column = 'cause_of_death_concept_id'
				and m2x.x_data_source_id = d.x_data_source_id
		where coalesce(m1.target_concept_id,m1x.target_concept_id,-99) >= 0
			and coalesce(m2.target_concept_id,m2x.target_concept_id,-99) >= 0;
				 
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert death', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
		
		select count(*) into v_row_count
		from  std.death c 	 
		inner join cz.cz_id_map p 
			  on  c.person_source_value = p.source_value 
			  and c.x_data_source_id = p.x_data_source_id
			  and p.id_type = 'person_id'
		where not exists
			  (select 1 from omop.person op
			   where p.id = op.person_id);
		if v_row_count > 0 then
			perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'death records not loaded, person linked in cz.cz_id_map but no omop.person record', v_row_count, v_step_ct, 'WARNING');
			v_step_ct := v_step_ct + 1;
		end if;
	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end omx_load_death', v_row_count, v_step_ct, 'COMPLETE');
	return 0;
	

end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION omop.omx_load_death(bigint)
  OWNER TO rosita;