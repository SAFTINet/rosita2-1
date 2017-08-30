-- Function: omop.omx_load_person(bigint)

-- DROP FUNCTION omop.omx_load_person(bigint);

CREATE OR REPLACE FUNCTION omop.omx_load_person(p_step_id bigint)
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
	v_function_name := 'omx_load_person';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;		


	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;


	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start omx_load_person', v_row_count, v_step_ct, 'START');
	
	--	removed insert to cz.cz_id_map, this functionality is now implemented in the record linkage processes
	
/*
		begin
		insert into cz.cz_id_map (
			x_data_source_id,
			source_value,
			id_type
		)
		select distinct
			d.x_data_source_id,
			d.person_source_value,
			'person_id'  
		from
			std.x_demographic d
		where
			not exists (
				select 1 from cz.cz_id_map m
				where m.source_value = d.person_source_value and m.id_type = 'person_id'
				and m.x_data_source_id = d.x_data_source_id
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
*/

		begin
			DROP INDEX omop.omop_person_source_idx;
			get diagnostics v_row_count = row_count;
		end;


		begin
		truncate table omop.person;
		get diagnostics v_row_count = row_count;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate person', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into omop.person (
			person_id,
			gender_concept_id,
			year_of_birth,
			month_of_birth,
			day_of_birth,
			race_concept_id,
			ethnicity_concept_id,
			location_id,
			provider_id,
			care_site_id,
			person_source_value,
			gender_source_value,
			race_source_value,
			ethnicity_source_value,
			x_organization_id,
			x_grid_node_id,
			x_data_source_id
		)
		select distinct
			temp4.id,
			coalesce(m1x.target_concept_id, m1.target_concept_id) gender_concept_id,
			temp4.year_of_birth,
			temp4.month_of_birth,
			temp4.day_of_birth,
			coalesce(m2x.target_concept_id, m2.target_concept_id) race_concept_id, 
			coalesce(m3x.target_concept_id, m3.target_concept_id) ethnicity_concept_id, 
			temp4.location_id, 
			temp4.provider_id, 
			temp4.care_site_id, 
			temp4.person_source_value, 
			temp4.gender_source_value,  
			temp4.race_source_value, 
			temp4.ethnicity_source_value,  
			temp4.x_organization_id, 
			temp4.x_grid_node_id,
			temp4.x_data_source_id
		from (select distinct
			temp3.id,
			--coalesce(m1x.target_concept_id, m1.target_concept_id) gender_concept_id,
			temp3.year_of_birth,
			temp3.month_of_birth,
			temp3.day_of_birth,
			--coalesce(m2x.target_concept_id, m2.target_concept_id) race_concept_id, 
			--coalesce(m3x.target_concept_id, m3.target_concept_id) ethnicity_concept_id, 
			temp3.location_id, 
			temp3.provider_id, 
			c.care_site_id care_site_id, 
			temp3.person_source_value, 
			temp3.gender_source_value,  
			temp3.race_source_value, 
			temp3.ethnicity_source_value,  
			temp3.x_organization_id, 
			temp3.x_grid_node_id,
			temp3.x_data_source_id
		from (select distinct
			temp2.id,
			--coalesce(m1x.target_concept_id, m1.target_concept_id) gender_concept_id,
			temp2.year_of_birth,
			temp2.month_of_birth,
			temp2.day_of_birth,
			--coalesce(m2x.target_concept_id, m2.target_concept_id) race_concept_id, 
			--coalesce(m3x.target_concept_id, m3.target_concept_id) ethnicity_concept_id, 
			temp2.location_id, 
			p.provider_id provider_id, 
			--c.care_site_id care_site_id, 
			temp2.person_source_value, 
			temp2.gender_source_value,  
			temp2.race_source_value, 
			temp2.ethnicity_source_value,  
			temp2.x_organization_id,
			temp2.care_site_source_value, 
			temp2.x_grid_node_id,
			temp2.x_data_source_id
		from (select distinct
			temp1.id,
			--coalesce(m1x.target_concept_id, m1.target_concept_id) gender_concept_id,
			temp1.year_of_birth,
			temp1.month_of_birth,
			temp1.day_of_birth,
			--coalesce(m2x.target_concept_id, m2.target_concept_id) race_concept_id, 
			--coalesce(m3x.target_concept_id, m3.target_concept_id) ethnicity_concept_id, 
			temp1.location_id, 
			--p.provider_id provider_id, 
			--c.care_site_id care_site_id, 
			temp1.person_source_value, 
			temp1.gender_source_value,  
			temp1.race_source_value, 
			temp1.ethnicity_source_value,
			temp1.provider_source_value,
			temp1.care_site_source_value,
			o.organization_id x_organization_id, 
			temp1.x_grid_node_id,
			temp1.x_data_source_id
		from (select distinct
			temp0.id,
			--coalesce(m1x.target_concept_id, m1.target_concept_id) gender_concept_id,
			temp0.year_of_birth,
			temp0.month_of_birth,
			temp0.day_of_birth,
			--coalesce(m2x.target_concept_id, m2.target_concept_id) race_concept_id, 
			--coalesce(m3x.target_concept_id, m3.target_concept_id) ethnicity_concept_id, 
			l.location_id location_id, 
			--p.provider_id provider_id, 
			--c.care_site_id care_site_id, 
			temp0.person_source_value, 
			temp0.gender_source_value,  
			temp0.race_source_value, 
			temp0.ethnicity_source_value,  
			--o.organization_id x_organization_id, 
			temp0.x_grid_node_id,
			temp0.x_organization_source_value,
			temp0.provider_source_value,
			temp0.care_site_source_value,
			temp0.x_data_source_id
		from (select distinct
			i.id,
			--coalesce(m1x.target_concept_id, m1.target_concept_id) gender_concept_id,
			d.year_of_birth year_of_birth,
			d.month_of_birth month_of_birth,
			d.day_of_birth day_of_birth,
			--coalesce(m2x.target_concept_id, m2.target_concept_id) race_concept_id, 
			--coalesce(m3x.target_concept_id, m3.target_concept_id) ethnicity_concept_id, 
			--l.location_id location_id, 
			--p.provider_id provider_id, 
			--c.care_site_id care_site_id, 
			d.person_source_value person_source_value, 
			d.gender_source_value gender_source_value,  
			d.race_source_value race_source_value, 
			d.ethnicity_source_value ethnicity_source_value,
			d.city,
			d.state,
			d.zip,
			d.x_organization_source_value,
			d.provider_source_value,
			d.care_site_source_value,
			d.county,
			--o.organization_id x_organization_id, 
			0 x_grid_node_id,
			d.x_data_source_id
		from
			std.x_demographic d
			inner join cz.cz_data_source ds
				  on  d.x_data_source_id = ds.x_data_source_id
				  and ds.data_source_type = 'CLINICAL'
			inner join (select person_source_value, x_data_source_id, max(to_char(x_etl_date,'YYYYMMDDHHMISS') || to_char(x_record_num,'0000000')) as latest_rec
						from std.x_demographic
						group by person_source_value, x_data_source_id) x
				  on  d.person_source_value = x.person_source_value
				  and d.x_data_source_id = x.x_data_source_id
				  and to_char(d.x_etl_date,'YYYYMMDDHHMISS') || to_char(d.x_record_num,'0000000') = x.latest_rec
			inner join cz.cz_id_map i on d.person_source_value = i.source_value and i.id_type = 'person_id'
				 and d.x_data_source_id = i.x_data_source_id) temp0
			inner join omop.location l on temp0.city = l.city and temp0.state = l.state and temp0.zip = l.zip and temp0.county = l.county and l.x_location_type = 'person'
				 and temp0.x_data_source_id = l.x_data_source_id) temp1
			inner join omop.organization o on temp1.x_organization_source_value = o.organization_source_value 
				 and temp1.x_data_source_id = o.x_data_source_id) temp2
			left outer join omop.provider p on temp2.provider_source_value = p.provider_source_value	
				 and temp2.x_data_source_id = p.x_data_source_id) temp3
			left outer join omop.care_site c on temp3.care_site_source_value = c.care_site_source_value
				 and temp3.x_data_source_id = c.x_data_source_id) temp4
			left outer join cz.cz_concept_map m1 on temp4.gender_source_value = m1.source_value
				and m1.target_table = 'person' and m1.target_column = 'gender_concept_id'
				and m1.x_data_source_id = -1
			left outer join cz.cz_concept_map m1x on temp4.gender_source_value = m1x.source_value
				and m1x.target_table = 'person' and m1x.target_column = 'gender_concept_id'
				and m1x.x_data_source_id = temp4.x_data_source_id
			left outer join cz.cz_concept_map m2 on temp4.race_source_value = m2.source_value
				and m2.target_table = 'person' and m2.target_column = 'race_concept_id'
				and m2.x_data_source_id = -1
			left outer join cz.cz_concept_map m2x on temp4.race_source_value = m2x.source_value
				and m2x.target_table = 'person' and m2x.target_column = 'race_concept_id'
				and m2x.x_data_source_id = temp4.x_data_source_id
			left outer join cz.cz_concept_map m3 on temp4.ethnicity_source_value = m3.source_value
				and m3.target_table = 'person' and m3.target_column = 'ethnicity_concept_id'
				and m3.x_data_source_id= -1
			left outer join cz.cz_concept_map m3x on temp4.ethnicity_source_value = m3x.source_value
				and m3x.target_table = 'person' and m3x.target_column = 'ethnicity_concept_id'
				and m3x.x_data_source_id= temp4.x_data_source_id;
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;

		begin
			CREATE INDEX omop_person_source_idx
			ON omop.person
			USING btree
			(x_data_source_id, person_source_value COLLATE pg_catalog."default" )
			TABLESPACE rosita_indx;

			
		end;

		

		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert person', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;
		
		get diagnostics v_row_count = row_count;

		if v_row_count = 0 then
			perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'No records inserted into omop.person', v_row_count, v_step_ct, 'ERROR');
			v_job_status := 'ERROR';
			return 16;
		end if;

		/*
	if p_step_id is null or p_step_id < 1 then
		perform cz.czx_end_audit(v_step_id, v_job_status);
	end if;
	*/
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end omx_load_person', v_row_count, v_step_ct, 'COMPLETE');
	return 0;
	

end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION omop.omx_load_person(bigint)
  OWNER TO rosita;