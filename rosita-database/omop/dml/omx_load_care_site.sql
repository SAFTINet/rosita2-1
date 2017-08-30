-- Function: omop.omx_load_care_site(bigint)

-- DROP FUNCTION omop.omx_load_care_site(bigint);

CREATE OR REPLACE FUNCTION omop.omx_load_care_site(p_step_id bigint)
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
	v_function_name := 'omx_load_care_site';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;		

	
	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;
	
	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start omx_load_care_site', v_row_count, v_step_ct, 'START');

		-- Drop the index that caused long loading time
		begin
			DROP INDEX omop.omop_care_site_source_idx;
			get diagnostics v_row_count = row_count;
		end;

		begin

			truncate table omop.care_site;
			get diagnostics v_row_count = row_count;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate care_site', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;


		perform setval('omop.omop_care_site_sq', 1, false);

		--get diagnostics v_row_count = row_count;

		


		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'reset omop_care_site_sq', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into omop.care_site (
			location_id,
			organization_id,
			place_of_service_concept_id,
			care_site_source_value,
			place_of_service_source_value,
			x_data_source_type,
			x_care_site_name,
			x_grid_node_id,
			x_data_source_id
		)
		select distinct 
			l.location_id location_id,
			o.organization_id organization_id,
			coalesce(m2.target_concept_id,m.target_concept_id) place_of_service_concept_id,
			c.care_site_source_value care_site_source_value,
			c.place_of_service_source_value place_of_service_source_value,
			c.x_data_source_type x_data_source_type,
			c.x_care_site_name x_care_site_name,
			0 x_grid_node_id,
			c.x_data_source_id
		from
			(SELECT DISTINCT cs.care_site_source_value, cs.x_data_source_type, cs.organization_source_value, 
			cs.place_of_service_source_value, cs.x_care_site_name, cs.care_site_address_1, 
			cs.care_site_address_2, cs.care_site_city, cs.care_site_state, cs.care_site_zip, 
			cs.care_site_county, cs.x_data_source_id, cs.x_etl_date, cs.x_record_num
			FROM std.care_site cs INNER JOIN 
						(select care_site_source_value, x_data_source_id, 
						max(to_char(x_etl_date, 'YYYYMMDDHHMISS') || to_char(x_record_num, '0000000000')) last_rec
						from std.care_site
						group by care_site_source_value, x_data_source_id) cs_maxrec ON cs.care_site_source_value = cs_maxrec.care_site_source_value
						and cs.x_data_source_id = cs_maxrec.x_data_source_id
						and to_char(cs.x_etl_date, 'YYYYMMDDHHMISS') || to_char(cs.x_record_num, '0000000000') = cs_maxrec.last_rec)c
			join omop.location l on c.care_site_source_value = l.location_source_value and l.x_location_type = 'care_site'
				 and c.x_data_source_id = l.x_data_source_id
			join omop.organization o on c.organization_source_value = o.organization_source_value 
				 and c.x_data_source_id = o.x_data_source_id
			left outer join cz.cz_concept_map m on c.place_of_service_source_value = m.source_value
				and m.target_table = 'care_site' and m.target_column = 'place_of_service_concept_id'
				and m.x_data_source_id = -1
			left outer join cz.cz_concept_map m2 on c.place_of_service_source_value = m2.source_value
				and m2.target_table = 'care_site' and m2.target_column = 'place_of_service_concept_id'
				and m2.x_data_source_id = c.x_data_source_id
		where coalesce(m2.target_concept_id,m.target_concept_id,-99) >= 0;
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert care_site', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		
	/*
	if p_step_id is null or p_step_id < 1 then
		perform cz.czx_end_audit(v_step_id, v_job_status);
	end if;
	*/

	
	begin
		CREATE INDEX omop_care_site_source_idx
		ON omop.care_site
		USING btree
		(x_data_source_id, care_site_source_value COLLATE pg_catalog."default" )
		TABLESPACE rosita_indx;
		
		get diagnostics v_row_count = row_count;
	end;

	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end omx_load_care_site', v_row_count, v_step_ct, 'COMPLETE');
	return 0;
	

end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION omop.omx_load_care_site(bigint)
  OWNER TO rosita;