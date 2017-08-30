-- Function: omop.omx_load_visit_occurrence(bigint)

-- DROP FUNCTION omop.omx_load_visit_occurrence(bigint);

CREATE OR REPLACE FUNCTION omop.omx_load_visit_occurrence(p_step_id bigint)
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
	-- added by PWH 11/13/13
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start omx_load_visit_occurrence1', v_row_count, v_step_ct, 'START');
	v_database_name := 'omop';
	v_function_name := 'omx_load_visit_occurrence';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;		

	-- added by PWH 11/13/13
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start omx_load_visit_occurrence2', v_row_count, v_step_ct, 'START');

	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;

	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start omx_load_visit_occurrence', v_row_count, v_step_ct, 'START');
		begin
		insert into cz.cz_id_map (
			x_data_source_id,
			source_value,
			id_type
		)
		select distinct
			v.x_data_source_id,
			v.x_visit_occurrence_source_identifier,
			'visit_id'  
		from
			std.visit_occurrence v
		where
			not exists (
				select 1 from cz.cz_id_map i
				where i.source_value = v.x_visit_occurrence_source_identifier and i.id_type = 'visit_id'
				  and i.x_data_source_id = v.x_data_source_id
			);
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert cz_id_map', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		--pulled by PWH 11/13/13
		DROP INDEX omop.omop_visit_occurrence_source_idx;
		get diagnostics v_row_count = row_count;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'drop index omop_visit_occurrence_source_idx', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
		
		begin
		truncate table omop.visit_occurrence;
		--delete from omop.visit_occurrence;
		--vacuum full omop.visit_occurrence;
		
		get diagnostics v_row_count = row_count;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate visit_occurrence', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;


		begin
		insert into omop.visit_occurrence (
			visit_occurrence_id,
			person_id,
			visit_start_date,
			visit_end_date,
			place_of_service_concept_id,
			care_site_id,
			place_of_service_source_value,
			x_visit_occurrence_source_identifier,
			x_data_source_type,
			x_provider_id,
			x_grid_node_id,
			x_data_source_id
		)
		select distinct
			temp3.visit_occurrence_id,
			temp3.person_id,
			temp3.visit_start_date,
			temp3.visit_end_date,
			coalesce(mx.target_concept_id, m.target_concept_id) place_of_service_concept_id,
			temp3.care_site_id,
			temp3.place_of_service_source_value,
			temp3.visit_occurrence_source_identifier,
			temp3.x_data_source_type,
			temp3.x_provider_id,
			temp3.x_grid_node_id,
			temp3.x_data_source_id
		from (select distinct
			temp2.visit_occurrence_id,
			temp2.person_id,
			temp2.visit_start_date,
			temp2.visit_end_date,
			--coalesce(mx.target_concept_id, m.target_concept_id) place_of_service_concept_id,
			temp2.care_site_id,
			temp2.place_of_service_source_value,
			temp2.visit_occurrence_source_identifier,
			temp2.x_data_source_type,
			pv.provider_id x_provider_id,
			temp2.x_grid_node_id,
			temp2.x_data_source_id

		from (select distinct
			temp1.visit_occurrence_id,
			temp1.person_id,
			temp1.visit_start_date,
			temp1.visit_end_date,
			--coalesce(mx.target_concept_id, m.target_concept_id) place_of_service_concept_id,
			c.care_site_id care_site_id,
			temp1.place_of_service_source_value,
			temp1.visit_occurrence_source_identifier,
			temp1.x_provider_source_value,
			temp1.x_data_source_type,
			--pv.provider_id x_provider_id,
			temp1.x_grid_node_id,
			temp1.x_data_source_id
		from (select distinct
			temp0.visit_occurrence_id,
			p.id person_id,
			temp0.visit_start_date,
			temp0.visit_end_date,
			--coalesce(mx.target_concept_id, m.target_concept_id) place_of_service_concept_id,
			--c.care_site_id care_site_id,
			temp0.place_of_service_source_value,
			temp0.visit_occurrence_source_identifier,
			temp0.x_data_source_type,
			temp0.x_provider_source_value,
			temp0.care_site_source_value,
			--pv.provider_id x_provider_id,
			temp0.x_grid_node_id,
			temp0.x_data_source_id
		from (select distinct
			i.id as visit_occurrence_id,
			--p.id person_id,
			v.visit_start_date,
			v.visit_end_date,
			--coalesce(mx.target_concept_id, m.target_concept_id) place_of_service_concept_id,
			--c.care_site_id care_site_id,
			v.place_of_service_source_value place_of_service_source_value,
			v.x_visit_occurrence_source_identifier visit_occurrence_source_identifier,
			v.x_data_source_type x_data_source_type,
			--pv.provider_id x_provider_id,
			0 x_grid_node_id,
			v.person_source_value,
			v.x_provider_source_value,
			v.care_site_source_value,
			v.x_data_source_id
		from (select distinct
			vo.x_visit_occurrence_source_identifier, vo.person_source_value, vo.care_site_source_value, vo.x_provider_source_value,
			vo.place_of_service_source_value, vo.visit_start_date, visit_end_date,
			vo.x_data_source_type, vo.x_data_source_id, vo.x_etl_date, vo.x_record_num
			from
			std.visit_occurrence vo inner join (select x_visit_occurrence_source_identifier, x_data_source_id, 
							max(to_char(x_etl_date, 'YYYYMMDDHHMISS') || to_char(x_record_num, '0000000000')) last_rec
							from std.visit_occurrence
							group by
							x_visit_occurrence_source_identifier, x_data_source_id) maxrec
							on vo.x_visit_occurrence_source_identifier = maxrec.x_visit_occurrence_source_identifier
							and vo.x_data_source_id = maxrec.x_data_source_id
							and to_char(vo.x_etl_date, 'YYYYMMDDHHMISS') || to_char(vo.x_record_num, '0000000000') = maxrec.last_rec) v
			inner join cz.cz_id_map i on v.x_visit_occurrence_source_identifier = i.source_value and i.id_type = 'visit_id'
				and v.x_data_source_id = i.x_data_source_id) temp0
			inner join cz.cz_id_map p on temp0.person_source_value = p.source_value and p.id_type = 'person_id'
				and temp0.x_data_source_id = p.x_data_source_id
			inner join omop.person op 
				on  p.id = op.person_id) temp1
			left outer join omop.care_site c on c.care_site_source_value = temp1.care_site_source_value
				and temp1.x_data_source_id = c.x_data_source_id) temp2
			left outer join omop.provider pv on temp2.x_provider_source_value = pv.provider_source_value
				and temp2.x_data_source_id = pv.x_data_source_id) temp3
			left outer join cz.cz_concept_map m on temp3.place_of_service_source_value = m.source_value
				and m.target_table = 'visit_occurrence' and m.target_column = 'place_of_service_concept_id'
				and m.x_data_source_id = -1
			left outer join cz.cz_concept_map mx on temp3.place_of_service_source_value = mx.source_value
				and mx.target_table = 'visit_occurrence' and mx.target_column = 'place_of_service_concept_id'
				and mx.x_data_source_id = temp3.x_data_source_id;

		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert visit_occurrence', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		CREATE INDEX omop_visit_occurrence_source_idx
		  ON omop.visit_occurrence
		  USING btree
		  (x_data_source_id, x_visit_occurrence_source_identifier COLLATE pg_catalog."default")
		TABLESPACE rosita_indx;
		get diagnostics v_row_count = row_count;
		end;

		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'create index omop_visit_occurrence_source_idx', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		select count(*) into v_row_count
		from  std.visit_occurrence c 	 
		inner join cz.cz_id_map p 
			  on  c.person_source_value = p.source_value 
			  and c.x_data_source_id = p.x_data_source_id
			  and p.id_type = 'person_id'
		where not exists
			  (select 1 from omop.person op
			   where p.id = op.person_id);
		if v_row_count > 0 then
			perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'visit_occurrence records not loaded, person linked in cz.cz_id_map but no omop.person record', v_row_count, v_step_ct, 'WARNING');
			v_step_ct := v_step_ct + 1;
		end if;		

	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end omx_load_visit_occurrence', v_row_count, v_step_ct, 'COMPLETE');
	return 0;
	

end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION omop.omx_load_visit_occurrence(bigint)
  OWNER TO rosita;