-- Function: omop.omx_load_provider(bigint)

-- DROP FUNCTION omop.omx_load_provider(bigint);

CREATE OR REPLACE FUNCTION omop.omx_load_provider(p_step_id bigint)
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
	v_function_name := 'omx_load_provider';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;		


	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;


	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start omx_load_provider', v_row_count, v_step_ct, 'START');

		
		begin
			DROP INDEX omop.omop_provider_source_idx;
			get diagnostics v_row_count = row_count;
		end;

		
		begin
		truncate table omop.provider;
		get diagnostics v_row_count = row_count;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate provider', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into omop.provider (
			npi,
			dea,
			specialty_concept_id,
			care_site_id,
			provider_source_value,
			specialty_source_value ,
			x_data_source_type,
			x_provider_first,
			x_provider_middle,
			x_provider_last,
			x_organization_id,
			x_grid_node_id,
			x_data_source_id
		)
		select distinct
			p.npi npi,
			p.dea dea,
			coalesce(mx.target_concept_id, m.target_concept_id) specialty_concept_id,  
			c.care_site_id care_site_id,   
			p.provider_source_value provider_source_value,
			p.specialty_source_value specialty_source_value,
			p.x_data_source_type x_data_source_type,
			p.x_provider_first x_provider_first,
			p.x_provider_middle x_provider_middle,
			p.x_provider_last x_provider_last,
			o.organization_id x_organization_id,  
			0 x_grid_node_id,
			p.x_data_source_id
		from
			std.provider p
			inner join (select provider_source_value, x_data_source_id, min(to_char(x_etl_date,'YYYYMMDDHHMISS') || to_char(x_record_num,'0000000')) as first_rec
						from std.provider
						group by provider_source_value, x_data_source_id) x
				  on  p.provider_source_value = x.provider_source_value
				  and p.x_data_source_id = x.x_data_source_id
				  and to_char(p.x_etl_date,'YYYYMMDDHHMISS') || to_char(p.x_record_num,'0000000') = x.first_rec
			--join (select x_data_source_id, min(x_record_num::bigint) x_record_num from std.provider group by x_data_source_id, provider_source_value) p1
			--	on p.x_record_num::bigint = p1.x_record_num::bigint
			--	and p.x_data_source_id = p1.x_data_source_id
			left outer join omop.care_site c on p.care_site_source_value = c.care_site_source_value
				 and p.x_data_source_id = c.x_data_source_id
			join omop.organization o on p.x_organization_source_value = o.organization_source_value
				 and p.x_data_source_id = o.x_data_source_id
			left outer join cz.cz_concept_map m on p.specialty_source_value = m.source_value
				and m.target_table = 'provider' and m.target_column = 'specialty_concept_id'
				and m.x_data_source_id = -1
			left outer join cz.cz_concept_map mx on p.specialty_source_value = mx.source_value
				and mx.target_table = 'provider' and mx.target_column = 'specialty_concept_id'
				and mx.x_data_source_id = p.x_data_source_id
			where coalesce(mx.target_concept_id, m.target_concept_id, -99) >= 0;
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;

		begin
			CREATE INDEX omop_provider_source_idx
			ON omop.provider
			USING btree
			(x_data_source_id, provider_source_value COLLATE pg_catalog."default" )
			TABLESPACE rosita_indx;

			get diagnostics v_row_count = row_count;
		end;

		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert provider', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

	if p_step_id is null or p_step_id < 1 then
		perform cz.czx_end_audit(v_step_id, v_job_status);
	end if;
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end omx_load_provider', v_row_count, v_step_ct, 'COMPLETE');
	return 0;

end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION omop.omx_load_provider(bigint)
  OWNER TO rosita;