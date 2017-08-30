-- Function: omop.omx_load_location(bigint)

-- DROP FUNCTION omop.omx_load_location(bigint);

CREATE OR REPLACE FUNCTION omop.omx_load_location(p_step_id bigint)
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
	v_function_name := 'omx_load_location';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;		


	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;


	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start omx_load_location', v_row_count, v_step_ct, 'START');
		
		-- Drop the index that causes long loading time
		begin
			DROP INDEX omop.omop_location_source_type_idx;
			DROP INDEX omop.omop_location_src_city_zip_state_idx;
			get diagnostics v_row_count = row_count;
		end;

		
		begin
			truncate table omop.location;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate location', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;


		perform setval('omop.omop_location_sq', 1, false);

		get diagnostics v_row_count = row_count;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'reset omop_location_sq', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into omop.location (
			address_1,
			address_2,
			city,
			state,
			zip,
			county,
			location_source_value,
			x_zip_deidentified,
			x_location_type,
			x_data_source_type,
			x_grid_node_id,
			x_data_source_id
		)
		select distinct
			o.organization_address_1 address_1,
			o.organization_address_2 address_2,
			o.organization_city city,
			o.organization_state state,
			o.organization_zip zip,
			o.organization_county county,
			o.organization_source_value location_source_value,
			substring(o.organization_zip from 1 for 3) x_zip_deidentified,
			'organization' x_location_type,
			o.x_data_source_type x_data_source_type,
			0 x_grid_node_id,
			o.x_data_source_id
		 from
			(SELECT DISTINCT og.organization_source_value, og.x_data_source_type, og.place_of_service_source_value, 
			og.organization_address_1, og.organization_address_2, og.organization_city, 
			og.organization_state, og.organization_zip, og.organization_county, og.x_data_source_id, 
			og.x_etl_date, og.x_record_num from std.organization og INNER JOIN (select
					organization_source_value, x_data_source_id, 
					max(to_char(x_etl_date, 'YYYYMMDDHHMISS') || to_char(x_record_num, '0000000000')) last_rec
					from std.organization
					group by
					organization_source_value, x_data_source_id) og_maxrec ON og.organization_source_value = og_maxrec.organization_source_value
					and og.x_data_source_id = og_maxrec.x_data_source_id
					and to_char(og.x_etl_date, 'YYYYMMDDHHMISS') || to_char(og.x_record_num, '0000000000') = og_maxrec.last_rec ) o;
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert location (organization)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into omop.location (
			address_1,
			address_2,
			city,
			state,
			zip,
			county,
			location_source_value,
			x_zip_deidentified,
			x_location_type,
			x_data_source_type,
			x_grid_node_id,
			x_data_source_id
		)
		select distinct
			trim(c.care_site_address_1) address_1,
			c.care_site_address_2 address_2,
			c.care_site_city city,
			c.care_site_state state,
			c.care_site_zip zip,
			c.care_site_county county,
			c.care_site_source_value location_source_value,
			substring(c.care_site_zip from 1 for 3) x_zip_deidentified,
			'care_site'  x_location_type,
			c.x_data_source_type x_data_source_type,
			0 x_grid_node_id,
			c.x_data_source_id
		from
			(SELECT DISTINCT cs.care_site_source_value, cs.x_data_source_type, cs.organization_source_value, 
			cs.place_of_service_source_value, cs.x_care_site_name, cs.care_site_address_1, 
			cs.care_site_address_2, cs.care_site_city, cs.care_site_state, cs.care_site_zip, 
			cs.care_site_county, cs.x_data_source_id, cs.x_etl_date, cs.x_record_num
			FROM std.care_site cs INNER JOIN (select
					care_site_source_value, x_data_source_id, 
					max(to_char(x_etl_date, 'YYYYMMDDHHMISS') || to_char(x_record_num, '0000000000')) last_rec
					from std.care_site
					group by
					care_site_source_value, x_data_source_id) cs_maxrec ON cs.care_site_source_value = cs_maxrec.care_site_source_value
					and cs.x_data_source_id = cs_maxrec.x_data_source_id
					and to_char(cs.x_etl_date, 'YYYYMMDDHHMISS') || to_char(cs.x_record_num, '0000000000') = cs_maxrec.last_rec ) c;
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert location (care_site)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into omop.location (
			address_1,
			address_2,
			city,
			state,
			zip,
			county,
			location_source_value,
			x_zip_deidentified,
			x_location_type,
			x_data_source_type,
			x_grid_node_id,
			x_data_source_id
		)
		select distinct
			null address_1,
			null address_2,
			d.city city,
			d.state state,
			d.zip zip,
			d.county county,
			'0' location_source_value,
			substring(d.zip from 1 for 3) x_zip_deidentified,
			'person'  x_location_type,
			d.x_data_source_type x_data_source_type,  
			0 x_grid_node_id,
			d.x_data_source_id
		from
			std.x_demographic d
			inner join (select person_source_value, x_data_source_id, max(to_char(x_etl_date,'YYYYMMDDHHMISS') || to_char(x_record_num,'0000000')) as latest_rec
						from std.x_demographic
						group by person_source_value, x_data_source_id) x
				  on  d.person_source_value = x.person_source_value
				  and d.x_data_source_id = x.x_data_source_id
				  and to_char(d.x_etl_date,'YYYYMMDDHHMISS') || to_char(d.x_record_num,'0000000') = x.latest_rec;
			--join (select x_data_source_id, min(x_record_num::bigint) x_record_num from std.x_demographic group by x_data_source_id, person_source_value) d1
			--	on d.x_record_num::bigint = d1.x_record_num::bigint
			--	and d.x_data_source_id = d1.x_data_source_id;
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;

		begin
			CREATE INDEX omop_location_source_type_idx
			ON omop.location
			USING btree
			(x_data_source_id, location_source_value COLLATE pg_catalog."default" , x_location_type COLLATE pg_catalog."default" )
			TABLESPACE rosita_indx;
			get diagnostics v_row_count = row_count;

			CREATE INDEX omop_location_src_city_zip_state_idx
			ON omop.location
			USING btree
			(x_data_source_id, x_location_type COLLATE pg_catalog."default", city COLLATE pg_catalog."default", state COLLATE pg_catalog."default", zip COLLATE pg_catalog."default", county COLLATE pg_catalog."default" )
			TABLESPACE rosita_indx;

			get diagnostics v_row_count := row_count;
		end;


		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert location (person)', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		/*
	if p_step_id is null or p_step_id < 1 then
		perform cz.czx_end_audit(v_step_id, v_job_status);
	end if;
	*/

	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end omx_load_location', v_row_count, v_step_ct, 'COMPLETE');
	return 0;
	

end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION omop.omx_load_location(bigint)
  OWNER TO rosita;