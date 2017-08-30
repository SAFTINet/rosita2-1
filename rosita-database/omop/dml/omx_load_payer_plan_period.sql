-- Function: omop.omx_load_payer_plan_period(bigint)

-- DROP FUNCTION omop.omx_load_payer_plan_period(bigint);

CREATE OR REPLACE FUNCTION omop.omx_load_payer_plan_period(p_step_id bigint)
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
	v_function_name := 'omx_load_payer_plan_period';
	v_step_id := p_step_id;
	v_job_status := 'SUCCESS';
	v_step_ct := 1;		


	if p_step_id is null or p_step_id < 1 then
		v_step_id := cz.czx_start_audit();
	end if;


	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'start omx_load_payer_plan_period', v_row_count, v_step_ct, 'START');
		begin
		truncate table omop.payer_plan_period;
		get diagnostics v_row_count = row_count;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'truncate payer_plan_period', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		begin
		insert into omop.payer_plan_period (
			person_id,
			payer_plan_period_start_date,
			payer_plan_period_end_date,
			payer_source_value,
			plan_source_value,
			family_source_value ,
			x_grid_node_id,
			x_data_source_id,
			x_data_source_type
		)
		select distinct
			p.id,
			ppp.payer_plan_period_start_date,
			ppp.payer_plan_period_end_date,
			ppp.payer_source_value,
			ppp.plan_source_value,
			ppp.family_source_value,
			0 x_grid_node_id,
			p.x_data_source_id,
			ppp.x_data_source_type
		from
			(SELECT DISTINCT ppp1.payer_plan_period_source_identifier, ppp1.x_data_source_type, ppp1.person_source_value, 
			ppp1.payer_plan_period_start_date, ppp1.payer_plan_period_end_date, ppp1.payer_source_value, 
			ppp1.plan_source_value, ppp1.family_source_value, ppp1.x_data_source_id, ppp1.x_etl_date, ppp1.x_record_num
			FROM std.payer_plan_period ppp1 INNER JOIN 
						(select payer_plan_period_source_identifier, x_data_source_id, 
						max(to_char(x_etl_date, 'YYYYMMDDHHMISS') || to_char(x_record_num, '0000000000')) last_rec
						from std.payer_plan_period
						group by payer_plan_period_source_identifier, x_data_source_id) ppp_maxrec ON ppp1.payer_plan_period_source_identifier = ppp_maxrec.payer_plan_period_source_identifier
						and ppp1.x_data_source_id = ppp_maxrec.x_data_source_id
						and to_char(ppp1.x_etl_date, 'YYYYMMDDHHMISS') || to_char(ppp1.x_record_num, '0000000000') = ppp_maxrec.last_rec) ppp
--			join (select x_data_source_id, min(x_record_num::bigint) x_record_num from std.payer_plan_period group by x_data_source_id, payer_plan_period_source_identifier, person_source_value) p1
--				on ppp.x_record_num::bigint = p1.x_record_num::bigint
---				and ppp.x_data_source_id = p1.x_data_source_id
			inner join cz.cz_id_map p on p.source_value = ppp.person_source_value
				  and p.x_data_source_id = ppp.x_data_source_id
				  and p.id_type = 'person_id'
			inner join omop.person op
				  on  p.id = op.person_id;
		get diagnostics v_row_count := row_count;
		exception 
		when others then
			perform cz.czx_write_error(v_step_id, v_function_name, v_step_ct, 'ERROR', sqlstate, sqlerrm);
			v_job_status := 'ERROR';
			return 16;
		end;
		perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'insert payer_plan_period', v_row_count, v_step_ct, 'SUCCESS');
		v_step_ct := v_step_ct + 1;

		select count(*) into v_row_count
		from  std.payer_plan_period c 	 
		inner join cz.cz_id_map p 
			  on  c.person_source_value = p.source_value 
			  and c.x_data_source_id = p.x_data_source_id
			  and p.id_type = 'person_id'
		where not exists
			  (select 1 from omop.person op
			   where p.id = op.person_id);
		if v_row_count > 0 then
			perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'payer_plan_period records not loaded, person linked in cz.cz_id_map but no omop.person record', v_row_count, v_step_ct, 'WARNING');
			v_step_ct := v_step_ct + 1;
		end if;		
		
	--if p_step_id is null or p_step_id < 1 then
	--	perform cz.czx_end_audit(v_step_id, v_job_status);
	--end if;
	
	perform cz.czx_write_audit(v_step_id, v_database_name, v_function_name, 'end omx_load_payer_plan_period', v_row_count, v_step_ct, 'COMPLETE');
	return 0;

end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION omop.omx_load_payer_plan_period(bigint)
  OWNER TO rosita;