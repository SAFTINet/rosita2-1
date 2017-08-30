-- Function: omop.omx_load_organization(bigint)

-- DROP FUNCTION omop.omx_load_organization(bigint);

CREATE OR REPLACE FUNCTION omop.omx_load_organization(p_step_id bigint)
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
	v_function_name := 'omx_load_organization';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;		


	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;

	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start omx_load_organization', v_row_count, v_step_ct, 'START');
	
	begin

		begin
			DROP INDEX omop.omop_organization_source_idx;
			get diagnostics v_row_count = row_count;
		end;


		truncate table omop.organization;

		get diagnostics v_row_count = row_count;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate organization', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;


		perform setval('omop.omop_organization_sq', 1, false);

		get diagnostics v_row_count = row_count;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'reset omop_organization_sq', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;


		insert into omop.organization (
			place_of_service_concept_id,
			location_id,
			organization_source_value,
			place_of_service_source_value,
			x_data_source_type,
			x_grid_node_id,
			x_data_source_id
		)
		select  distinct
			coalesce(m1.target_concept_id, m.target_concept_id) place_of_service_concept_id,  
			l.location_id location_id,  
			o.organization_source_value organization_source_value,
			o.place_of_service_source_value place_of_service_source_value,
			case when o.x_data_source_type is null then '0' else o.x_data_source_type end x_data_source_type,  
			0 x_grid_node_id,
			o.x_data_source_id
		from
			(SELECT DISTINCT og.organization_source_value, og.x_data_source_type, og.place_of_service_source_value, 
			og.organization_address_1, og.organization_address_2, og.organization_city, 
			og.organization_state, og.organization_zip, og.organization_county, og.x_data_source_id, 
			og.x_etl_date, og.x_record_num
			FROM std.organization og INNER JOIN 
						(select organization_source_value, x_data_source_id, 
						max(to_char(x_etl_date, 'YYYYMMDDHHMISS') || to_char(x_record_num, '0000000000')) last_rec
						from std.organization
						group by organization_source_value, x_data_source_id) og_maxrec ON og.organization_source_value = og_maxrec.organization_source_value
						and og.x_data_source_id = og_maxrec.x_data_source_id
						and to_char(og.x_etl_date, 'YYYYMMDDHHMISS') || to_char(og.x_record_num, '0000000000') = og_maxrec.last_rec) o
						
			join omop.location l on o.organization_source_value = l.location_source_value  and l.x_location_type = 'organization'
				 and o.x_data_source_id = l.x_data_source_id
			left outer join cz.cz_concept_map m on o.place_of_service_source_value = m.source_value
				and m.target_table = 'organization' and m.target_column = 'place_of_service_concept_id'
				and m.x_data_source_id = -1
			left outer join cz.cz_concept_map m1 on o.place_of_service_source_value = m1.source_value
				and m1.target_table = 'organization' and m1.target_column = 'place_of_service_concept_id'
				and m1.x_data_source_id = o.x_data_source_id
		where coalesce(m1.target_concept_id, m.target_concept_id, -99) >= 0;

		get diagnostics v_row_count = row_count;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert organization', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

	exception
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
	end;

	/*
	if p_step_id is null or p_step_id < 1 then
		perform cz.czx_end_audit(v_step_id, v_job_status);
	end if;
	*/


	begin
		CREATE INDEX omop_organization_source_idx
		ON omop.organization
		USING btree
		(x_data_source_id, organization_source_value COLLATE pg_catalog."default" )
		TABLESPACE rosita_indx;

		get diagnostics v_row_count = row_count;
	end;


	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end omx_load_organization', v_row_count, v_step_ct, 'COMPLETE');	
	return 0;


end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION omop.omx_load_organization(bigint)
  OWNER TO rosita;